package com.examenfomyou.service;

import com.examenfomyou.dto.PreguntaRespuestasDTO;
import com.examenfomyou.dto.RespuestaDTO;
import com.examenfomyou.model.*;
import com.examenfomyou.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<PreguntaRespuestasDTO> realizarExamen(Long idEstudiante){
        Asignacion asignacion = asignacionRepository.findByEstudianteId(idEstudiante);
        if (asignacion == null) {
            return Collections.emptyList();
        }
        List<Object[]> preguntas = preguntaRepository.findByExamenId(asignacion.getIdExamen());
        if (preguntas.isEmpty()) {
            return Collections.emptyList();
        }
        List<Object> idsPreguntas = preguntas.stream()
                .filter(pregunta -> pregunta != null && pregunta.length > 0)
                .map(pregunta -> pregunta[0])
                .collect(Collectors.toList());
        if (idsPreguntas.isEmpty()) {
            return Collections.emptyList();
        }
        List<Object[]> respuestas = respuestaRepository.findRespuestasByPreguntaIds(idsPreguntas);

        return construirJsonExamen(preguntas, respuestas);
    }

    public List<PreguntaRespuestasDTO> construirJsonExamen(List<Object[]> preguntas, List<Object[]> respuestas) {
        Map<Long, PreguntaRespuestasDTO> mapaPreguntas = new HashMap<>();

        preguntas.forEach(preguntaArray -> {
            if (preguntaArray != null && preguntaArray.length >= 2) {
                Long idPregunta = (Long) preguntaArray[0];
                String enunciado = (String) preguntaArray[1];

                PreguntaRespuestasDTO preguntaDTO = new PreguntaRespuestasDTO();
                preguntaDTO.setIdPregunta(idPregunta);
                preguntaDTO.setEnunciado(enunciado);
                preguntaDTO.setRespuestas(new ArrayList<>());

                mapaPreguntas.put(idPregunta, preguntaDTO);
            }
        });

        respuestas.forEach(respuestaArray -> {
            if (respuestaArray != null && respuestaArray.length >= 4) {
                Long idPregunta = (Long) respuestaArray[3];
                Long idRespuesta = (Long) respuestaArray[0];
                String opcion = (String) respuestaArray[1];
                String textoRespuesta = (String) respuestaArray[2];

                RespuestaDTO respuestaDTO = new RespuestaDTO();
                respuestaDTO.setIdRespuesta(idRespuesta);
                respuestaDTO.setOpcion(opcion);
                respuestaDTO.setRespuesta(textoRespuesta);

                PreguntaRespuestasDTO preguntaDTO = mapaPreguntas.get(idPregunta);
                if (preguntaDTO != null) {
                    preguntaDTO.getRespuestas().add(respuestaDTO);
                }
            }
        });

        return new ArrayList<>(mapaPreguntas.values());
    }

    public void responderExamen(Long idEstudiante){

    }
}
