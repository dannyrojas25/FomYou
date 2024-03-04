package com.examenfomyou.service;

import com.examenfomyou.model.*;
import com.examenfomyou.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamenService {
    private final ExamenRepository examenRepository;
    private final EstudianteRepository estudianteRepository;
    private final AsignacionRepository asignacionRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;

    @Autowired
    public ExamenService(ExamenRepository examenRepository, EstudianteRepository estudianteRepository, AsignacionRepository asignacionRepository, PreguntaRepository preguntaRepository, RespuestaRepository respuestaRepository) {
        this.examenRepository = examenRepository;
        this.estudianteRepository = estudianteRepository;
        this.asignacionRepository = asignacionRepository;
        this.preguntaRepository = preguntaRepository;
        this.respuestaRepository = respuestaRepository;
    }

    public ResponseEntity<String> crearExamen(Examen examen) {
        try {
            examenRepository.save(examen);
            return ResponseEntity.ok("Examen creado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el examen.");
        }
    }

    public ResponseEntity<String> realizarExamen(Long idEstudiante){
        Optional<Estudiante> estudiante = estudianteRepository.findById(idEstudiante);
        Asignacion asignacion = asignacionRepository.findByEstudianteId(idEstudiante);
        List<Object[]> preguntas = preguntaRepository.findByExamenId(asignacion.getIdExamen());
        List<Object> idsPreguntas = new ArrayList<>();
        for (Object[] pregunta : preguntas) {
            if (pregunta != null && pregunta.length > 0) {
                idsPreguntas.add(pregunta[0]);
            }
        }
        List<Object[]> respuestas = respuestaRepository.findRespuestasByPreguntaIds(idsPreguntas);

       /* comprobarAsignacionExamen(estudiante.get().getId_estudiante());
        try {
            Optional estudiante = estudianteRepository.findById(idEstudiante);
            if(!estudiante.isEmpty()){
                comprarAsignacionExamen(estudiante);
            }
            examenRepository.save();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al realizar el examen.");
        }*/
        return ResponseEntity.ok("Todo bien");
    }
/*
    private void comprobarAsignacionExamen(long idEstudiante){
        Asignacion asignacion = asignacionRepository.findByEstudianteId(1L);
    }*/
}
