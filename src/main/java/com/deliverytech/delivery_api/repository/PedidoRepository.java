package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.entity.Pedido; 
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.ItemPedido;
import com.deliverytech.delivery_api.enums.StatusPedido; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.query.Param; 
import org.springframework.stereotype.Repository;

import java.lang.foreign.Linker.Option;
import java.math.BigDecimal; 
import java.time.LocalDateTime; 
import java.util.List;
import java.util.Optional; 
 
@Repository 
public interface PedidoRepository extends JpaRepository<Pedido, Long> { 
 
    // Buscar pedidos por cliente 
    List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);  
 
    // Buscar por status 
    List<Pedido> findByStatusOrderByDataPedidoDesc(StatusPedido status); 


    @Query("""
        SELECT DISTINCT p FROM Pedido p
        LEFT JOIN FETCH p.itens i
        LEFT JOIN FETCH p.cliente c
        LEFT JOIN FETCH p.restaurante r
        WHERE p.id = :id
        """)
    Optional<Pedido> buscarPedidoCompleto(@Param("id") Long id);
 
    // Buscar por número do pedido 
    @Query("SELECT p FROM Pedido p WHERE p.id = :numeroPedido")
    Optional<Pedido> findByNumeroPedido(@Param("numeroPedido") String numeroPedido);
 
    // Buscar pedidos por período 
    List<Pedido> findByDataPedidoBetweenOrderByDataPedidoDesc(LocalDateTime inicio, LocalDateTime fim); 
 
    // Buscar pedidos do dia 
    @Query("SELECT p FROM Pedido p WHERE p.dataPedido >= :inicio AND p.dataPedido < :fim ORDER BY p.dataPedido DESC")
       List<Pedido> findPedidosDodia(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // Buscar pedidos por restaurante 
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC") 
    List<Pedido> findByRestauranteId(@Param("restauranteId") Long restauranteId); 
 
    // Relatório - pedidos por status 
    @Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status") 
    List<Object[]> countPedidosByStatus(); 
 
    // Pedidos pendentes (para dashboard) 
    @Query("SELECT p FROM Pedido p WHERE p.status IN ('PENDENTE', 'CONFIRMADO', 'PREPARANDO') " + 
           "ORDER BY p.dataPedido ASC") 
    List<Pedido> findPedidosPendentes(); 
 
    // Valor total de vendas por período 
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim " + 
           "AND p.status NOT IN ('CANCELADO')") 
    BigDecimal calcularVendasPorPeriodo(@Param("inicio") LocalDateTime inicio, 
                                       @Param("fim") LocalDateTime fim); 

   List<ItemPedido> findItensByPedidoId(Long pedidoId);

   // Buscar os 10 pedidos mais recentes
   List<Pedido> findTop10ByOrderByDataPedidoDesc();

   // Buscar pedidos com valor total acima de um valor específico
   List<Pedido> findByValorTotalGreaterThan(BigDecimal valor);

   // Relatório - pedidos por período e status
   @Query("SELECT p FROM Pedido p " + 
       "WHERE p.dataPedido BETWEEN :inicio AND :fim " + 
       "AND p.status = :status " + 
       "ORDER BY p.dataPedido DESC") 
   List<Pedido> relatorioPedidosPorPeriodoEStatus( 
      @Param("inicio") LocalDateTime inicio, 
      @Param("fim") LocalDateTime fim, 
      @Param("status") StatusPedido status 
   );
}