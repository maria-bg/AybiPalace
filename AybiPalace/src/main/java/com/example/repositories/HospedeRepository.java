package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Hospede;

public interface HospedeRepository extends JpaRepository<Hospede, String> {
}
