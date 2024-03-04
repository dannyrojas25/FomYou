package com.examenfomyou.repository;

import com.examenfomyou.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    @Query(value = "SELECT * FROM Asignacion WHERE id_estudiante = :idEstudiante", nativeQuery = true)
    Asignacion findByEstudianteId(@Param("idEstudiante") Long idEstudiante);
}
