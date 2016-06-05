package com.tonyjs.solitaire;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class CardWithImage extends Card {
	final static String sep = java.io.File.separator;
	private static final ImageIcon cardBackImage = 
			new ImageIcon("cards" + sep + "cardBack.png");
	private ImageIcon myImage;
	private Image image;
	private static final int CARDHEIGHT = 142;
	private static final int CARDWIDTH = 100;
	private int width = 0;
	private int height = 0;
	private int x = 0;
	private int y = 0;

	public CardWithImage(String theRank, String theSuit, int theValue) {
		super(theRank, theSuit, theValue);
		myImage = new ImageIcon("cards" + sep + theRank + theSuit + ".png");
		this.image = getImageFromIcon();
	}

	public CardWithImage(String theRank, String theSuit) {
		super(theRank, theSuit);
		myImage = new ImageIcon("cards" + sep + theRank + theSuit + ".png");
		this.image = getImageFromIcon();
	}

	public Image getImageFromIcon() {
		return isFaceUp() ? ((ImageIcon)myImage).getImage() : ((ImageIcon)cardBackImage).getImage();
	}

	public void draw(Graphics g) {
		image = getImageFromIcon();
		this.width = image.getWidth(null);
		this.height = image.getHeight(null);
		g.drawImage(image, x, y, null);
	}

	public static void drawOutline(Graphics g, int x, int y) {
		g.setColor(new Color(255, 255, 255, 80));
		g.fillRoundRect(x, y, CARDWIDTH, CARDHEIGHT, 8, 8);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void addToXY(int changeX, int changeY) {
		x += changeX;
		y += changeY;
	}

	public static int getCardHeight() {
		return CARDHEIGHT;
	}

	public static int getCardWidth() {
		return CARDWIDTH;
	}

	public boolean contains(int pointX, int pointY) {
		boolean contains = false;
		if (pointX >= x 
				&& pointX <= x + width
				&& pointY >= y
				&& pointY <= y + height) {
			contains = true;
		}
		return contains;
	}

	public boolean isNear(int pointX, int pointY) {
		boolean isNear = false;
		int offsetX = CARDWIDTH/2;
		int offsetY = CARDHEIGHT;
		if (pointX > x - offsetX 
				&& pointX < x + offsetX
				&& pointY > y - offsetY
				&& pointY < y - offsetY) {
			isNear = true;
		}
		return isNear;
	}

	public boolean isNear(CardWithImage card) {
		int pointX = card.getX();
		int pointY = card.getY();
		boolean isNear = isNear(pointX, pointY);
		return isNear;
	}
}
