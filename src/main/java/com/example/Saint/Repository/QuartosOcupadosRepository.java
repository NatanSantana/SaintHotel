package com.example.Saint.Repository;

import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuartosOcupadosRepository extends JpaRepository<QuartosOcupados, Long> {

    List<QuartosOcupados> findAllByIdQuarto(Long idQuarto);

    List<QuartosOcupados> findAllByIdUsuario(Long idUsuario);


    @Transactional
    @Modifying
    @Query("DELETE FROM QuartosOcupados WHERE diaReservado = :dia AND idUsuario = :idUsuario ")
    int deleteReserva(@Param("dia")LocalDateTime dia, @Param("idUsuario") Long id);

}
