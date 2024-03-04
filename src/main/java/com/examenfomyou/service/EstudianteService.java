package com.examenfomyou.service;

import com.examenfomyou.model.Estudiante;
import com.examenfomyou.repository.EstudianteRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public ResponseEntity<String> crearEstudiante(Estudiante estudiante){
        try {
            estudianteRepository.save(estudiante);
            return ResponseEntity.ok("Estudiante creado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el estudiante.");
        }
    }
}
