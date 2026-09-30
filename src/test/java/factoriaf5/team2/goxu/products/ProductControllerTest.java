package factoriaf5.team2.goxu.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import factoriaf5.team2.goxu.products.dtos.ProductDTORequest;
import factoriaf5.team2.goxu.products.dtos.ProductDTOResponse;

/*
 * Tests unitarios de ProductController.
 * El Service se sustituye por un mock y se llama directamente a los métodos del Controller
 * para comprobar el código de estado y el cuerpo de la respuesta (mismo patrón que OfferControllerTest).
 */
class ProductControllerTest {

    private ProductService service;
    private ProductController controller;

    /* Se ejecuta antes de cada test, para que cada uno empiece con objetos nuevos */
    @BeforeEach
    void setUp() {
        service = mock(ProductService.class);
        controller = new ProductController(service);
    }

    /* Crea un DTO de respuesta de prueba, para no repetir el builder en cada test */
    private ProductDTOResponse buildResponse(Long id, String category, boolean featured) {
        return ProductDTOResponse.builder()
                .id(id)
                .name("Fabada asturiana con compango")
                .description("La receta de siempre.")
                .price(new BigDecimal("16.00"))
                .image("/menu-img/fabada.png")
                .category(category)
                .available(true)
                .featured(featured)
                .build();
    }

    /* Crea un DTO de petición de prueba, como el que enviaría el panel de administración */
    private ProductDTORequest buildRequest() {
        return ProductDTORequest.builder()
                .name("Fabada asturiana con compango")
                .description("La receta de siempre.")
                .price(new BigDecimal("16.00"))
                .image("/menu-img/fabada.png")
                .category("Especialidades")
                .available(true)
                .featured(true)
                .build();
    }

    /* GET sin parámetros: responde 200 con todos los productos */
    @Test
    void getProducts_withoutParams_returns200WithAllProducts() {
        List<ProductDTOResponse> products = List.of(
                buildResponse(1L, "Especialidades", true),
                buildResponse(2L, "Postres", false));
        when(service.getAll()).thenReturn(products);

        ResponseEntity<List<ProductDTOResponse>> response = controller.getProducts(null, null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(products);
    }

    /* GET con featured=true: responde 200 solo con los destacados */
    @Test
    void getProducts_withFeaturedTrue_returns200WithFeaturedProducts() {
        List<ProductDTOResponse> featured = List.of(buildResponse(1L, "Especialidades", true));
        when(service.getFeatured()).thenReturn(featured);

        ResponseEntity<List<ProductDTOResponse>> response = controller.getProducts(null, true);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(featured);
    }

    /* GET con category y featured a la vez: el filtro featured tiene prioridad */
    @Test
    void getProducts_withCategoryAndFeatured_usesFeaturedFilter() {
        List<ProductDTOResponse> featured = List.of(buildResponse(1L, "Especialidades", true));
        when(service.getFeatured()).thenReturn(featured);

        ResponseEntity<List<ProductDTOResponse>> response = controller.getProducts("Postres", true);

        assertThat(response.getBody()).isEqualTo(featured);
    }

    /* GET con category: responde 200 con los productos de esa categoría */
    @Test
    void getProducts_withCategory_returns200WithProductsOfCategory() {
        List<ProductDTOResponse> desserts = List.of(buildResponse(2L, "Postres", false));
        when(service.getByCategory("Postres")).thenReturn(desserts);

        ResponseEntity<List<ProductDTOResponse>> response = controller.getProducts("Postres", null);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(desserts);
    }

    /* GET por id: responde 200 con el producto */
    @Test
    void getProductById_returns200WithProduct() {
        ProductDTOResponse expected = buildResponse(1L, "Especialidades", true);
        when(service.getById(1L)).thenReturn(expected);

        ResponseEntity<ProductDTOResponse> response = controller.getProductById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    /* POST de un producto válido: responde 201 con el producto creado */
    @Test
    void createProduct_returns201WithCreatedProduct() {
        ProductDTORequest request = buildRequest();
        ProductDTOResponse expected = buildResponse(1L, "Especialidades", true);
        when(service.create(request)).thenReturn(expected);

        ResponseEntity<ProductDTOResponse> response = controller.createProduct(request);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    /* PUT de un producto existente: responde 200 con el producto actualizado */
    @Test
    void updateProduct_returns200WithUpdatedProduct() {
        ProductDTORequest request = buildRequest();
        ProductDTOResponse expected = buildResponse(1L, "Especialidades", true);
        when(service.update(1L, request)).thenReturn(expected);

        ResponseEntity<ProductDTOResponse> response = controller.updateProduct(1L, request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    /* DELETE: responde 204 sin cuerpo y llama al Service con el id correcto */
    @Test
    void deleteProduct_returns204AndCallsService() {
        ResponseEntity<Void> response = controller.deleteProduct(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(204);
        assertThat(response.getBody()).isNull();
        verify(service).delete(1L);
    }
}
