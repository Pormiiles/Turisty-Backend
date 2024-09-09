package br.ifba.turisty.domain.lugar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifba.turisty.domain.lugar.model.Lugar;

@Repository
public interface LugarRepository extends JpaRepository<Lugar, Long> {
}
