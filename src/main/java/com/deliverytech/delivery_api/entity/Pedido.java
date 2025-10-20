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
    private String EnderecoDeEntrega;
    private BigDecimal taxaDeEntrega;
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

     public Pedido() {
        
    }

    public Pedido(Cliente cliente, Restaurante restaurante, List<ItemPedido> itens, StatusPedido statusPedido, String observacoes, BigDecimal taxaDeEntrega, String enderecoDeEntrega) {
        
        this.validarDados(cliente, restaurante, itens);
        this.validarStatus(statusPedido, null);
        this.observacoes = observacoes;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.EnderecoDeEntrega = enderecoDeEntrega;
        this.taxaDeEntrega = taxaDeEntrega;
        this.itens = itens;
        this.status = statusPedido;
        this.dataPedido = LocalDateTime.now();
        this.status = statusPedido == null ? StatusPedido.PENDENTE : statusPedido;
        this.numeroPedido = this.numeroPedido == null ? "PED-" + System.currentTimeMillis() : this.numeroPedido;
        this.itens = itens;
        this.calcularValorTotal();
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
        return this.valorTotal;
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

    public void setCliente(Cliente cliente) {
        this.validarClienteAtivo(cliente);
        this.cliente = cliente;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.validarRestauranteAtivo(restaurante);
        this.restaurante = restaurante;
    }

    public void setItens(List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }
        this.itens = itens;
        this.itens.forEach(item -> item.setPedido(this));
    }

    public void setStatus(StatusPedido status, String motivo) {
        validarStatus(status, motivo);
        this.status = status;
    }

    //Regras de negócio
    private void validarDados(Cliente cliente, Restaurante restaurante, List<ItemPedido> itens) {
        validarClienteAtivo(cliente);
        validarRestauranteAtivo(restaurante);
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }
    }

    private void validarClienteAtivo(Cliente cliente) {
        if (Objects.isNull(cliente) || !cliente.isAtivo()) {
            throw new IllegalArgumentException("Cliente inválido ou inativo.");
        }
    }

    private void validarRestauranteAtivo(Restaurante restaurante) {
        if (Objects.isNull(restaurante) || !restaurante.isAtivo()) {
            throw new IllegalArgumentException("Restaurante inválido ou inativo.");
        }
    }

    private void validarStatus(StatusPedido novoStatus, String motivo) {

        if (this.status == StatusPedido.CANCELADO) {
            throw new IllegalArgumentException("Pedido cancelado não pode ter status alterado");
        }

        if(this.status != null){
             switch (novoStatus) {
                case CONFIRMADO:
                    if (this.status != StatusPedido.PENDENTE) {
                        throw new IllegalArgumentException("Apenas pedidos pendentes podem ser confirmados");
                    }

                    break;
                case PREPARANDO:
                    if (this.status != StatusPedido.CONFIRMADO) {
                        throw new IllegalArgumentException("Apenas pedidos confirmados podem entrar em preparo");
                    }
                    break;
                case SAIU_PARA_ENTREGA:
                    if (this.status != StatusPedido.PREPARANDO) {
                        throw new IllegalArgumentException("Apenas pedidos em preparo podem sair para entrega");
                    }
                    break;
                case ENTREGUE:
                    if (this.status != StatusPedido.SAIU_PARA_ENTREGA) {
                        throw new IllegalArgumentException("Apenas pedidos que saíram para entrega podem ser entregues");
                    }
                    break;
                case CANCELADO:
                    if (this.status == StatusPedido.ENTREGUE) { 
                        throw new IllegalArgumentException("Pedido já entregue não pode ser cancelado"); 
                    } 
    
                    if (this.status == StatusPedido.CANCELADO) { 
                        throw new IllegalArgumentException("Pedido já está cancelado"); 
                    } 

                    if (motivo != null && !motivo.trim().isEmpty()) { 
                        this.observacoes = " | Cancelado: " + motivo; 
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Transição de status inválida");
            }
        }
    }

   private void calcularValorTotal() {
        BigDecimal valorTotalItens = (this.itens == null || this.itens.isEmpty())
    ? BigDecimal.ZERO
    : this.itens.stream()
        .filter(Objects::nonNull)
        .map(ItemPedido::getSubtotal)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);     

        this.valorTotal = valorTotalItens
                            .add(this.taxaDeEntrega);

    }
}
