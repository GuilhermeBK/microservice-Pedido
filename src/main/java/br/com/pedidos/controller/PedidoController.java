package br.com.pedidos.controller;

import br.com.pedidos.dto.PedidoDto;
import br.com.pedidos.dto.StatusDto;
import br.com.pedidos.exception.PedidoException;
import br.com.pedidos.http.PagamentoDto;
import br.com.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping()
    public List<PedidoDto> listarTodos() {
        return service.obterTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> listarPorId(@PathVariable @NotNull Long id) {
        PedidoDto dto = service.obterPorId(id);

        return ResponseEntity.ok(dto);
    }

    //para permitir requisicoes do front
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping()
    public ResponseEntity<PedidoDto> realizaPedido(@RequestBody @Valid PedidoDto pedidoDto, PagamentoDto  pagamentoDto,UriComponentsBuilder uriBuilder) {
        try {
            PedidoDto pedidoRealizado = service.criarPedido(pedidoDto);

            URI uri = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.getId()).toUri();

            //exemplo de comunicaçao por rest template
            service.criarPagamento(pagamentoDto);
            return ResponseEntity.created(uri).body(pedidoRealizado);
        } catch (Exception e) {
            new PedidoException(e.getMessage());
            final ResponseEntity<PedidoDto> pedidoDtoResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return pedidoDtoResponseEntity;
        }


    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDto> atualizaStatus(@PathVariable Long id, @RequestBody StatusDto status) {
        PedidoDto dto = service.atualizaStatus(id, status);

        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}/pago")
    public ResponseEntity<Void> aprovaPagamento(@PathVariable @NotNull Long id) {
        service.aprovaPagamentoPedido(id);

        return ResponseEntity.ok().build();

    }
}
