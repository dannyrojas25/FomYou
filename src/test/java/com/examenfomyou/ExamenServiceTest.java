package com.examenfomyou;

import com.examenfomyou.dto.PreguntaRespuestasDTO;
import com.examenfomyou.model.Asignacion;
import com.examenfomyou.model.Examen;
import com.examenfomyou.repository.AsignacionRepository;
import com.examenfomyou.repository.ExamenRepository;
import com.examenfomyou.repository.PreguntaRepository;
import com.examenfomyou.repository.RespuestaRepository;
import com.examenfomyou.service.ExamenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private ExamenService examenService;
    @Test
    public void testRealizarExamen() throws JsonProcessingException {
        Long idEstudiante = 1L;

        Asignacion asignacion = new Asignacion();
        asignacion.setId(1L);
        asignacion.setIdExamen(1L);

        Examen examen = Examen.builder()
                .descripcion("Examen de las capitales")
                .fecha(LocalDate.parse("03/03/2024"))
                .build();

        when(asignacionRepository.findByEstudianteId(anyLong())).thenReturn(asignacion);
        when(examenRepository.findById(anyLong())).thenReturn(Optional.of(examen));

        when(asignacionRepository.findByEstudianteId(idEstudiante)).thenReturn(asignacion);

        List<Object[]> preguntas = Arrays.asList(new Object[]{1L, "Pregunta 1"}, new Object[]{2L, "Pregunta 2"});
        when(preguntaRepository.findByExamenId(asignacion.getIdExamen())).thenReturn(preguntas);

        List<Object[]> respuestas = Arrays.asList(new Object[]{1L, "Respuesta 1"}, new Object[]{2L, "Respuesta 2"});
        when(respuestaRepository.findRespuestasByPreguntaIds(Arrays.asList(1L, 2L))).thenReturn(respuestas);

        List<PreguntaRespuestasDTO> resultado = (List<PreguntaRespuestasDTO>) examenService.realizarExamen(idEstudiante);

        assertEquals(2, resultado.size());
    }
}
