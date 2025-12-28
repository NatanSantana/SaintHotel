package com.example.Saint.Repository;

import com.example.Saint.Entity.Quartos;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuartosRepository extends JpaRepository<Quartos, Long> {


    Quartos findByNomeQuarto(String nomeQuarto);
    Optional<Quartos> findByNumero(int numero);
}
