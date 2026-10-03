package factoriaf5.team2.goxu.orders.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDTORequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long userId;

    private String tableNumber;

    @NotEmpty(message = "El pedido debe tener al menos un producto")
    @Valid
    private List<OrderItemDTORequest> items;

}