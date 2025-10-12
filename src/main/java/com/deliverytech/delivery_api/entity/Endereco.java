package com.deliverytech.delivery_api.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Classe Endereco representa um endereço físico.
 * Pode ser embutido em outras entidades, como Pedido, para armazenar o endereço de entrega.
 */
public class Endereco {

    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}