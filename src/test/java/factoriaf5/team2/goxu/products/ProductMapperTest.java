package factoriaf5.team2.goxu.products;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import factoriaf5.team2.goxu.products.dtos.ProductDTORequest;
import factoriaf5.team2.goxu.products.dtos.ProductDTOResponse;

/*
 * Tests unitarios de ProductMapper.
 * El mapper no depende de nada, así que se crea directamente con new y se comprueba
 * que copia todos los campos en las dos direcciones.
 */
class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    /* toEntity: copia todos los campos de la petición y deja el id sin asignar (lo genera la base de datos) */
    @Test
    void toEntity_copiesAllFieldsFromRequest() {
        ProductDTORequest request = ProductDTORequest.builder()
                .name("Cachopo Goxu")
                .description("Jamón serrano y queso, empanado.")
                .price(new BigDecimal("19.50"))
                .image("/menu-img/cachopo.png")
                .category("Especialidades")
                .available(true)
                .featured(true)
                .badgeLabel("Recomendado")
                .badgeTone("primary")
                .build();

        ProductEntity entity = mapper.toEntity(request);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo("Cachopo Goxu");
        assertThat(entity.getDescription()).isEqualTo("Jamón serrano y queso, empanado.");
        assertThat(entity.getPrice()).isEqualTo(new BigDecimal("19.50"));
        assertThat(entity.getImage()).isEqualTo("/menu-img/cachopo.png");
        assertThat(entity.getCategory()).isEqualTo("Especialidades");
        assertThat(entity.isAvailable()).isTrue();
        assertThat(entity.isFeatured()).isTrue();
        assertThat(entity.getBadgeLabel()).isEqualTo("Recomendado");
        assertThat(entity.getBadgeTone()).isEqualTo("primary");
    }

    /* toResponse: copia todos los campos de la entidad, incluido el id */
    @Test
    void toResponse_copiesAllFieldsFromEntity() {
        ProductEntity entity = ProductEntity.builder()
                .id(7L)
                .name("Tarta de queso")
                .description("Cremosa, con un ligero toque caramelizado.")
                .price(new BigDecimal("6.50"))
                .image("/menu-img/tarta_de_queso.png")
                .category("Postres")
                .available(false)
                .featured(false)
                .badgeLabel(null)
                .badgeTone(null)
                .build();

        ProductDTOResponse response = mapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(7L);
        assertThat(response.getName()).isEqualTo("Tarta de queso");
        assertThat(response.getDescription()).isEqualTo("Cremosa, con un ligero toque caramelizado.");
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("6.50"));
        assertThat(response.getImage()).isEqualTo("/menu-img/tarta_de_queso.png");
        assertThat(response.getCategory()).isEqualTo("Postres");
        assertThat(response.isAvailable()).isFalse();
        assertThat(response.isFeatured()).isFalse();
        assertThat(response.getBadgeLabel()).isNull();
        assertThat(response.getBadgeTone()).isNull();
    }
}
