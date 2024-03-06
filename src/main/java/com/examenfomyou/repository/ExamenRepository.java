package com.examenfomyou.repository;

import com.examenfomyou.model.Examen;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE Asignacion SET json_examenlisto = :examenPresentar WHERE id_examen = :idExamen", nativeQuery = true)
    void guardarExamen(@Param("examenPresentar") String examenPresentar, @Param("idExamen") Long idExamen);

}

