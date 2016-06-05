package com.tonyjs.solitaire;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int CARDWIDTH = CardWithImage.getCardWidth();
	private static final int CARDHEIGHT = CardWithImage.getCardHeight();
	private static final int SPACING = 4;
	private static final int MARGIN = 10;
	private static final int WIDTH = 8*CARDWIDTH + 12*SPACING + 2*MARGIN;
	private static final int HEIGHT = 5*CARDHEIGHT + 3*MARGIN;
	private static final int FOUNDATIONX = WIDTH/2;
	private static final int FOUNDATIONY = MARGIN;
	private static final int BOARDX = (int) (MARGIN*12.5); 
	private static final int BOARDY = CARDHEIGHT + MARGIN*2;
	private static final int OVERLAP = (int) (CARDHEIGHT * .20);
	private CardStack[] foundation = new CardStack[4];
	private CardStack[] column = new CardStack[7];
	private CardStack pile;
	private Deck deck;
	private CardWithImage card;
	
	public TablePanel() {
		int x = FOUNDATIONX;
		int y = FOUNDATIONY;
		for (int i = 0; i < 4; i++) {
			foundation[i] = new CardStack(x, y, 0);
			x += CARDWIDTH + SPACING;
		}
		
		x = BOARDX;
		y = BOARDY;
		for (int i = 0; i < 7; i++) {
			column[i] = new CardStack(x, y, OVERLAP);
			x += CARDWIDTH + SPACING;
		}
		
		x = 10; y = 10;
		pile = new CardStack(x, y, 0);

		deck = new Deck();
		deck.shuffle();
		deal();
		setPile();
	}
	
	public void deal() {
		for (int row = 0; row < 7; row++) {
			for (int col = 6; col >= row; col--) {
				if (col > row) {
					card = deck.deal(false);
				} else {
					card = deck.deal(true);
				}
				column[col].add(card);
			}
		}
		repaint();
	}
	
	public void setPile() {
		int i = 0;
		while (i++ < deck.cardsLeft()) {
			card = deck.deal(false);
			pile.add(card);
		}
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(25, 177, 104));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for (int i = 0; i < 4; i++) {
			if (foundation[i].size() > 0) {
				foundation[i].draw(g);
			} else {
				int x = foundation[i].getX();
				int y = foundation[i].getY();
				CardWithImage.drawOutline(g, x, y);
			}
		}
		
		for (int i = 0; i < 7; i++) {
			column[i].draw(g);
		}

		pile.draw(g);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
}
