package com.tonyjs.solitaire;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CardWithImage extends Card {
	final static String sep = java.io.File.separator;
	private static final int CARDHEIGHT = 142;
	private static final int CARDWIDTH = 100;
	private static final int OVERLAP = (int) (CARDHEIGHT * .20);
	private int width = 0;
	private int height = 0;
	private int x = 0;
	private int y = 0;
	private BufferedImage imageBuffered, retrievedImage;
	private static BufferedImage cardBackImage;
	
	
	public static void setBackImage() {
		try {
			cardBackImage = ImageIO.read(new File("cards" + sep + "cardBack.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CardWithImage(String theRank, String theSuit, int theValue) {
		super(theRank, theSuit, theValue);
		try {
			imageBuffered = ImageIO.read(new File("cards" + sep + theRank + theSuit + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CardWithImage(String theRank, String theSuit) {
		super(theRank, theSuit);
		try {
			imageBuffered = ImageIO.read(new File("cards" + sep + theRank + theSuit + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getImageFromIcon() {
		return isFaceUp() ? imageBuffered : cardBackImage;
	}

	public void draw(Graphics g) {
		retrievedImage = getImageFromIcon();
		this.width = retrievedImage.getWidth(null);
		this.height = retrievedImage.getHeight(null);
		g.drawImage(retrievedImage, x, y, null);
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

	public boolean contains(int pointX, int pointY, int colSize, boolean isLast) {
		boolean contains = false;
		if (isLast) {
			if (colSize == 0) {// if just grabbing the last card
				if (pointX >= x 
						&& pointX <= x + width
						&& pointY >= y
						&& pointY <= y + height) {
					contains = true;
				}
			} else {// if other cards above the last one are grabbed
				if (pointX >= x 
						&& pointX <= x + width
						&& pointY <= y + height + OVERLAP * colSize) {
					contains = true;
				}
			}
		} else {
			if (colSize == 0) {//if grabbing more than one
				if (pointX >= x 
						&& pointX <= x + width
						&& pointY >= y
						&& pointY <= y + OVERLAP) {
					contains = true;
				}
			} else {//if grabbed more than one
				if (pointX >= x 
						&& pointX <= x + width
						&& pointY <= y + height + OVERLAP * colSize) {
					contains = true;
				}
			}
		}
		return contains;
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
