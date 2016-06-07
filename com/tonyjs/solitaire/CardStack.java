package com.tonyjs.solitaire;

import java.awt.Graphics;
import java.util.ArrayList;

public class CardStack {
	ArrayList<CardWithImage> cards = new ArrayList<CardWithImage>();
	int stackWidth = 0;
	int stackHeight = 0;
	int overlap = 0;
	
	public CardStack(int stackWidth, int stackHeight, int overlap) {
		this.stackWidth = stackWidth;
		this.stackHeight = stackHeight;
		this.overlap = overlap;
	}
	
	public void add(CardWithImage card) {
		int cardx = stackWidth;
		int cardy = stackHeight + overlap * cards.size();
		card.setXY(cardx, cardy);
		cards.add(card);
	}
	
	public void add(int index, CardWithImage card) {
		int cardx = stackWidth;
		int cardy = stackHeight + overlap * cards.size();
		card.setXY(cardx, cardy);
		cards.add(index, card);
	}
	
	public void draw(Graphics g) {
		if (cards.size() > 0 && overlap == 0) {
			int lastIndex = cards.size() - 1;
			CardWithImage card = cards.get(lastIndex);
			card.draw(g);
		} else {
			for (int i = 0; i < cards.size(); i++) {
				CardWithImage card = cards.get(i);
				card.draw(g);
			}
		}
	}

	public int size() {
		return cards.size();
	}
	
	public int getX() {
		return stackWidth;
	}
	
	public int getY() {
		return stackHeight;
	}
	
	public void setXY(int x, int y) {
		this.stackWidth = x;
		this.stackHeight = y;
	}
	
	public void clear() {
		cards.clear();
	}
	
	public CardWithImage getLast() {
		return cards.get(cards.size() - 1);
	}
	
	public CardWithImage getCard(int i) {
		return cards.get(i);
	}
	
	public void remove(int i) {
		cards.remove(i);
	}
	
	public void removeLast() {
		cards.remove(cards.size() - 1);
	}
}
