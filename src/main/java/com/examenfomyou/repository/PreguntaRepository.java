package com.examenfomyou.repository;

import com.examenfomyou.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    @Query(value = "SELECT id_pregunta, enunciado FROM Pregunta WHERE id_examen = :idExamen", nativeQuery = true)
    List<Object[]> findByExamenId(@Param("idExamen") Long idExamen);
}
