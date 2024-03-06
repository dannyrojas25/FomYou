package com.examenfomyou;

import com.examenfomyou.model.Asignacion;
import com.examenfomyou.repository.AsignacionRepository;
import com.examenfomyou.service.AsignacionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsignacionServiceTest {
    @Mock
    private AsignacionRepository asignacionRepositoryMock;

    @InjectMocks
    private AsignacionService asignacionService;

    @Test
    public void testCrearAsignacionExitosa() {
        Asignacion asignacion = Asignacion.builder()
                .idEstudiante(1L)
                .idExamen(1L)
                .calificacion("0")
                .realizado(false)
                .build();

        ResponseEntity<String> response = asignacionService.crearAsignacion(asignacion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("El examen fue asignado exitosamente.", response.getBody());

        verify(asignacionRepositoryMock, times(1)).save(asignacion);
    }

    @Test
    public void testCrearAsignacionConError() {
        Asignacion asignacion = Asignacion.builder()
                .idEstudiante(3L)
                .idExamen(2L)
                .calificacion("0")
                .realizado(false)
                .build();
        doThrow(new RuntimeException("Error simulado")).when(asignacionRepositoryMock).save(asignacion);

        ResponseEntity<String> response = asignacionService.crearAsignacion(asignacion);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al asignar el examen.", response.getBody());

        verify(asignacionRepositoryMock, times(1)).save(asignacion);
    }
}
