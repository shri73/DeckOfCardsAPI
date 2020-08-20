package com.deck.cards.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.deck.cards.model.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

}
