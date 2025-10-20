package com.deliverytech.delivery_api.external;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DistanceApiClient {

    public BigDecimal calcularDistanciaKm(String cepOrigem, String cepDestino) {       
        return BigDecimal.valueOf(5);
    }
}
