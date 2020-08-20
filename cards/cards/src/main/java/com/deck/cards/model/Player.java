package com.deck.cards.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
@Entity
@ApiModel(description = "Players in the Game")
public class Player {

	@Getter
	@Setter
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;
	
	@Getter
	@Setter
	@Column(name = "Name")
	@ApiModelProperty(notes = "Name of the Player", required = true)
	@NotNull(message = "Name cannot be null")
	private String name;
	
	@Setter
	@ApiModelProperty(notes = "total value of the cards a player has")
	@Transient
	/** The total value of cards in hand of player */
	private int totalValue;
	
	@Getter
	@Setter
	@ApiModelProperty(notes = "list of cards a players posseses in the game")
	@Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	/** The cards currently held by the player, in the order they were dealt. */
	private List<Card> cards;
	
	public Player() {}
	
	public Player(String name) {
		this.name = name;
		this.totalValue = 0;
		cards = new ArrayList<>();
	}
	
	/** calculate total value of cards by adding face value */
	public int getTotalValue() {
		return this.cards.stream().collect(Collectors.summingInt(eachCard -> eachCard.getValueOfCard().getValue()));
	}
}
