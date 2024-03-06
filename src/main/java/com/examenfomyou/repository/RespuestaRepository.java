package com.examenfomyou.repository;

import com.examenfomyou.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    @Query(value = "SELECT id_respuesta, opcion, opc_respuesta, id_pregunta FROM Respuesta WHERE id_pregunta IN (:idsPreguntas)", nativeQuery = true)
    List<Object[]> findRespuestasByPreguntaIds(@Param("idsPreguntas") List<Object> idsPreguntas);

    @Query(value = "SELECT * FROM Respuesta WHERE id_pregunta IN (:listaIdsPreguntas) AND es_correcta = :esCorrecta", nativeQuery = true)
    List<Respuesta> encontarRespuestasPorIdPreguntaYEsCorrecta(@Param("listaIdsPreguntas") List<Long> listaIdsPreguntas, @Param("esCorrecta") boolean esCorrecta);
}
