package br.ifba.turisty.web.controllers.lugar;

import br.ifba.turisty.domain.lugar.model.Lugar;
import br.ifba.turisty.domain.lugar.service.LugarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turisty/lugares")
public class LugarController {

    @Autowired
    private LugarService lugarService;

    @GetMapping
    public List<Lugar> getAllLugares() {
        return lugarService.findAll();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Lugar> getLugarById(@PathVariable Long id) {
        return lugarService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public Lugar createLugar(@RequestBody Lugar lugar) {
        return lugarService.save(lugar);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Lugar> updateLugar(@PathVariable Long id, @RequestBody Lugar lugarDetails) {
        return lugarService.findById(id)
                .map(lugar -> {
                    lugar.setNome(lugarDetails.getNome());
                    lugar.setImagem(lugarDetails.getImagem());
                    lugar.setLocalizacao(lugarDetails.getLocalizacao());
                    lugar.setLatitude(lugarDetails.getLatitude());
                    lugar.setLongitude(lugarDetails.getLongitude());
                    Lugar updatedLugar = lugarService.save(lugar);
                    return ResponseEntity.ok(updatedLugar);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLugar(@PathVariable Long id) {
        return lugarService.findById(id)
                .map(lugar -> {
                    lugarService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
