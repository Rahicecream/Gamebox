package com.ninjaboxer.core;

import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT;
import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.ninjaboxer.sound.SimpleSoundPlayer;

public class NinjaBoxerRunner extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel totalPunchesLabel;
	private JLabel scoreLabel;
	private JLabel outcomeLabel;
	private JLabel accuracyLabel;
	public JLabel waitLabel;
	public JLabel rulesLabel;

	private final int B_WIDTH = 400;
	private final int B_HEIGHT = 400;
	private Timer preGameTimer;
	private int count;

	private JButton play = new JButton("Start Punching");
	private JButton hw2play = new JButton("How to Play");
	private JButton back = new JButton("Back");
	private JButton exit = new JButton("Exit");
	private JButton mainMenu = new JButton("Main menu");

	SimpleSoundPlayer beepSound = new SimpleSoundPlayer("sounds/beep.wav");

	CardLayout layout = new CardLayout();

	JPanel panel = new JPanel();
	JPanel preGame = new JPanel();
	JPanel how2Play = new JPanel();
	JPanel game = new JPanel();
	JPanel menu = new JPanel();


	public NinjaBoxerRunner() {

		super("GradientTranslucentWindow");
		setBackground(new Color(0,0,0,0));

		/*JPanel bottomPanel = new JPanel();
		JPanel topPanel = new JPanel();

		totalPunchesLabel = new JLabel("Total Punches : ");
		scoreLabel = new JLabel("Score : 0");
		outcomeLabel = new JLabel("Outcome :");
		accuracyLabel = new JLabel("Accuracy :");

		bottomPanel.add(totalPunchesLabel,BorderLayout.EAST);
		bottomPanel.add(scoreLabel,BorderLayout.CENTER);
		bottomPanel.add(accuracyLabel,BorderLayout.WEST);

		add(bottomPanel, BorderLayout.SOUTH);

		topPanel.add(play,BorderLayout.WEST);
		topPanel.add(exit,BorderLayout.EAST);
		topPanel.add(outcomeLabel,BorderLayout.CENTER);

		add(topPanel,BorderLayout.NORTH);*/

		panel.setLayout(layout); 

		initBoard();
		initMainMenu();
		//initGame();

		count = 3;

		play.addActionListener(this);
		hw2play.addActionListener(this);
		exit.addActionListener(this);


		//add(new NinjaBoxerBoard(totalPunchesLabel,scoreLabel,outcomeLabel,accuracyLabel));
		setTitle("Ninja Boxer");
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(panel);
		layout.show(panel,"Menu");
	}

	public void TransparentPanel()
	{
		createAndShowGUI();
	}

	public void createAndShowGUI()
	{

	}

	public void initInstruction()
	{
		how2Play.setLayout(new BorderLayout());
		how2Play.setBackground(Color.WHITE);
		how2Play.setOpaque(true);


		rulesLabel = new JLabel("<html><u><center>Rules for Ninja Boxer</center></u><br><br>1. Ninja boxer randomly moves around in the four boxes.<br><br> 2. Punching the ninja boxer on time increments the score.<br><br>3. Use the top, bottom, right and left arrow keys to punch the ninja boxer.<br><br>3. Go ahead, Punch faster and Try to get 100% accuracy.<br><html>");
		rulesLabel.setFocusable(true);

		how2Play.add(rulesLabel,BorderLayout.CENTER);

		back.setBorderPainted(true);
		back.setFocusPainted(false);
		back.setContentAreaFilled(false);

		how2Play.add(back,BorderLayout.SOUTH);

		back.addActionListener(this);
		panel.add(how2Play,"How2Play");
	}

	public void initPreGame()
	{	
		preGame.setLayout(new GridBagLayout());
		preGame.setBackground(Color.WHITE);
		preGame.setOpaque(true);

		waitLabel = new JLabel("");
		preGameTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(count>=0)
				{	
					playBeep();
					waitLabel.setText("Game starts in "+count);
					count--;
				}
				else
				{
					preGameTimer.stop();
					initGame();
					layout.show(panel, "Game");
				}
			}
		});


		preGameTimer.start();
		waitLabel.setFocusable(true);

		preGame.add(waitLabel);
		panel.add(preGame,"PreGame");

	}


	public void initMainMenu()
	{
		menu.setLayout (new GridBagLayout());
		menu.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints ();

		// next two lines will place the components top-to-bottom, rather than left-to-right
		gbc.gridx = 0;
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.gridy = GridBagConstraints.RELATIVE;

		play.setBorderPainted(true);
		play.setFocusPainted(false);
		play.setContentAreaFilled(false);

		hw2play.setBorderPainted(true);
		hw2play.setFocusPainted(false);
		hw2play.setContentAreaFilled(false);

		exit.setBorderPainted(true);
		exit.setFocusPainted(false);
		exit.setContentAreaFilled(false);

		// add as many buttons as you want in a column
		menu.add(play,gbc);
		menu.add(hw2play,gbc);
		menu.add(exit,gbc);

		panel.add(menu,"Menu");

	}


	public void playBeep()
	{
		InputStream stream3 = new ByteArrayInputStream(beepSound.getSamples());
		beepSound.play(stream3);
		try {
			stream3.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void initGame()
	{
		/*JPanel bottomPanel = new JPanel();
		JPanel topPanel = new JPanel();

		totalPunchesLabel = new JLabel("Total Punches : ");
		scoreLabel = new JLabel("Score : 0");
		outcomeLabel = new JLabel("Outcome :");
		accuracyLabel = new JLabel("Accuracy :");

		bottomPanel.add(totalPunchesLabel,BorderLayout.EAST);
		bottomPanel.add(scoreLabel,BorderLayout.CENTER);
		bottomPanel.add(accuracyLabel,BorderLayout.WEST);

		game.add(bottomPanel, BorderLayout.SOUTH);

		topPanel.add(play,BorderLayout.WEST);
		topPanel.add(exit,BorderLayout.EAST);
		topPanel.add(outcomeLabel,BorderLayout.CENTER);

		game.add(topPanel,BorderLayout.NORTH);
		//game.add(mainMenu);*/

		game.setLayout(new BorderLayout());

		JPanel bottomPanel = new JPanel();
		JPanel topPanel = new JPanel();

		bottomPanel.setBackground(Color.WHITE);
		topPanel.setBackground(Color.WHITE);


		totalPunchesLabel = new JLabel("Punches : ");
		scoreLabel = new JLabel("Score : 0");
		outcomeLabel = new JLabel("Stage 1");
		accuracyLabel = new JLabel("Come on punch..");

		bottomPanel.add(totalPunchesLabel,BorderLayout.EAST);
		bottomPanel.add(scoreLabel,BorderLayout.CENTER);
		bottomPanel.add(outcomeLabel,BorderLayout.WEST);

		topPanel.add(accuracyLabel,BorderLayout.CENTER);

		game.add(bottomPanel, BorderLayout.SOUTH);
		game.setFocusable(true);

		game.add(new NinjaBoxerBoard(totalPunchesLabel,scoreLabel,outcomeLabel,accuracyLabel));
		game.add(topPanel,BorderLayout.NORTH);
		panel.add(game,"Game");



	}


	private void initBoard()
	{
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		//setDoubleBuffered(true);
	}


	public static void center(JFrame frame) {

		// get the size of the screen, on systems with multiple displays,
		// the primary display is used
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// calculate the new location of the window
		int w = frame.getSize().width;
		int h = frame.getSize().height;

		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// moves this component to a new location, the top-left corner of
		// the new location is specified by the x and y
		// parameters in the coordinate space of this component's parent
		frame.setLocation(x, y);

	}

	public static void main(String[] args) {
		GraphicsEnvironment ge = 
				GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		boolean isPerPixelTranslucencySupported = 
				gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);

		//If translucent windows aren't supported, exit.
		if (!isPerPixelTranslucencySupported) {
			System.err.println(
					"Translucency is not supported");
			System.exit(0);
		}

		JFrame.setDefaultLookAndFeelDecorated(true);

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {                
				JFrame ex = new NinjaBoxerRunner();
				center(ex);
				// Set the window to 55% opaque (45% translucent).
				ex.setOpacity(0.80f);

				ex.setVisible(true);                
			}
		});
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		Object source = event.getSource();

		if (source == exit) {

			System.exit(0);
		}
		else if (source == play)
		{
			initPreGame();
			layout.show(panel,"PreGame");
		}
		else if (source == hw2play)
		{
			initInstruction();
			layout.show(panel, "How2Play");
		}
		else if (source == back)
		{
			layout.show(panel, "Menu");
		}

	}
}