package factoriaf5.team2.goxu.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import factoriaf5.team2.goxu.products.dtos.ProductDTORequest;
import factoriaf5.team2.goxu.products.dtos.ProductDTOResponse;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductEntity product;
    private ProductDTORequest request;
    private ProductDTOResponse response;

    @BeforeEach
    void setUp() {
        product = ProductEntity.builder()
                .id(1L)
                .name("Fabada Asturiana")
                .description("Plato tradicional asturiano")
                .price(new BigDecimal("14.50"))
                .image("fabada.jpg")
                .category("Platos principales")
                .available(true)
                .featured(true)
                .build();

        request = ProductDTORequest.builder()
                .name("Fabada Asturiana")
                .description("Plato tradicional asturiano")
                .price(new BigDecimal("14.50"))
                .image("fabada.jpg")
                .category("Platos principales")
                .available(true)
                .featured(true)
                .build();

        response = ProductDTOResponse.builder()
                .id(1L)
                .name("Fabada Asturiana")
                .description("Plato tradicional asturiano")
                .price(new BigDecimal("14.50"))
                .image("fabada.jpg")
                .category("Platos principales")
                .available(true)
                .featured(true)
                .build();
    }

    @Test
    void getAll_shouldReturnAllProductsMapped() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        List<ProductDTOResponse> result = productService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Fabada Asturiana");
    }

    @Test
    void getById_shouldReturnProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        ProductDTOResponse result = productService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getById_shouldThrowNotFound_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Producto no encontrado con id 99");
    }

    @Test
    void create_shouldSaveAndReturnMappedProduct() {
        when(productMapper.toEntity(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(response);

        ProductDTOResponse result = productService.create(request);

        assertThat(result.getName()).isEqualTo("Fabada Asturiana");
        verify(productRepository).save(product);
    }

    @Test
    void delete_shouldRemoveProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void delete_shouldThrowNotFound_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

    /* getFeatured: devuelve solo los productos destacados, convertidos a DTO */
    @Test
    void getFeatured_shouldReturnFeaturedProductsMapped() {
        when(productRepository.findByFeaturedTrue()).thenReturn(List.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        List<ProductDTOResponse> result = productService.getFeatured();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isFeatured()).isTrue();
        verify(productRepository).findByFeaturedTrue();
    }

    /* getByCategory: devuelve los productos de la categoría pedida, convertidos a DTO */
    @Test
    void getByCategory_shouldReturnProductsOfThatCategoryMapped() {
        when(productRepository.findByCategory("Platos principales")).thenReturn(List.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        List<ProductDTOResponse> result = productService.getByCategory("Platos principales");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("Platos principales");
        verify(productRepository).findByCategory("Platos principales");
    }

    /* update: modifica los campos del producto existente con los de la petición y lo guarda */
    @Test
    void update_shouldModifyAndSaveProduct_whenExists() {
        ProductDTORequest changes = ProductDTORequest.builder()
                .name("Fabada asturiana con compango")
                .description("La receta de siempre.")
                .price(new BigDecimal("16.00"))
                .image("/menu-img/fabada.png")
                .category("Especialidades")
                .available(false)
                .featured(false)
                .badgeLabel("Clásico")
                .badgeTone("highlight")
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(response);

        productService.update(1L, changes);

        /* La entidad ha recibido los valores nuevos antes de guardarse */
        assertThat(product.getName()).isEqualTo("Fabada asturiana con compango");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("16.00"));
        assertThat(product.getCategory()).isEqualTo("Especialidades");
        assertThat(product.isAvailable()).isFalse();
        assertThat(product.isFeatured()).isFalse();
        assertThat(product.getBadgeLabel()).isEqualTo("Clásico");
        verify(productRepository).save(product);
    }

    /* update: lanza 404 si el producto no existe */
    @Test
    void update_shouldThrowNotFound_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.update(99L, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Producto no encontrado con id 99");
    }
}