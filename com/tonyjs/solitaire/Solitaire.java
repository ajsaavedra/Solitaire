package com.tonyjs.solitaire;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Solitaire extends JFrame {
	private static final long serialVersionUID = 1L;
	private TablePanel tablePanel = new TablePanel();
	private JPanel buttonPanel;
	private JButton newGameButton, restartButton;

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
		
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLACK);
		add(buttonPanel, BorderLayout.PAGE_END);
		
		newGameButton = new JButton("Redeal");
		newGameButton.setFocusable(false);
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SoundEffect.playRedealEffect();
				tablePanel.newGame();
			}
		});
		buttonPanel.add(newGameButton);
		
		restartButton = new JButton("Restart");
		restartButton.setFocusable(false);
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tablePanel.replay();
			}
		});
		buttonPanel.add(restartButton);
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
