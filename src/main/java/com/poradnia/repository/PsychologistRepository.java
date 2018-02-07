package com.poradnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poradnia.model.Psychologist;

@Repository
public interface PsychologistRepository  extends JpaRepository<Psychologist, Long> {
}
