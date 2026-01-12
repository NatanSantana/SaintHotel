package com.example.Saint.Repository;

import com.example.Saint.Entity.RedeSaintHotels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedeSaintHotelsRepository extends JpaRepository<RedeSaintHotels, Long> {


    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
    int deleteByIdHotel(Long idHotel);





}
