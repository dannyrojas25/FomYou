package com.examenfomyou;

import com.examenfomyou.model.*;
import com.examenfomyou.repository.PreguntaRepository;
import com.examenfomyou.service.PreguntaService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PreguntaServiceTest {
    @Mock
    private PreguntaRepository preguntaRepositoryMock;

    @InjectMocks
    private PreguntaService preguntaService;

    @Test
    public void testGuardarPreguntas() {
        Pregunta pregunta1 = Pregunta.builder()
                .enunciado("¿Cuál es la capital de Francia?")
                .examen(Examen.builder()
                        .idExamen(1L)
                        .build())
                .respuestas(Arrays.asList(
                        Mockito.mock(Respuesta.class),
                        Mockito.mock(Respuesta.class),
                        Mockito.mock(Respuesta.class),
                        Mockito.mock(Respuesta.class)
                ))
                .build();

        Pregunta pregunta2 = Pregunta.builder()
                .enunciado("¿Cuál es la capital de España?")
                .examen(Examen.builder()
                        .idExamen(1L)
                        .build())
                .respuestas(Arrays.asList(
                        Mockito.mock(Respuesta.class),
                        Mockito.mock(Respuesta.class),
                        Mockito.mock(Respuesta.class),
                        Mockito.mock(Respuesta.class)
                ))
                .build();

        List<Pregunta> preguntas = Arrays.asList(pregunta1, pregunta2);

        preguntaService.guardarPreguntas(preguntas);

        verify(preguntaRepositoryMock, times(1)).save(pregunta1);
        verify(preguntaRepositoryMock, times(1)).save(pregunta2);

        for (Respuesta respuesta : pregunta1.getRespuestas()) {
            verify(respuesta, times(1)).setPregunta(pregunta1);
        }

        for (Respuesta respuesta : pregunta2.getRespuestas()) {
            verify(respuesta, times(1)).setPregunta(pregunta2);
        }
    }
}
