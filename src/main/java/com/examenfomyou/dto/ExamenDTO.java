package com.examenfomyou.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamenDTO {
    private String descripcion;
    private Date fecha;
}
