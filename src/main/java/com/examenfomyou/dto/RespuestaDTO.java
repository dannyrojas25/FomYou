package com.examenfomyou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaDTO {
    private Long idRespuesta;
    private String opcion;
    private String respuesta;
}
