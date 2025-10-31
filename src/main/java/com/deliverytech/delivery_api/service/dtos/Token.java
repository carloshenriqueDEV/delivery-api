package com.deliverytech.delivery_api.service.dtos;

public record Token(String value, Long expiration, String email) {}
