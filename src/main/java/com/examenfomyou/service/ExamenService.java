package com.examenfomyou.service;

import com.examenfomyou.dto.PreguntaRespuestasDTO;
import com.examenfomyou.dto.RespuestaDTO;
import com.examenfomyou.model.*;
import com.examenfomyou.model.RespuestasExamen;
import com.examenfomyou.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public ExamenService(ExamenRepository examenRepository, AsignacionRepository asignacionRepository, PreguntaRepository preguntaRepository,
                         RespuestaRepository respuestaRepository, RespuestasExamenRepository respuestasExamenRepository, ObjectMapper objectMapper) {
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

        List<Object> idsPreguntas = preguntas.stream()
                .filter(pregunta -> pregunta != null && pregunta.length > 0)
                .map(pregunta -> pregunta[0])
                .toList();

        if (idsPreguntas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Object[]> respuestas = respuestaRepository.findRespuestasByPreguntaIds(idsPreguntas);
        examenPresentar = construirJsonExamen(preguntas, respuestas);

        if (examen.isPresent()) {
            Examen examenOpcional = examen.get();
            try {
                String examenPresentarJson = objectMapper.writeValueAsString(examenPresentar);
                examenRepository.guardarExamen(examenPresentarJson, examenOpcional.getIdExamen());
                return ResponseEntity.ok("Examen creado exitosamente.");
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Error al convertir el objeto a JSON: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el examen correspondiente a la asignación.");
        }
    }

    public List<PreguntaRespuestasDTO> construirJsonExamen(List<Object[]> preguntas, List<Object[]> respuestas) {
        Map<Long, PreguntaRespuestasDTO> mapaPreguntas = new HashMap<>();

        if (preguntas != null && !preguntas.isEmpty()) {
            for (int i = 0; i < preguntas.size(); i++) {
                Object[] preguntaArray = preguntas.get(i);
                if (preguntaArray != null && preguntaArray.length >= 2) {
                    Long idPregunta = (Long) preguntaArray[0];
                    String enunciado = (String) preguntaArray[1];

                    PreguntaRespuestasDTO preguntaDTO = new PreguntaRespuestasDTO();
                    preguntaDTO.setIdPregunta(idPregunta);
                    preguntaDTO.setEnunciado(enunciado);
                    preguntaDTO.setRespuestas(new ArrayList<>());

                    mapaPreguntas.put(idPregunta, preguntaDTO);
                }
            }
        }

        if (respuestas != null && !respuestas.isEmpty()) {
            for (int i = 0; i < respuestas.size(); i++) {
                Object[] respuestaArray = respuestas.get(i);
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
            }
        }

        return new ArrayList<>(mapaPreguntas.values());
    }

    public ResponseEntity<String> solucionarExamen(List<RespuestasExamen> listadoRespuestas, Long idExamen){
        List<RespuestasExamen> respuestasAGuardar = new ArrayList<>();
        boolean esCorrecta = true;
        boolean realizado = false;
        int indice = 0;
        int calificacionTotal = 0;

        Asignacion verificarDisposicionExamen = asignacionRepository.findByEstudianteId(listadoRespuestas.get(0).getIdEstudiante());

        if(verificarDisposicionExamen.isRealizado()){
            return ResponseEntity.ok("Ya realizaste el examen, tu calificación fue de " + verificarDisposicionExamen.getCalificacion() + " puntos de 100 disponibles");
        }

        List<Long> listaIdsPreguntas = listadoRespuestas.stream()
                .map(RespuestasExamen::getIdPregunta)
                .toList();

        List<Respuesta> respuestasCorrestas = respuestaRepository.encontarRespuestasPorIdPreguntaYEsCorrecta(listaIdsPreguntas, esCorrecta);

        for (RespuestasExamen respuesta : listadoRespuestas) {
            if((Objects.equals(respuestasCorrestas.get(indice).getOpcRespuesta(), respuesta.getOpcMarcada()))){
                calificacionTotal += respuesta.getCalificacion();
            }else {
                respuesta.setCalificacion(0);
            }
            RespuestasExamen actualizarObjetoRespuesta = new RespuestasExamen();
            actualizarObjetoRespuesta.setIdEstudiante(respuesta.getIdEstudiante());
            actualizarObjetoRespuesta.setIdPregunta(respuesta.getIdPregunta());
            actualizarObjetoRespuesta.setIdRespuesta(respuesta.getIdRespuesta());
            actualizarObjetoRespuesta.setCalificacion(respuesta.getCalificacion());
            actualizarObjetoRespuesta.setOpcion(respuesta.getOpcion());
            actualizarObjetoRespuesta.setOpcMarcada(respuesta.getOpcMarcada());

            respuestasAGuardar.add(actualizarObjetoRespuesta);
            indice ++;
        }
        String mensaje = "";
        if(!respuestasAGuardar.isEmpty()){
            respuestasExamenRepository.saveAll(respuestasAGuardar);
            realizado = true;
            asignacionRepository.actualizarCalificacionExamen(calificacionTotal, realizado, idExamen);

            mensaje = "Tu calificación del examen es: " + calificacionTotal + " puntos de 100 disponibles";
            return ResponseEntity.ok(mensaje);
        }else {
            mensaje = "Error al tratar de calificar el examen.";
            return ResponseEntity.badRequest().body(mensaje);
        }
    }
}
