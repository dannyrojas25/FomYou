package com.examenfomyou;

import com.examenfomyou.dto.PreguntaRespuestasDTO;
import com.examenfomyou.dto.RespuestaDTO;
import com.examenfomyou.model.*;
import com.examenfomyou.repository.*;
import com.examenfomyou.service.ExamenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExamenServiceTest {
    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private PreguntaRepository preguntaRepository;

    @Mock
    private RespuestaRepository respuestaRepository;

    @Mock
    private ExamenRepository examenRepository;

    @Mock
    private RespuestasExamenRepository respuestasExamenRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private List<Object[]> preguntas;

    @Mock
    private List<Object[]> respuestas;

    @InjectMocks
    private ExamenService examenService;
    @Test
    public void testRealizarExamen() throws JsonProcessingException {
        when(asignacionRepository.findByEstudianteId(anyLong())).thenReturn(new Asignacion());
        lenient().when(examenRepository.findById(anyLong())).thenReturn(Optional.of(new Examen()));
        lenient().when(preguntaRepository.findByExamenId(anyLong())).thenReturn(Arrays.<Object[]>asList(new Object[]{1L, "Pregunta 1"}));
        lenient().when(respuestaRepository.findRespuestasByPreguntaIds(anyList())).thenReturn(Arrays.<Object[]>asList(new Object[]{1L, "Respuesta 1"}));

        lenient().when(objectMapper.writeValueAsString(any())).thenReturn("Examen simulado");

        ResponseEntity<String> response = examenService.realizarExamen(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testConstruirJsonExamen() {
        MockitoAnnotations.openMocks(this);

        List<Object[]> preguntaArrays = Arrays.asList(new Object[][]{new Object[]{1L, "Enunciado de prueba"}});
        List<Object[]> respuestaArrays = Arrays.asList(new Object[][]{new Object[]{1L, "Opción A", "Texto de la respuesta", 1L}});

        when(preguntas.size()).thenReturn(1);
        when(preguntas.get(0)).thenReturn(preguntaArrays.get(0));

        when(respuestas.size()).thenReturn(1);
        when(respuestas.get(0)).thenReturn(respuestaArrays.get(0));

        List<PreguntaRespuestasDTO> result = examenService.construirJsonExamen(preguntas, respuestas);

        System.out.println("Tamaño result después: " + result.size());
        System.out.println("Contenido de result: " + result);

        assertEquals(1, result.size());

        PreguntaRespuestasDTO preguntaDTO = result.get(0);
        Assert.assertEquals(1L, preguntaDTO.getIdPregunta().longValue());
        assertEquals("Enunciado de prueba", preguntaDTO.getEnunciado());
        assertEquals(1, preguntaDTO.getRespuestas().size());

        RespuestaDTO respuestaDTO = preguntaDTO.getRespuestas().get(0);
        assertEquals(1L, respuestaDTO.getIdRespuesta().longValue());
        assertEquals("Opción A", respuestaDTO.getOpcion());
        assertEquals("Texto de la respuesta", respuestaDTO.getOpcRespuesta());
    }

}
