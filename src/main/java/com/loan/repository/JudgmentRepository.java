package com.loan.repository;

import com.loan.domain.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgmentRepository extends JpaRepository<Judgment,Long> {


}
