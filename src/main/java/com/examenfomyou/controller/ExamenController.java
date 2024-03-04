package com.examenfomyou.controller;

import com.examenfomyou.model.Examen;
import com.examenfomyou.service.ExamenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/examenes")
public class ExamenController {
    private final ExamenService examenService;

    public ExamenController(ExamenService examenService) {
        this.examenService = examenService;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearExamen(@RequestBody Examen examen){
        examenService.crearExamen(examen);
        return ResponseEntity.ok("Examen creado con exito.");
    }

    @GetMapping("/realizar-examen/{idEstudiante}")
    public ResponseEntity<String> realizarExamen(@PathVariable Long idEstudiante){
        examenService.realizarExamen(idEstudiante);
        return ResponseEntity.ok("Examen realizado satisfactoriamente");
    }
}
