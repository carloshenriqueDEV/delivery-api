package com.deliverytech.delivery_api.entity;
 
import com.deliverytech.delivery_api.enums.StatusPedido;
import jakarta.persistence.*; 
import lombok.Data;
import java.math.BigDecimal; 

import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects; 
 
@Entity 
@Data 
public class Pedido { 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)     
    @Column(name = "pedido_id")
    private Long id; 

    @Column(name = "numero_pedido")
    private String numeroPedido;
 
    @Column(name ="data_pedido")
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING) 
    private StatusPedido status;
    private BigDecimal valorTotal;
    private String observacoes;  
   
 
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "cliente_id")
    private Cliente cliente; 
 
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante; 
 
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) 
    private List<ItemPedido> itens; 

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
        item.setPedido(this);
    }

    public void confirmar() {
        this.status = StatusPedido.CONFIRMADO;
    }

    @PrePersist
    public void prePersist() {
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.numeroPedido = "PED-" + System.currentTimeMillis();
        this.valorTotal = itens.stream()
            .filter(Objects::nonNull)
            .map(ItemPedido::getSubtotal)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Pedido() {
        super();
        this.itens = new java.util.ArrayList<ItemPedido>();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public BigDecimal getValorTotal() {
        return Objects.isNull(valorTotal) ? BigDecimal.ZERO : valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }
    
    public String getNumeroPedido() {
        return numeroPedido;
    }



   
}
