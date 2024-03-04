package com.examenfomyou.controller;

import com.examenfomyou.model.Estudiante;
import com.examenfomyou.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearEstudiante(@RequestBody Estudiante estudiante){
        estudianteService.crearEstudiante(estudiante);
        return ResponseEntity.ok("Estudiante guardado exitosamente.");
    }
}
