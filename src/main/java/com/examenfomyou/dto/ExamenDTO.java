package com.examenfomyou.dto;

import com.examenfomyou.model.Pregunta;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamenDTO {
    private String descripcion;

    private Date fecha;
}
