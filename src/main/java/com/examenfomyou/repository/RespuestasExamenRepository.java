package com.examenfomyou.repository;

import com.examenfomyou.model.RespuestasExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestasExamenRepository extends JpaRepository<RespuestasExamen, Long> {
}
