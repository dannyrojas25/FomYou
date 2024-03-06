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
        return examenService.crearExamen(examen);
    }

    @GetMapping("/realizar-examen/{idEstudiante}")
    public ResponseEntity<String> realizarExamen(@PathVariable Long idEstudiante) throws JsonProcessingException {
        return examenService.realizarExamen(idEstudiante);
    }
    @PostMapping("/solucionar-examen/{idExamen}")
    public ResponseEntity<String> solucionarExamen(@RequestBody List<RespuestasExamen> respuestasExamen, @PathVariable Long idExamen){
        return examenService.solucionarExamen(respuestasExamen, idExamen);
    }

}
