package com.examenfomyou.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Asignacion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idEstudiante;
    private Long idExamen;
    private boolean realizado;
    @Column(name = "json_examenlisto", columnDefinition = "TEXT")
    private String examenListo;
    private String calificacion;
}
