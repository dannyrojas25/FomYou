package com.examenfomyou.service;

import com.examenfomyou.model.Asignacion;
import com.examenfomyou.repository.AsignacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AsignacionService {
    private final AsignacionRepository asignacionRepository;

    public AsignacionService(AsignacionRepository asignacionRepository) {
        this.asignacionRepository = asignacionRepository;
    }

    public ResponseEntity<String> crearAsignacion(Asignacion asignacion){
        try {
            asignacionRepository.save(asignacion);
            return ResponseEntity.ok("El examen fue asignado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar el examen.");
        }
    }
}
