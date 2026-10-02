package factoriaf5.team2.goxu.products;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import factoriaf5.team2.goxu.products.dtos.ProductDTORequest;
import factoriaf5.team2.goxu.products.dtos.ProductDTOResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDTOResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public List<ProductDTOResponse> getFeatured() {
        return productRepository.findByFeaturedTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public List<ProductDTOResponse> getByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductDTOResponse getById(Long id) {
        ProductEntity product = findProductOrThrow(id);
        return productMapper.toResponse(product);
    }

    public ProductDTOResponse create(ProductDTORequest request) {
        ProductEntity product = productMapper.toEntity(request);
        ProductEntity saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    public ProductDTOResponse update(Long id, ProductDTORequest request) {
        ProductEntity product = findProductOrThrow(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImage(request.getImage());
        product.setCategory(request.getCategory());
        product.setAvailable(request.isAvailable());
        product.setFeatured(request.isFeatured());
        product.setBadgeLabel(request.getBadgeLabel());
        product.setBadgeTone(request.getBadgeTone());

        ProductEntity updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    public void delete(Long id) {
        ProductEntity product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    private ProductEntity findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con id " + id));
    }

}