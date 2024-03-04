package com.examenfomyou;

import com.examenfomyou.model.Estudiante;
import com.examenfomyou.repository.EstudianteRepository;
import com.examenfomyou.service.EstudianteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EstudianteServiceTest {
    @Mock
    private EstudianteRepository estudianteRepositoryMock;

    @InjectMocks
    private EstudianteService estudianteService;

    @Test
    public void testCrearEstudianteExitoso() {
        Estudiante estudiante = Estudiante.builder()
                .nombre("Francisco Rojas")
                .edad(12)
                .ciudad("Bogotá")
                .zonaHoraria("America/Bogotá")
                .build();

        ResponseEntity<String> response = estudianteService.crearEstudiante(estudiante);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Estudiante creado exitosamente.", response.getBody());

        verify(estudianteRepositoryMock, times(1)).save(estudiante);
    }

    @Test
    public void testCrearEstudianteConError() {
        Estudiante estudiante = Estudiante.builder()
                .nombre("Francisco Rojas")
                .ciudad("Bogotá")
                .zonaHoraria("America/Bogotá")
                .build();
        doThrow(new RuntimeException("Error simulado")).when(estudianteRepositoryMock).save(estudiante);

        ResponseEntity<String> response = estudianteService.crearEstudiante(estudiante);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al crear el estudiante.", response.getBody());

        verify(estudianteRepositoryMock, times(1)).save(estudiante);
    }
}
