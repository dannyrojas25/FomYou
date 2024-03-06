package com.examenfomyou.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class RespuestasExamen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExamenResuelto;

    private Long idEstudiante;

    private Long idPregunta;

    private Long idRespuesta;

    private String opcion;

    private String opcMarcada;

    private int calificacion;

    private String respuestaExamen;
}
