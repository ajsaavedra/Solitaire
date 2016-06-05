package com.tonyjs.solitaire;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Solitaire extends JFrame {
	private static final long serialVersionUID = 1L;
	private TablePanel tablePanel = new TablePanel();

	public Solitaire() {
		initGUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}

	public void initGUI() {
		setTitle("Solitaire");
		add(tablePanel);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args) {
		try {
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(className);
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Solitaire();
			}
		});
	}
}
