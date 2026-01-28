package com.example.Saint.Repository;

import com.example.Saint.Entity.Quartos;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuartosRepository extends JpaRepository<Quartos, Long> {

    @Query("SELECT q FROM Quartos q WHERE idHotel = :idHotel AND nomeQuarto = :nomeQuarto")
    Quartos findByNomeQuarto(@Param("nomeQuarto") String nomeQuarto, @Param("idHotel") Long idHotel);

    @Query("SELECT q FROM Quartos q")
    List<Quartos> findAllQuartos();

    @Modifying
    @Transactional
    @Query("UPDATE Quartos q SET q.nomeQuarto = :nomeQuarto WHERE q.idQuarto = :idQuarto")
    void atualizarNomeQuarto(@Param("idQuarto") Long idQuarto, @Param("nomeQuarto") String nomeQuarto);

    @Modifying
    @Transactional
    @Query("UPDATE Quartos q SET q.valorDoQuarto = :valor WHERE q.idQuarto = :idQuarto")
    void atualizarValor(@Param("idQuarto") Long idQuarto, @Param("valor") BigDecimal valor);

    @Query("SELECT q FROM Quartos q WHERE idHotel = :idHotel")
    List<Quartos> findAllQuartosByIdHotel(@Param("idHotel") Long idHotel);

    @Query("SELECT q FROM Quartos q WHERE idHotel = :idHotel AND numero = :numero")
    Optional<Quartos> findByNumero(@Param("numero")int numero, @Param("idHotel") Long idHotel);

    @Transactional
    @Modifying
    int deleteByNomeQuarto(String nomeQuarto);


}
