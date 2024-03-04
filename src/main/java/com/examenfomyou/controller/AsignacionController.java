package com.examenfomyou.controller;

import com.examenfomyou.model.Asignacion;
import com.examenfomyou.service.AsignacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asignacion")
public class AsignacionController {
    private final AsignacionService asignacionService;

    public AsignacionController(AsignacionService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearAsignacion(@RequestBody Asignacion asignacion){
        asignacionService.crearAsignacion(asignacion);
        return ResponseEntity.ok("El examen fue asignado exitosamente.");
    }
}
