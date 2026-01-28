package com.example.Saint.Controller;

import com.example.Saint.DTO.RedeSaintHotelsDTO;
import com.example.Saint.Entity.RedeSaintHotels;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.Service.RedeSaintHotelsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
@AllArgsConstructor
public class HotelController {

    private final RedeSaintHotelsService redeSaintHotelsService;

    private final RedeSaintHotelsRepository redeSaintHotelsRepository;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarHotel(@RequestBody @Valid RedeSaintHotelsDTO dto) {
        redeSaintHotelsService.registrarHotel(dto);
        return ResponseEntity.ok().body("Hotel Registrado Com Sucesso");

    }

    @DeleteMapping("/deletar")
    public ResponseEntity<?> excluirRegistroHotel(@RequestParam Long idHotel) {
        RedeSaintHotels redeSaintHotels = redeSaintHotelsRepository.findById(idHotel).orElseThrow(() ->
                new RuntimeException("Hotel não encontrado"));

        redeSaintHotelsService.excluirHotel(idHotel);
        return ResponseEntity.ok().body("Hotel Deletado Com Sucesso, unidade deletada: "+redeSaintHotels.getBairro());
    }




}
