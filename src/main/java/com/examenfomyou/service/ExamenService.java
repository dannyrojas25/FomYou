package com.examenfomyou.service;

import com.examenfomyou.dto.PreguntaRespuestasDTO;
import com.examenfomyou.dto.RespuestaDTO;
import com.examenfomyou.model.*;
import com.examenfomyou.model.RespuestasExamen;
import com.examenfomyou.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.boot.model.source.internal.hbm.ModelBinder.prepare;

@Service
public class ExamenService {
    private final ExamenRepository examenRepository;
    private final AsignacionRepository asignacionRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;
    private final RespuestasExamenRepository respuestasExamenRepository;
    private final ObjectMapper objectMapper;
    private static final String MENSAJE = "Examen creado exitosamente";

    @Autowired
    public ExamenService(ExamenRepository examenRepository, EstudianteRepository estudianteRepository, AsignacionRepository asignacionRepository, PreguntaRepository preguntaRepository, RespuestaRepository respuestaRepository, RespuestasExamen respuestasExamen, RespuestasExamenRepository respuestasExamenRepository, ObjectMapper objectMapper) {
        this.examenRepository = examenRepository;
        this.asignacionRepository = asignacionRepository;
        this.preguntaRepository = preguntaRepository;
        this.respuestaRepository = respuestaRepository;
        this.respuestasExamenRepository = respuestasExamenRepository;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<String> crearExamen(Examen examen) {
        try {
            examenRepository.save(examen);
            return ResponseEntity.ok(MENSAJE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el examen.");
        }
    }

    public ResponseEntity<String> realizarExamen(Long idEstudiante) throws JsonProcessingException {
        Asignacion asignacion = asignacionRepository.findByEstudianteId(idEstudiante);
        Optional<Examen> examen = examenRepository.findById(asignacion.getIdExamen());
        List<PreguntaRespuestasDTO> examenPresentar;
        List<Object[]> preguntas = preguntaRepository.findByExamenId(asignacion.getIdExamen());
        if (preguntas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista de preguntas vacía.");
        }
        List<Object> idsPreguntas = preguntas.stream()
                .filter(pregunta -> pregunta != null && pregunta.length > 0)
                .map(pregunta -> pregunta[0])
                .collect(Collectors.toList());
        if (idsPreguntas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista de los id's de las preguntas está vacía.");
        }
        List<Object[]> respuestas = respuestaRepository.findRespuestasByPreguntaIds(idsPreguntas);
        examenPresentar = construirJsonExamen(preguntas, respuestas);

        try {
            String examenPresentarJson = objectMapper.writeValueAsString(examenPresentar);
            examenRepository.guardarExamen(examenPresentarJson, examen.get().getIdExamen());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Error al convertir el objeto a JSON: " + e.getMessage());
        }
        return ResponseEntity.ok("Examen creado exitosamente.");
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
                respuestaDTO.setOpcRespuesta(textoRespuesta);

                PreguntaRespuestasDTO preguntaDTO = mapaPreguntas.get(idPregunta);
                if (preguntaDTO != null) {
                    preguntaDTO.getRespuestas().add(respuestaDTO);
                }
            }
        });

        return new ArrayList<>(mapaPreguntas.values());
    }

    public ResponseEntity<String> solucionarExamen(List<RespuestasExamen> listadoRespuestas, Long idExamen){
        List<RespuestasExamen> respuestasAGuardar = new ArrayList<>();
        boolean esCorrecta = true;
        int indice = 0;
        int calificacionTotal = 0;
        List<Long> listaIdsPreguntas = listadoRespuestas.stream()
                .map(RespuestasExamen::getIdPregunta)
                .collect(Collectors.toList());

        List<Respuesta> respuestasCorrestas = respuestaRepository.encontarRespuestasPorIdPreguntaYEsCorrecta(listaIdsPreguntas, esCorrecta);

        for (RespuestasExamen respuesta : listadoRespuestas) {
            if((Objects.equals(respuestasCorrestas.get(indice).getOpcRespuesta(), respuesta.getOpcMarcada()))){
                calificacionTotal += respuesta.getCalificacion();
            }
            RespuestasExamen actualizarObjetoRespuesta = new RespuestasExamen();
            actualizarObjetoRespuesta.setIdEstudiante(respuesta.getIdEstudiante());
            actualizarObjetoRespuesta.setIdPregunta(respuesta.getIdPregunta());
            actualizarObjetoRespuesta.setIdRespuesta(respuesta.getIdRespuesta());
            actualizarObjetoRespuesta.setCalificacion(respuesta.getCalificacion());
            actualizarObjetoRespuesta.setOpcion(respuesta.getOpcion());
            actualizarObjetoRespuesta.setOpcMarcada(respuesta.getOpcMarcada());
            actualizarObjetoRespuesta.setRespuestaExamen(respuesta.getOpcMarcada());

            respuestasAGuardar.add(actualizarObjetoRespuesta);


            indice ++;
        }

        respuestasExamenRepository.saveAll(respuestasAGuardar);
        asignacionRepository.actualizarCalificacionExamen(calificacionTotal, idExamen);
        return ResponseEntity.ok("Examen creado exitosamente.");
    }


}
