package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Faxineiro;

public interface FaxineiroRepository extends JpaRepository<Faxineiro, String> {
    // JpaRepository já fornece métodos para operações CRUD
}
