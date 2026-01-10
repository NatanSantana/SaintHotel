package com.example.Saint.Repository;

import com.example.Saint.DTO.ReservaVencidaDTO;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuartosOcupadosRepository extends JpaRepository<QuartosOcupados, Long> {

    List<QuartosOcupados> findAllByIdQuarto(Long idQuarto);

    List<QuartosOcupados> findAllByIdUsuario(Long idUsuario);

    Optional<QuartosOcupados> findByDiaReservado(LocalDateTime diaReservado);


    @Transactional
    @Modifying
    @Query("DELETE FROM QuartosOcupados WHERE diaReservado = :dia AND idUsuario = :idUsuario ")
    int deleteReserva(@Param("dia")LocalDateTime dia, @Param("idUsuario") Long id);


    @Query("SELECT q FROM QuartosOcupados q WHERE q.checkOut < :diaAtual")
    List<QuartosOcupados> reservasVencidas(@Param("diaAtual") LocalDateTime diaAtual);

    @Query("SELECT q FROM QuartosOcupados q WHERE q.expirou = true AND idUsuario = :id")
    List<QuartosOcupados> reservasVencidasPorUsuario(@Param("id") Long idUsuario);

    @Transactional
    @Modifying
    @Query("UPDATE QuartosOcupados q SET q.expirou = true WHERE q.checkOut < :diaAtual")
    int marcarReservasComoExpirada(@Param("diaAtual") LocalDateTime diaAtual);


}
