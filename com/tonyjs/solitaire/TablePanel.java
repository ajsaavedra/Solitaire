package com.tonyjs.solitaire;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

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
	private CardStack pileFaceDown, pileFaceUp, movingCards;
	private Deck deck;
	private CardWithImage card;
	private int mouseX = 0;
	private int mouseY = 0;
	private int fromCol = 0;

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
		pileFaceDown = new CardStack(x, y, 0);
		pileFaceUp = new CardStack(x*2 + CARDWIDTH, y, 0);
		movingCards = new CardStack(x, y, OVERLAP);

		deck = new Deck();
		deck.shuffle();
		deal();
		setPile();

		// Mouse listeners
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				movingCards.setXY(x, y);
				clicked(x, y);
			}

			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				released(x, y);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				dragged(x, y);
			}
		});

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
		while (i++ < 24) {
			card = deck.deal(false);
			pileFaceDown.add(card);
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
		pileFaceDown.draw(g);

		movingCards.draw(g);

	}

	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	private void clicked(int x, int y) {
		movingCards.clear();
		for (int col = 0; col < 7 && movingCards.size() == 0; col++) {
			if (column[col].size() > 0) {
				int iter = 0;
				while (iter < column[col].size()) {
					card = column[col].getCard(iter);
					if (card.isFaceUp()) {
						if (card.contains(x, y)) {
							movingCards.add(card);
							mouseX = x;
							mouseY = y;
							y += OVERLAP;
							fromCol = col;
						}
					}
					iter++;
				}
				iter = column[col].size() - 1;
				while (iter >= 0) {
					card = column[col].getCard(iter);
					if (card.isFaceUp()) {
						if (card.contains(x, y)) {
							column[col].remove(iter);
						}
					}
					iter--;
				}
			}
		}
	}

	private void dragged(int x, int y) {
		if (movingCards.size() > 0) {
			int changeX = x - mouseX;
			int changeY = y - mouseY;
			for (int i = 0; i < movingCards.size(); i++) {
				movingCards.getCard(i).addToXY(changeX, changeY);
			}
			mouseX = x;
			mouseY = y;
			repaint();
		}
	}

	private void released(int x, int y) {
		if (movingCards.size() > 0) {
			boolean validMove = false;
			for (int i = 0; i < 4 && !validMove; i++) {
				int foundationx = foundation[i].getX();
				int foundationy = foundation[i].getY();
				CardWithImage cardMoving = movingCards.getCard(0);
				if (!cardMoving.isNear(foundationx, foundationy)) { // FIX
					if (foundation[i].size() == 0) {
						if (cardMoving.getRank() == "Ace") {
							validMove = true;
							foundation[i].add(cardMoving);
							movingCards.clear();
							if (column[fromCol].size() > 0) {
								column[fromCol].getLast().setFaceUp(true);
							}
						}
					} else {
						CardWithImage topCard = foundation[i].getLast();
						if (cardMoving.getSuit() == topCard.getSuit()
								&& cardMoving.getValue() == topCard.getValue() + 1) {
							validMove = true;
							foundation[i].add(cardMoving);
							movingCards.clear();
							if (column[fromCol].size() > 0) {
								column[fromCol].getLast().setFaceUp(true);
							}
						}
					}
				}
			}
			for (int l = 0; l < 7 && !validMove; l++) {
				CardWithImage cardMoving = movingCards.getCard(0);
				if (column[l].size() > 0) { // can only place opposite color and value+1 on top
					Card card = column[l].getLast();
					if (cardMoving.getValue() == card.getValue() - 1) {
						if (Deck.canBeStacked(card.getSuit(), cardMoving.getSuit())) {
							validMove = true;
							for (int i = 0; i < movingCards.size(); i++) {
								column[l].add(movingCards.getCard(i));
							}
							movingCards.clear();
							if (column[fromCol].size() > 0) {
								column[fromCol].getLast().setFaceUp(true);
							}
						}
					}
				} else { // can only place King on empty stack
					if (cardMoving.getRank() == "King") {
						validMove = true;
						for (int i = 0; i < movingCards.size(); i++) {
							column[l].add(movingCards.getCard(i));
						}
						movingCards.clear();
						if (column[fromCol].size() > 0) {
							column[fromCol].getLast().setFaceUp(true);
						}
					}
				}
			}

			if (!validMove) {
				for (int k = 0; k< movingCards.size(); k++) {
					column[fromCol].add(movingCards.getCard(k));
				}
				movingCards.clear();
			}
			repaint();
		}

	}
}
