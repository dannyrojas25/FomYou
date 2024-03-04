package com.examenfomyou.service;

import com.examenfomyou.model.*;
import com.examenfomyou.repository.PreguntaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreguntaService {
    private final PreguntaRepository preguntaRepository;

    public PreguntaService(PreguntaRepository preguntaRepository) {
        this.preguntaRepository = preguntaRepository;
    }
    @Transactional
    public void guardarPreguntas(List<Pregunta> preguntas){
        for (Pregunta pregunta : preguntas){
            preguntaRepository.save(pregunta);
            for (Respuesta respuesta : pregunta.getRespuestas()){
                respuesta.setPregunta(pregunta);
            }
        }
    }
}
