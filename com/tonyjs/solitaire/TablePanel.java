package com.tonyjs.solitaire;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JOptionPane;
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
	private CardStack pile, tempPile, movingCards;
	private Deck deck, saveDeck;
	private boolean fromPile = false;
	private boolean toPile = false;
	private boolean GAME_OVER = false;
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
		pile = new CardStack(x, y, 0);
		tempPile = new CardStack(0,0,0);
		movingCards = new CardStack(x, y, OVERLAP);

		newGame();

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

	public void dealCards() {
		// Clear all foundations and columns
		clearPanel();

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

	public void clearPanel() {
		for (int i = 0; i < 4; i++) {
			foundation[i].clear();
		}
		for (int i = 0; i < 7; i++) {
			column[i].clear();
		}
	}

	public void setPile() {
		while (deck.cardsLeft() > 0) {
			card = deck.deal(true);
			pile.add(card);
		}
		repaint();
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
						if (iter == column[col].size() - 1) {
							if (card.contains(x, y, movingCards.size(), true)) {
								movingCards.add(card);
								mouseX = x; mouseY = y;
								y += OVERLAP/2; fromCol = col;
							}
						} else {
							if (card.contains(x, y, movingCards.size(), false)) {
								movingCards.add(card);
								mouseX = x; mouseY = y;
								y += OVERLAP/2; fromCol = col;
							}
						}
					}
					iter++;
				}
				iter = column[col].size() - 1;
				int limit = movingCards.size();
				while (limit > 0) {
					card = column[col].getCard(iter);
					column[col].remove(iter);
					limit--;
					iter--;
				}
			}
		}

		if (pile.size() > 0) {
			card = pile.getLast();
			if (card.contains(x, y)) {
				movingCards.add(card);
				fromPile = true;
				mouseX = x; mouseY = y;
			}
		} else {
			if (x >= pile.getX() && x <= pile.getX() + CARDHEIGHT
					&& y >= pile.getY() && y <= pile.getY() + CARDWIDTH) {
				for (int i = tempPile.size() - 1; i >= 0; i--) {
					movingCards.add(tempPile.getCard(i));
				}
				toPile = true;
				tempPile.clear();
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
		if (toPile) {
			for (int i = 0; i < movingCards.size(); i++) {
				pile.add(movingCards.getCard(i));
			}
			toPile = false;
			movingCards.clear();
		}
		if (movingCards.size() > 0) {
			boolean validMove = false;
			if (movingCards.size() == 1) {
				for (int i = 0; i < 4 && !validMove; i++) {// Adding a card to the foundation
					int foundationx = foundation[i].getX();
					int foundationy = foundation[i].getY();
					CardWithImage cardMoving = movingCards.getCard(0);
					if (!cardMoving.isNear(foundationx, foundationy)) {
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
								isGameOver();
							}
						}
					}
				}
				for (int l = 0; l < 7 && !validMove; l++) {// Adding a card on a column
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
			} else if (movingCards.size() > 1) {
				CardWithImage topCardMoving = movingCards.getCard(0);
				for (int l = 0; l < 7 && !validMove; l++) {// Adding a card on a column
					if (column[l].size() > 0) { // can only place opposite color and value+1 on top
						Card card = column[l].getLast();
						if (topCardMoving.getValue() == card.getValue() - 1) {
							if (Deck.canBeStacked(card.getSuit(), topCardMoving.getSuit())) {
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
						if (topCardMoving.getRank() == "King") {
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
			}

			if (fromPile && !validMove) {
				tempPile.add(pile.getLast());
				pile.removeLast();
				fromPile = false;
			} else if (fromPile && validMove) {
				pile.removeLast();
				fromPile = false;
			} else if (!validMove) {
				for (int k = 0; k< movingCards.size(); k++) {
					column[fromCol].add(movingCards.getCard(k));
				}
			}
			movingCards.clear();
		}
		repaint();
	}

	private void isGameOver() {
		for (int i = 0; i < 4 && !GAME_OVER; i++) {
			if (foundation[i].size() == 13) {
				GAME_OVER = true;
			}
		}

		if (GAME_OVER) {
			String message = "You won! Play again?";
			int option = JOptionPane.showConfirmDialog(null, message);
			if (option == JOptionPane.YES_OPTION) {
				newGame();
			} else {
				System.exit(0);
			}
		}
	}

	public void newGame() {
		deck = new Deck();
		saveDeck = new Deck();
		deck.shuffle();
		saveDeck.copyFrom(deck);
		dealCards();
		setPile();
	}

	public void replay() {
		deck.copyFrom(saveDeck);
		dealCards();
		setPile();
	}
}
