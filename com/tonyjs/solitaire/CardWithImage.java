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
	private int x = 0;
	private int y = 0;
	
	public CardWithImage(String theRank, String theSuit, int theValue) {
		super(theRank, theSuit, theValue);
		myImage = new ImageIcon("cards" + sep + theRank + theSuit + ".png");
	}
	
	public CardWithImage(String theRank, String theSuit) {
		super(theRank, theSuit);
		myImage = new ImageIcon("cards" + sep + theRank + theSuit + ".png");
	}
	
	public Image getImageFromIcon() {
		return isFaceUp() ? ((ImageIcon)myImage).getImage() : ((ImageIcon)cardBackImage).getImage();
	}
	
	public void draw(Graphics g) {
		image = getImageFromIcon();
		g.drawImage(image, x, y, null);
	}
	
	public static void drawOutline(Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.drawRoundRect(x, y, CARDWIDTH, CARDHEIGHT, 8, 8);
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

	public static int getCardHeight() {
		return CARDHEIGHT;
	}
	
	public static int getCardWidth() {
		return CARDWIDTH;
	}
}
