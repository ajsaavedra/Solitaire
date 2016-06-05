package com.tonyjs.solitaire;

public class Card {
	private String cardRank;
	private String cardSuit;
	private int cardValue;
	private boolean isFaceUp;

	public Card(String theRank, String theSuit, int faceUpValue) {
		this.cardRank= theRank;
		this.cardSuit= theSuit;
		this.cardValue = faceUpValue;
		this.isFaceUp = false;
	}

	public Card(String theRank, String theSuit) {
		this.cardRank= theRank;
		this.cardSuit= theSuit;
		this.cardValue = 0;
		this.isFaceUp = false;
	}

	public String getSuit() {
		return cardSuit;
	}

	public String getRank() {
		return cardRank;
	}

	public int getValue() {
		return cardValue;
	}

	public boolean isFaceUp() {
		return isFaceUp;
	}

	public void setValue(int theValue) {
		cardValue = theValue;
	}

	public void setFaceUp(boolean faceUpValue) {
		isFaceUp = faceUpValue;
	}
}
