package com.examenfomyou.controller;

import com.examenfomyou.dto.PreguntaRespuestasDTO;
import com.examenfomyou.model.Examen;
import com.examenfomyou.service.ExamenService;
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
    public List<PreguntaRespuestasDTO> realizarExamen(@PathVariable Long idEstudiante){
        List<PreguntaRespuestasDTO> examen = examenService.realizarExamen(idEstudiante);
        return examen;
    }

    @PostMapping("/solucionar-examen/{idEstudiante}")
    public void responderExamen(@PathVariable Long idEstudiante){
        examenService.responderExamen(idEstudiante);
    }
}
