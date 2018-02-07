package com.poradnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poradnia.model.User;
import com.poradnia.model.Visit;

@Repository
public interface VisitRepository  extends JpaRepository<Visit, Long> {
	Visit findByStartDate(String startDate);
}
