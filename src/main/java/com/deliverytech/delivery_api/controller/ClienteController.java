package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.entity.Cliente; 
import com.deliverytech.delivery_api.service.ClienteService;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*; 
 
import jakarta.validation.Valid;

import java.util.List; 
import java.util.Optional; 
 
@RestController 
@RequestMapping("/clientes") 
@CrossOrigin(origins = "*") 
public class ClienteController { 
 
    @Autowired 
    private ClienteService clienteService; 
 
    /** 
     * Cadastrar novo cliente 
     */ 
    @PostMapping 
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ClienteDTO cliente) { 
        try { 
            ClienteDTO clienteSalvo = clienteService.cadastrar(cliente); 
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo); 
        } catch (IllegalArgumentException e) { 

            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Listar todos os clientes a vos 
     */ 
    @GetMapping 
    public ResponseEntity<List<ClienteDTO>> listar() { 
        List<ClienteDTO> clientes = clienteService.listarAtivos(); 
        return ResponseEntity.ok(clientes); 
    } 
 
    /** 
     * Buscar cliente por ID 
     */ 
    @GetMapping("/{id}") 
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) { 
        Optional<ClienteDTO> cliente = clienteService.buscarPorId(id); 
 
        if (cliente.isPresent()) { 
            return ResponseEntity.ok(cliente.get()); 
        } else { 
            return ResponseEntity.notFound().build(); 
        } 
    } 
 
    /** 
     * Atualizar cliente 
     */ 
    @PutMapping("/{id}") 
    public ResponseEntity<?> atualizar(@PathVariable Long id, 
                                      @Valid @RequestBody ClienteDTO cliente) { 
        try { 
            ClienteDTO clienteAtualizado = clienteService.atualizar(cliente); 
            return ResponseEntity.ok(clienteAtualizado); 
        } catch (IllegalArgumentException e) { 

            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Ina var cliente (so delete) 
     */ 
    @DeleteMapping("/{id}") 
    public ResponseEntity<?> AtivarOuDesativar(@PathVariable Long id) { 
        try { 
            clienteService.ativarDesativar(id); 
            return ResponseEntity.ok().body("Cliente ina vado com sucesso"); 
        } catch (IllegalArgumentException e) { 

            if(e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + e.getMessage());
            }
            
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage()); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body("Erro interno do servidor"); 
        } 
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @GetMapping("/buscar") 
    public ResponseEntity<List<Cliente>> buscarPorNome(@RequestParam String nome) { 
        List<Cliente> clientes = clienteService.buscarPorNome(nome); 
        return ResponseEntity.ok(clientes); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @GetMapping("/email/{email}") 
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) { 
        Optional<ClienteDTO> cliente = clienteService.buscarPorEmail(email); 
 
        if (cliente.isPresent()) { 
            return ResponseEntity.ok(cliente.get()); 
        } else { 
            return ResponseEntity.notFound().build(); 
        } 
    } 
}