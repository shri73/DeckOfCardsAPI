package com.deck.cards.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.deck.cards.exception.BadRequestException;
import com.deck.cards.exception.GameNotFoundException;
import com.deck.cards.model.Card;
import com.deck.cards.model.Game;
import com.deck.cards.model.Player;
import com.deck.cards.repository.GameRepository;
import com.deck.cards.repository.PlayerRepository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlayerService {
	
	private PlayerRepository repository;
	private GameRepository gameRepository;
	
	PlayerService(PlayerRepository repository, GameRepository gameRepository ){
		this.repository = repository;
		this.gameRepository = gameRepository;
	}
	
	public Long addPlayer(@NonNull String name, Long gameId) {
		log.info("adding a player :" + name + "to game : " + gameId);
		Optional<Game> gameToAddPlayer = gameRepository.findById(gameId);
		
		if(gameToAddPlayer.isPresent()) {
			Game game = gameToAddPlayer.get();
			Player toAddPlayer = new Player(name);
			repository.save(toAddPlayer);
			game.getPlayers().add(toAddPlayer);
			gameRepository.save(game);
			return toAddPlayer.getPlayerId();
		}
		else {
			throw new GameNotFoundException( gameId);
		}
   
    }
	

    public void removePlayerFromGame(Long playerId, Long gameId) {
    	Optional<Game> gameToRemovePlayer = gameRepository.findById(gameId);
    	Optional<Player> playerToRemove = repository.findById(playerId);
    	if(gameToRemovePlayer.isPresent() && playerToRemove.isPresent()) {
    		Game game = gameToRemovePlayer.get();
			Iterator<Player> it = game.getPlayers().iterator();
			while (it.hasNext()) {
				 Player each = it.next();
				    if (each.getPlayerId() == playerId){
				    	it.remove();
				    } 
					       
			}
			
			gameRepository.save(game);
    		
    	}
    	else {
    		throw gameToRemovePlayer.isPresent() ? new BadRequestException("Player not found : " + playerId) : new GameNotFoundException(gameId);
		}
       
        
    }
	
	private Player getPlayerForGame(Long playerId, Long gameId) {
		Optional<Game> gameOptional = gameRepository.findById(gameId);
		Optional<Player> playerOptional = repository.findById(playerId);
		
		if(gameOptional.isPresent() && playerOptional.isPresent()) {
			return playerOptional.get();
		}
		else {
			throw gameOptional.isPresent() ? new BadRequestException("Player not found : " + playerId) : new GameNotFoundException(gameId);
		}
		
	}
	
	public List<Card> getPlayerCardsForGame(Long playerId, Long gameId){
		Player player = getPlayerForGame(playerId, gameId);
		return player.getCards();
	}

}
