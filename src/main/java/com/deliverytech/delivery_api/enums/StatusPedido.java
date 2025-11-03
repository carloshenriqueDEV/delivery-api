package com.deliverytech.delivery_api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status possíveis de um pedido no sistema")
public enum StatusPedido {
    @Schema(description = "Pedido foi criado mas ainda não foi confirmado pelo restaurante")
    CRIADO, 
    @Schema(description = "Pedido foi confirmado pelo restaurante")        
    CONFIRMADO, 
    @Schema(description = "Pedido está sendo preparado na cozinha") 
    PREPARANDO, 
    @Schema(description = "Pedido está pronto e aguardando entregador")
    PRONTO, 
    @Schema(description = "Pedido saiu para entrega")
    SAIU_PARA_ENTREGA,
     @Schema(description = "Pedido foi entregue com sucesso ao cliente") 
    ENTREGUE, 
    @Schema(description = "Pedido foi cancelado pelo cliente") 
    CANCELADO,
    @Schema(description = "Pedido está aguardando confirmação do restaurante")
    PENDENTE     
}
