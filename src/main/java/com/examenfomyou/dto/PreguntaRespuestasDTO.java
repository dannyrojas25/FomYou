package com.examenfomyou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreguntaRespuestasDTO {
    private Long idPregunta;
    private String enunciado;
    private List<RespuestaDTO> respuestas;
}
