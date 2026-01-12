package com.example.Saint.Repository;

import com.example.Saint.Entity.Usuarios;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByCpf(String cpf);

    Optional<Usuarios> findByNome(String nome);

    @Transactional
    int deleteByNome(String nome);
}
