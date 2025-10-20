package com.deliverytech.delivery_api.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.entity.ItemPedido;
import com.deliverytech.delivery_api.entity.Pedido;
import com.deliverytech.delivery_api.entity.Produto;
import com.deliverytech.delivery_api.entity.Restaurante;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.repository.*;;

@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired 
    PedidoRepository pedidoRepository;
    @Autowired 
    RestauranteRepository restauranteRepository;
    @Autowired
    ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) throws Exception {
       System.out.println("=== INICIANDO CARGA DE DADOS DE TESTE ===");
       carregarClientes();
       carregarRestaurantes();       
       testarConsultas();
       System.out.println("=== CARGA DE DADOS DE TESTE CONCLUÍDA ===");
    }

    private void carregarClientes(){
        System.out.println("--- Inserindo clientes --- ");
        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Carlos Santos");
        cliente1.setEmail("jc@email.com");
        cliente1.setTelefone("11999999999");
        cliente1.setEndereco("Rua A, 231, São Paulo, SP");
        cliente1.setAtivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria Silva Oliveira");
        cliente2.setEmail("oli@emai.com");
        cliente2.setTelefone("11988888888");
        cliente2.setEndereco("Av. B, 123, Rio de Janeiro, RJ");
        cliente2.setAtivo(true);

        Cliente cliente3 = new Cliente();
        cliente3.setNome("Pedro Souza Lima");   
        cliente3.setEmail("lima@email.com");
        cliente3.setTelefone("11977777777");
        cliente3.setEndereco("Rua C, 456, Belo Horizonte, MG");
        cliente3.setAtivo(true);

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
        System.out.println("3 clientes inseridos.");
    }

    private void carregarRestaurantes(){
        System.out.println("Inciando carga de restaurantes...");
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Restaurante Bom Sabor");
        restaurante1.setCategoria("Comida Caseira");
        restaurante1.setEndereco("Rua X, 100, São Paulo, SP");
        restaurante1.setTelefone("1133333333");
        restaurante1.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante1.setAtivo(true);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Pizzaria Bella Italia");
        restaurante2.setCategoria("Pizza");
        restaurante2.setEndereco("Av. Y, 200, Rio de Janeiro, RJ");
        restaurante2.setTelefone("2122222222");
        restaurante2.setTaxaEntrega(new BigDecimal("7.50"));
        restaurante2.setAtivo(true);

        restauranteRepository.saveAll(Arrays.asList(restaurante1, restaurante2));
        System.out.println("2 restaurantes inseridos.");

        //incluir 10 produtos para cada restaurante
        System.out.println("Iniciando carga de produtos...");   
        for (int i = 1; i <= 10; i++) {
            var produto1 = new com.deliverytech.delivery_api.entity.Produto();
            produto1.setNome("Produto " + i + " do Restaurante 1");
            produto1.setDescricao("Descrição do Produto " + i);
            produto1.setPreco(new BigDecimal(10 + i));
            produto1.setDisponivel(true);
            produto1.setRestaurante(restaurante1);
            produtoRepository.save(produto1);

             var produto2 = new com.deliverytech.delivery_api.entity.Produto();
            produto2.setNome("Produto " + i + " do Restaurante 1");
            produto2.setDescricao("Descrição do Produto " + i);
            produto2.setPreco(new BigDecimal(10 + i));
            produto2.setDisponivel(true);
            produto2.setRestaurante(restaurante2);
            produtoRepository.save(produto2);
        }

        //incluir 5 pedidos para cada restaurante
        System.out.println("Iniciando carga de pedidos...");
        var clientes = clienteRepository.findAll();
        var produtosRest1 = produtoRepository.findByRestauranteAndDisponivelTrue(restaurante1);
        // var produtosRest2 = produtoRepository.findByRestauranteAndDisponivelTrue(restaurante2);
        for (int i = 1; i <= 5; i++) {
            
            Produto produto1 = produtosRest1.get(i % produtosRest1.size());
            ItemPedido item = new ItemPedido(4, BigDecimal.valueOf(1.5), produto1);
            List<ItemPedido> itensPedido1 = new ArrayList<ItemPedido>();
            itensPedido1.add(item);

            Pedido pedido1 = new Pedido(clientes.get(i % clientes.size()), restaurante1, itensPedido1, StatusPedido.ENTREGUE, null, BigDecimal.valueOf(0), "Rua dos bobos número 0.");
            
            pedidoRepository.save(pedido1);

            // var pedido2 = new Pedido();
            // pedido2.setCliente(clientes.get((i + 1) % clientes.size()));
            // pedido2.setRestaurante(restaurante2);
            // pedido2.setStatus(StatusPedido.ENTREGUE);
            // pedido2.setDataPedido(LocalDateTime.now().minusDays(10 - i));
            //  Produto produto2 = produtosRest1.getFirst();
            // ItemPedido item2 = new ItemPedido(4, BigDecimal.valueOf(1.5), produto2);
            // pedido2.setValorTotal(produtosRest2.get(i % produtosRest2.size()).getPreco());
            // pedidoRepository.save(pedido2);
        }
    }

    private void testarConsultas(){
        System.out.println("== TESTANDO CONSULTAS DOS REPOSITORIES ==");
        System.out.println("\n Testes ClientRepository" );

        var clientePorEmail = clienteRepository.findByEmail("lima@email.com");
        System.out.println("Cliente por email:" +
        clientePorEmail.map(Cliente::getNome).orElse("Não encontrado"));

        var clientesPorNome = clienteRepository.findByNomeContainingIgnoreCase("Silva");
        System.out.println("Clientes com 'Silva' no nome:" + clientesPorNome.size());

        boolean existsByEmail = clienteRepository.existsByEmail("lima@email.com");
        System.out.println("Existe cliente com email:" + existsByEmail);

        System.out.println("\n Testes RestauranteRepository");

        System.out.println("Restaurante com taxa <= 6.00: ");
        var restaurantesPorTaxa = restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("6.00"));
        restaurantesPorTaxa.forEach(r -> 
            System.out.println( r.getNome() + " - Taxa: " + r.getTaxaEntrega())
        );

        System.out.println("\n Relatório total de vendas por restaurante:");
        var totalVendasPorRestaurante = restauranteRepository.calcularTotalVendasPorRestaurante();
        totalVendasPorRestaurante.forEach(r -> 
            System.out.println("Restaurante: " + r.nome() + " - Total Vendas: " + r.valorTotal())
        );

        System.out.println("\n Testes PedidoRepository");

        System.out.println("Últimos 10 pedidos:");
        var ultimos10Pedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        ultimos10Pedidos.forEach(p -> 
            System.out.println("Pedido ID: " + p.getId() + " - Data: " + p.getDataPedido() + " - Valor: " + p.getValorTotal())
        );

        System.out.println("\n Pedidos com valor total acima de 50.00:");
        var pedidosAcimaDe50 = pedidoRepository.findByValorTotalGreaterThan(new BigDecimal("50.00"));
        pedidosAcimaDe50.forEach(p -> 
            System.out.println("Pedido ID: " + p.getId() + " - Valor: " + p.getValorTotal())
        );

        System.out.println("\n Relatório de pedidos por período e status:");
        var pedidosPeriodoStatus = pedidoRepository.relatorioPedidosPorPeriodoEStatus(
            LocalDateTime.now().minusDays(30), 
            LocalDateTime.now(), 
            StatusPedido.ENTREGUE
        );
        pedidosPeriodoStatus.forEach(p -> 
            System.out.println("Pedido ID: " + p.getId() + " - Data: " + p.getDataPedido() + " - Status: " + p.getStatus())
        );

        System.out.println("\n Relatório 10 pedidos mais recentes:");
        var pedidosRecentes = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        pedidosRecentes.forEach(p -> 
            System.out.println("Pedido ID: " + p.getId() + " - Data: " + p.getDataPedido())
        );

    }
    
}
