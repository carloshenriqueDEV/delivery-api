package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.service.ClienteService;
import com.deliverytech.delivery_api.service.dtos.ClienteDTO;
import com.deliverytech.delivery_api.service.dtos.ClienteResponseDTO;

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
//Todo: incluir paginação nos endpoints que trazem uma grande massa de dados
//Todo: incluir api response wrapper para todos os endpoint
//Todo: remover endpoints redundantes exemplos AtivarOuDesativar e atualizarStatus
//Todo: incluir versionamento de api
public class ClienteController { 
 
    @Autowired 
    private ClienteService clienteService; 
 
    /** 
     * Cadastrar novo cliente 
     */ 
    @PostMapping 
    public ResponseEntity<ClienteResponseDTO> cadastrar(@Valid @RequestBody ClienteDTO cliente) {       
        ClienteResponseDTO clienteSalvo = clienteService.cadastrar(cliente); 
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo); 
    } 
 
    /** 
     * Listar todos os clientes a vos 
     */ 
    @GetMapping 
    public ResponseEntity<List<ClienteResponseDTO>> listar() { 
        List<ClienteResponseDTO> clientes = clienteService.listarAtivos(); 
        return ResponseEntity.ok(clientes); 
    } 
 
    /** 
     * Buscar cliente por ID 
     */ 
    @GetMapping("/{id}") 
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) { 
        Optional<ClienteResponseDTO> cliente = clienteService.buscarPorId(id); 
            return ResponseEntity.ok(cliente.get()); 
    } 
 
    /** 
     * Atualizar cliente 
     */ 
    @PutMapping("/{id}") 
    public ResponseEntity<?> atualizar(@PathVariable Long id, 
                                      @Valid @RequestBody ClienteDTO cliente) { 
    
        ClienteResponseDTO clienteAtualizado = clienteService.atualizar(id, cliente); 
        return ResponseEntity.ok(clienteAtualizado); 
     
    } 
 
    /** 
     * Inativar cliente (so delete) 
     */ 
    @DeleteMapping("/{id}") 
    public ResponseEntity<?> AtivarOuDesativar(@PathVariable Long id, @RequestParam Boolean ativo) { 
            clienteService.ativarDesativar(id, ativo); 
            return ResponseEntity.ok().body("Cliente inativado com sucesso"); 
    } 
 
    /** 
     * Buscar clientes por nome 
     */ 
    @GetMapping("/buscar") 
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@RequestParam String nome) { 
        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome); 
        return ResponseEntity.ok(clientes); 
    } 
 
    /** 
     * Buscar cliente por email 
     */ 
    @GetMapping("/email/{email}") 
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) { 
        Optional<ClienteResponseDTO> cliente = clienteService.buscarPorEmail(email); 
 
        return ResponseEntity.ok(cliente.get()); 
    } 
    
    /*
     * Atualiza status do cliente (ativo/inativo)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
            clienteService.ativarDesativar(id, ativo);
            String status = ativo ? "ativado" : "inativado";
            return ResponseEntity.ok().body("Cliente " + status + " com sucesso");
    }
}