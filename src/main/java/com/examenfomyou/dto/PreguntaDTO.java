package com.examenfomyou.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaDTO {
    private String enunciado;
    private List<String> opciones;
    private int opcionCorrecta;
    private int puntaje;
}
