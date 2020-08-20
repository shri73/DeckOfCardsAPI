package com.deck.cards.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.deck.cards.model.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long>{

}
