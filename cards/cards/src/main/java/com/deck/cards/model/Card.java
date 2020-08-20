package com.deck.cards.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@ApiModel(description = "Cards in the Game")
public class Card{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
    private Long cardId;
	
	@ApiModelProperty
	private Suit suit;
	
	@ApiModelProperty
	private FaceValue valueOfCard;
	
	public Card() {
		
	}
	
	public Card(Suit suit, FaceValue valueOfCard){
		this.suit = suit;
		this.valueOfCard = valueOfCard;
	}
	
	public boolean equals(Card card) {
		return (this.suit.equals(card.getSuit()) && this.getValueOfCard().equals(card.getValueOfCard()));
	}

	

}
