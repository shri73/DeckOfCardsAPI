package com.deck.cards.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deck.cards.dto.PlayerWithValue;
import com.deck.cards.exception.BadRequestException;
import com.deck.cards.exception.GameNotFoundException;
import com.deck.cards.exception.NoMoreCardsException;
import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;
import com.deck.cards.model.Card;
import com.deck.cards.model.DeckOfCards;
import com.deck.cards.model.Game;
import com.deck.cards.model.Player;
import com.deck.cards.model.Suit;
import com.deck.cards.repository.GameRepository;
import com.deck.cards.repository.PlayerRepository;
import com.deck.cards.util.Constants;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class GameService {
	
	private GameRepository repository;
	
	private PlayerRepository playerRepository;
	
	private ModelMapper mapper;
	
	public GameService(GameRepository repository, PlayerRepository playerRepository, ModelMapper mapper ) {
		this.mapper = mapper;
		this.repository = repository;
		this.playerRepository = playerRepository;
	}
	
	private Optional<Game> getGameById(Long gameId) {
		return repository.findById(gameId);
	}
	
	public Long createGame() {
		log.info("Game is being created :");
		Game game = new Game();
		repository.save(game);
		return game.getGameId();
	}
	
	public void deleteGame(Long gameId) {
		log.info("Game is being deleted by Id :" + gameId);
		repository.deleteById(gameId);
	}
	
	public void addDeckToGame(Long gameId) {
		Optional<Game> gameToAddDeck = getGameById(gameId);
		if(gameToAddDeck.isPresent()) {
			log.info("Deck is being added to game :" + gameId);
			Game game = gameToAddDeck.get();
			DeckOfCards cards = createDeck();
			game.addDeck(cards);
			repository.save(game);
		}
		else {
			throw new GameNotFoundException(gameId);
		}
		
	}
	
	public DeckOfCards createDeck() {
		return new DeckOfCards();
	}
	
	
	/** Provides a mechanism for randomly shuffling elements of a list by using random numbers from 1-52 and swapping them. */
    public void shuffle(Long gameId) {
    	Optional<Game> gameToShuffle = getGameById(gameId);
    	if(gameToShuffle.isPresent()) {
    		Game game = gameToShuffle.get();
    		//pre shuffle cards maintain all the cards that are already dealt
    		List<Card> preShuffleCards = game.getCurrentListOfCards().subList(0, game.getCurrentCard());
    		
    		//toShuffleCards contain all the cards that can be dealt to the user, and needs to be shuffled
    		List<Card> toShuffleCards = game.getCurrentListOfCards().subList(game.getCurrentCard(), game.getCurrentListOfCards().size());
    		int multiplier = Math.multiplyExact(toShuffleCards.size(), game.getListOfDecks().size());
    		
    		
    		for(int eachCard = 0; eachCard < toShuffleCards.size(); eachCard++) {
    			
    			int randomCard = multiplier * (int) Math.random();
    			int nextRandomCard = (int) (toShuffleCards.size() * Math.random());
    			
    			//swapping cards to stimulate shuffling
    			Card tempCard = toShuffleCards.get(randomCard);
    			toShuffleCards.set(randomCard, toShuffleCards.get(nextRandomCard));
    			toShuffleCards.set(nextRandomCard, tempCard);
    			
    		}
    		//after shuffling cards all cards are combined to form the list of current cards
    		preShuffleCards.addAll(toShuffleCards);
    		game.setCurrentListOfCards(preShuffleCards);
			repository.save(game);
    		
    		log.info("Cards are shuffled for the game : " + gameId);
    		
    	}
    	else {
			throw new GameNotFoundException(gameId);
		}
	}
    
    public List<PlayerWithValue> getPlayersSortedForGame(Long gameId){
    	Optional<Game> game = getGameById(gameId);
    	if(game.isPresent()) {
    		Game currentGame = game.get();
    		return currentGame.getPlayers().stream().sorted(Comparator.comparing(Player::getTotalValue).reversed()).map(this::convertToDto).collect(Collectors.toList());
    	}
    	else {
			throw new GameNotFoundException(gameId);
		}
		
	}
    
    private PlayerWithValue convertToDto(Player player) {
    	PlayerWithValue dto = mapper.map(player, PlayerWithValue.class);
        return dto;
    }
    
    public Card deal(Long gameId, Long playerId) {
    	Optional<Game> gameToDeal = getGameById(gameId);
    	Optional<Player> playerToDeal = playerRepository.findById(playerId);
    	if(gameToDeal.isPresent() && playerToDeal.isPresent()) {
    		Game game = gameToDeal.get();
    		Player player = playerToDeal.get();
    		Card dealtCard = null;
    		//the current card is the index of the top of the card on undealt cards
    		int currentCard = game.getCurrentCard();
    		List<Card> currentListOfCards = game.getCurrentListOfCards();
    		//multiplier is the total number of cards that is compromised on card in each deck(52) times the number of decks 
    		int multiplier = Math.multiplyExact(Constants.NUMBER_OF_CARDS, game.getListOfDecks().size());
    		if ( currentCard < multiplier )
   	   	 	{
    			dealtCard = currentListOfCards.get(currentCard);
    			player.getCards().add(dealtCard);
    			game.setCurrentCard(currentCard + 1);
    			repository.save(game);
    			playerRepository.save(player);
    			log.info("Card is dealt for player : " + playerId);
   	   	 	}
	   	   	 else
	   	   	 {
	   	   	    log.info("No more cards left");
	   	   	    throw new NoMoreCardsException("No more cards left");
	   	   	 }
    		return dealtCard;
    	
    	}
    	else {
    		throw gameToDeal.isPresent() ? new BadRequestException("Player not found : " + playerId) : new GameNotFoundException(gameId);
		}
		
		 
	}
    
    public Map<Suit, Long> getUnDealtCardsBySuit(Long gameId) {
    	Optional<Game> game = getGameById(gameId);
    	if(game.isPresent()) {
    		Game currentGame = game.get();
    		Map<Suit, Long> cardsPerSuitMap = currentGame.getCurrentListOfCards().subList(currentGame.getCurrentCard(), 
    				currentGame.getCurrentListOfCards().size()).stream().
    				collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));
    		List<Suit> suits = new ArrayList<Suit>(Arrays.asList(Suit.values()));
    		for(Suit eachSuit: suits) {
    			if(!cardsPerSuitMap.containsKey(eachSuit)) {
    				cardsPerSuitMap.put(eachSuit, (long) 0);
    			}
    		}
    		
    		return cardsPerSuitMap;
    	}
    	else {
			throw new GameNotFoundException(gameId);
		}
		
	}
	
	public List<Card> getSortedUndealtCards(Long gameId){
		Optional<Game> game = getGameById(gameId);
    	if(game.isPresent()) {
    		Game currentGame = game.get();
    		return currentGame.getCurrentListOfCards().subList(currentGame.getCurrentCard(), 
    				currentGame.getCurrentListOfCards().size()).stream().
    				sorted(Comparator.comparing(Card::getSuit).
    				thenComparing(reverseOrder(comparing(Card::getValueOfCard))))
    				.collect(Collectors.toList());	
    	}
    	else {
			throw new GameNotFoundException(gameId);
		}
		
		
	}

}
