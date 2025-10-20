package com.deliverytech.delivery_api.controller;

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
@RequestMapping("/api/clientes") 
@CrossOrigin(origins = "*") 
public class ClienteController { 
 
    @Autowired 
    private ClienteService clienteService; 
 
    /** 
     * Cadastrar novo cliente 
     */ 
    @PostMapping 
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ClienteDTO cliente) {       
        ClienteDTO clienteSalvo = clienteService.cadastrar(cliente); 
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo); 
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
            return ResponseEntity.ok(cliente.get()); 
    } 
 
    /** 
     * Atualizar cliente 
     */ 
    @PutMapping("/{id}") 
    public ResponseEntity<?> atualizar(@PathVariable Long id, 
                                      @Valid @RequestBody ClienteDTO cliente) { 
    
        ClienteDTO clienteAtualizado = clienteService.atualizar(cliente); 
        return ResponseEntity.ok(clienteAtualizado); 
     
    } 
 
    /** 
     * Ina var cliente (so delete) 
     */ 
    @DeleteMapping("/{id}") 
    public ResponseEntity<?> AtivarOuDesativar(@PathVariable Long id) { 
            clienteService.ativarDesativar(id); 
            return ResponseEntity.ok().body("Cliente inativado com sucesso"); 
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @GetMapping("/buscar") 
    public ResponseEntity<List<ClienteDTO>> buscarPorNome(@RequestParam String nome) { 
        List<ClienteDTO> clientes = clienteService.buscarPorNome(nome); 
        return ResponseEntity.ok(clientes); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @GetMapping("/email/{email}") 
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) { 
        Optional<ClienteDTO> cliente = clienteService.buscarPorEmail(email); 
 
        return ResponseEntity.ok(cliente.get()); 
    } 
    
    /*
     * Atualiza status do cliente (ativo/inativo)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
            clienteService.ativarDesativar(id);
            String status = ativo ? "ativado" : "inativado";
            return ResponseEntity.ok().body("Cliente " + status + " com sucesso");
    }
}