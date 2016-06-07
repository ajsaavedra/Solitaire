package com.tonyjs.solitaire;

import java.io.*;
import javax.sound.sampled.*;

public class SoundEffect {
	private static File redealFile, cardFlipFile;
	private static Clip songClip;
	final static String sep = java.io.File.separator;

	public static void playRedealEffect() {
		redealFile = new File("sounds" + sep + "redeal.wav");
		playSongFile(redealFile);
	}

	public static void playCardFlipEffect() {
		cardFlipFile = new File("sounds" + sep + "draw.wav");
		playSongFile(cardFlipFile);
	}

	public static Clip playSongFile(File song) {
		try {
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;

			stream = AudioSystem.getAudioInputStream(song);
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			songClip = (Clip) AudioSystem.getLine(info);
			songClip.open(stream);
			if (songClip.isRunning()) {
				songClip.stop();
			} else {
				FloatControl gainControl = 
						(FloatControl) songClip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-10.0f);
				songClip.start();
			}
		}
		catch (Exception e) {
			System.out.println("Could not load song file");
		}
		return songClip;
	}
}
