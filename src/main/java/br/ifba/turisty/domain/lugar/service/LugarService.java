package br.ifba.turisty.domain.lugar.service;

import br.ifba.turisty.domain.lugar.model.Lugar;
import br.ifba.turisty.domain.lugar.repository.LugarRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LugarService {

    @Autowired
    private LugarRepository lugarRepository;

    public List<Lugar> findAll() {
        return lugarRepository.findAll();
    }

    public Optional<Lugar> findById(Long id) {
        return lugarRepository.findById(id);
    }

    public Lugar save(Lugar lugar) {
        return lugarRepository.save(lugar);
    }

    public void deleteById(Long id) {
        lugarRepository.deleteById(id);
    }
}
