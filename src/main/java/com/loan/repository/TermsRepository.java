package com.loan.repository;

import com.loan.domain.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms,Long> {

}
