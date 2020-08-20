package com.deck.cards.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.deck.cards.exception.GameNotFoundException;
import com.deck.cards.model.Game;
import com.deck.cards.model.Player;
import com.deck.cards.repository.GameRepository;
import com.deck.cards.repository.PlayerRepository;

@TestInstance(Lifecycle.PER_CLASS)
public class PlayerServiceTests {
	
	@Mock
    private PlayerRepository repository;
	
	@Mock
	private GameRepository gameRepository;
	
	@InjectMocks
	private PlayerService service;
	
	private Game game;
	
	@BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        game = new Game();	
        Mockito.when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        Mockito.when(gameRepository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(gameRepository.save(Mockito.any(Game.class))).then(AdditionalAnswers.returnsFirstArg());
        
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(new Player("test")));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(repository.save(Mockito.any(Player.class))).then(AdditionalAnswers.returnsFirstArg());
    }
	
	@Test
    public void addPlayerToGameWhenThereIsNoGame() {
		Exception exception = assertThrows(
				GameNotFoundException.class,
				() -> service.addPlayer("", 3L));

	        assertTrue(exception.getMessage().contains("Cannot find a game with ID"));
    }
	
	@Test
    public void addPlayerToGame() {
    	service.addPlayer("", 1L);
        assertTrue(game.getPlayers().size() > 0);

    }
	
	@Test
    public void removePlayerFromGame() {
    	service.removePlayerFromGame(1L, 1L);
        assertTrue(game.getPlayers().size() == 0);
    }
	

}
