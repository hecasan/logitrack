package com.ags.logitrack.controller;

import com.ags.logitrack.model.RoboLogistico;
import com.ags.logitrack.service.RoboLogisticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/robos")
public class RoboLogisticoController {

    @Autowired
    private RoboLogisticoService roboLogisticoService;

    @GetMapping
    public ResponseEntity<List<RoboLogistico>> listarTodos() {
        return ResponseEntity.ok(roboLogisticoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoboLogistico> buscarPorId(@PathVariable Long id) {
        Optional<RoboLogistico> robo = roboLogisticoService.findById(id);
        return robo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoboLogistico> salvar(@RequestBody RoboLogistico roboLogistico) {
        RoboLogistico novoRobo = roboLogisticoService.save(roboLogistico);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRobo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoboLogistico> atualizar(@PathVariable Long id, @RequestBody RoboLogistico roboLogistico) {
        RoboLogistico roboAtualizado = roboLogisticoService.update(id, roboLogistico);
        if (roboAtualizado != null) {
            return ResponseEntity.ok(roboAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        roboLogisticoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RoboLogistico> atualizarStatus(@PathVariable Long id, @RequestBody String status) {
        RoboLogistico roboAtualizado = roboLogisticoService.atualizarStatus(id, status);
        if (roboAtualizado != null) {
            return ResponseEntity.ok(roboAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/bateria")
    public ResponseEntity<RoboLogistico> atualizarNivelEnergia(@PathVariable Long id, @RequestBody Double nivelBateria) {
        RoboLogistico roboAtualizado = roboLogisticoService.atualizarNivelBateria(id, nivelBateria);
        if (roboAtualizado != null) {
            return ResponseEntity.ok(roboAtualizado);
        }
        return ResponseEntity.notFound().build();
    }
}
