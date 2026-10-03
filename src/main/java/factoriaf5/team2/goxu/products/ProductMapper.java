package factoriaf5.team2.goxu.products;

import org.springframework.stereotype.Component;

import factoriaf5.team2.goxu.products.dtos.ProductDTORequest;
import factoriaf5.team2.goxu.products.dtos.ProductDTOResponse;

@Component
public class ProductMapper {

    public ProductEntity toEntity(ProductDTORequest request) {
        return ProductEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .image(request.getImage())
                .category(request.getCategory())
                .available(request.isAvailable())
                .featured(request.isFeatured())
                .badgeLabel(request.getBadgeLabel())
                .badgeTone(request.getBadgeTone())
                .build();
    }

    public ProductDTOResponse toResponse(ProductEntity entity) {
        return ProductDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .image(entity.getImage())
                .category(entity.getCategory())
                .available(entity.isAvailable())
                .featured(entity.isFeatured())
                .badgeLabel(entity.getBadgeLabel())
                .badgeTone(entity.getBadgeTone())
                .build();
    }

}