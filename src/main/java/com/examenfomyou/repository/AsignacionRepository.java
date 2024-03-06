package com.examenfomyou.repository;

import com.examenfomyou.model.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    @Query(value = "SELECT * FROM Asignacion WHERE id_estudiante = :idEstudiante", nativeQuery = true)
    Asignacion findByEstudianteId(@Param("idEstudiante") Long idEstudiante);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Asignacion SET calificacion = :calificacionTotal WHERE id_examen = :idExamen", nativeQuery = true)
    void actualizarCalificacionExamen(@Param("calificacionTotal") int calificacionTotal, @Param("idExamen") Long idExamen);
}
