package com.deck.cards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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
import com.deck.cards.exception.NoMoreCardsException;
import com.deck.cards.model.Card;
import com.deck.cards.model.DeckOfCards;
import com.deck.cards.model.Game;
import com.deck.cards.model.Player;
import com.deck.cards.repository.GameRepository;
import com.deck.cards.repository.PlayerRepository;
import com.deck.cards.util.Constants;
@TestInstance(Lifecycle.PER_CLASS)
public class GameServiceTests {
	
	@Mock
    private GameRepository repository;
	
	@Mock
	private PlayerRepository playerRepository;
	
	@InjectMocks
	private GameService service;
	
	private Game game;
	
	@BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        game = new Game();
        game.addDeck(new DeckOfCards());

        assertEquals(game.getCurrentListOfCards().size(), Constants.NUMBER_OF_CARDS); 	
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(repository.save(Mockito.any(Game.class))).then(AdditionalAnswers.returnsFirstArg());
        
        Mockito.when(playerRepository.findById(1L)).thenReturn(Optional.of(new Player("test")));
        Mockito.when(playerRepository.findById(2L)).thenReturn(Optional.empty());
    }
	
	@Test
    public void addDeckToGameWhenThereIsNoGame() {
		Exception exception = assertThrows(
				GameNotFoundException.class,
				() -> service.addDeckToGame(2L));

	        assertTrue(exception.getMessage().contains("Cannot find a game with ID"));
    }
	
	 
	 @Test
	 public void createDeck() {
		    DeckOfCards storeObject = service.createDeck();
	        assertTrue(storeObject != null);
	  }
	 
	 @Test
	 public void getUndealtCardsBySuit_whenNoGame() {
		 Exception exception = assertThrows(
					GameNotFoundException.class,
					() -> service.getUnDealtCardsBySuit(2L));

		        assertTrue(exception.getMessage().contains("Cannot find a game with ID"));
	  }
	 
	 @Test
	 public void getUndealtCardsBySuit_test() {
		 Exception exception = assertThrows(
					GameNotFoundException.class,
					() -> service.getUnDealtCardsBySuit(2L));

		        assertTrue(exception.getMessage().contains("Cannot find a game with ID"));
	  }
	 
	 @Test
	 public void testDeal_whenNoCardsInDeck() {	
		 Exception exception = assertThrows(
					NoMoreCardsException.class,
					() -> service.deal(1L, 1L));

		        assertTrue(exception.getMessage().contains("No more cards left"));
			Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyLong());
			Mockito.verifyNoMoreInteractions(repository);
			
		}
	 
	 @Test
	 public void testShuffle() {
		 DeckOfCards deck1 = new DeckOfCards();
		 service.shuffle(1L);
		 List<Card> list = game.getCurrentListOfCards();
		 List<Card> deckCards = deck1.getCards();
		 int index = 0;
		 while(list.size() > index) {
			 assertNotEquals(list.get(index), deckCards.get(index));
			 index++;
		}
		 
	 }
	 
	 @Test
	 public void testDeal_52times() {
		 Card card = null;
			int count = 0;
			while (count < Constants.NUMBER_OF_CARDS) {
				card = service.deal(1L, 1L);
				if (card != null) {
					++count;
			  }
			}
				
			assertTrue(count == Constants.NUMBER_OF_CARDS);
			Mockito.verify(repository, Mockito.times(52)).findById(Mockito.anyLong());
			
		}
	 
	 @Test
	 public void testDeal_with2Decks() {
		 game.addDeck(new DeckOfCards());
		 Card card = null;
			int count = 0;
			while (count < Constants.NUMBER_OF_CARDS * 2) {
				card = service.deal(1L, 1L);
				if (card != null) {
					++count;
			  }
			}
				
			assertTrue(count == Constants.NUMBER_OF_CARDS * 2);
			Mockito.verify(repository, Mockito.times(104)).findById(Mockito.anyLong());
		}
	 
	 @Test
	 public void testDeal_NoMoreCards() {
		 try {
			 Card card = null;
				int count = 0;
				while (count < Constants.NUMBER_OF_CARDS * 105) {
					card = service.deal(1L, 1L);
					if (card != null) {
						++count;
				  }
				}
				Assertions.fail("NoMoreCardsException not thrown");
		 }
		 catch(NoMoreCardsException ex) {
			 
		 }
			
		}
	 
	
	
	
	

}
