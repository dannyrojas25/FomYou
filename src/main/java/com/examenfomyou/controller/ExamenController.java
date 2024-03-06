package com.examenfomyou.controller;

import com.examenfomyou.model.Examen;
import com.examenfomyou.model.RespuestasExamen;
import com.examenfomyou.service.ExamenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> realizarExamen(@PathVariable Long idEstudiante) throws JsonProcessingException {
        examenService.realizarExamen(idEstudiante);
        return ResponseEntity.ok("Examen realizado con exito.");
    }
    @PostMapping("/solucionar-examen/{idExamen}")
    public ResponseEntity<String> solucionarExamen(@RequestBody List<RespuestasExamen> respuestasExamen, @PathVariable Long idExamen){
        examenService.solucionarExamen(respuestasExamen, idExamen);
        return ResponseEntity.ok("Examen resuelto y calificado con exito..");
    }

}
