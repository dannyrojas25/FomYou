package com.examenfomyou.controller;

import com.examenfomyou.model.Pregunta;
import com.examenfomyou.service.PreguntaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
public class PreguntaController {
    private final PreguntaService preguntaService;

    public PreguntaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearPreguntas(@RequestBody List<Pregunta> preguntas){
        preguntaService.guardarPreguntas(preguntas);
        return ResponseEntity.ok("Preguntas y respuestas guardadas exitosamente.");
    }
}
