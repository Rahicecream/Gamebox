package com.ninjaboxer.core;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;


import com.ninjaboxer.bean.TotalPunches;
import com.ninjaboxer.logic.NinjaBoxerModel;
import com.ninjaboxer.logic.SingletonImpl;
import com.ninjaboxer.sound.SimpleSoundPlayer;

public class NinjaBoxerBoard extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int B_WIDTH = 500;
	private final int B_HEIGHT = 500;
	private final int INITIAL_X = -40;
	private final int INITIAL_Y = -40;
	private int DELAY = 2000;
	private int punches = 1;
	public float currentScore;
	public float currentPunches;
	public float currentAccuracy;

	private Image boxer;
	private int x, y;
	private Random random;
	private boolean initialLoad =  true;
	private int previousId;

	public JLabel waitLabel;
	public JLabel outcomeLabel;
	public JLabel scoreLabel;
	public JLabel totalPunchesLabel;
	public JLabel accuracyLabel;
	public JButton ready;
	private NinjaBoxerModel model;

	private Timer timer;

	CardLayout secondLayout = new CardLayout();

	JPanel results = new JPanel();
	/*JPanel game = new JPanel();
	JPanel menu = new JPanel();*/

	private boolean keyFlag;
	private TotalPunches totalPunchesBean;
	private int instructionId = 4 ;
	private int score = 0;

	CardLayout panelLayout = new CardLayout();

	SimpleSoundPlayer punchSound = new SimpleSoundPlayer("sounds/punch.wav");
	SimpleSoundPlayer flySound = new SimpleSoundPlayer("sounds/whoosh.wav");
	SimpleSoundPlayer noSound = new SimpleSoundPlayer("sounds/no.wav");

	public NinjaBoxerBoard(JLabel total, JLabel scor, JLabel out, JLabel accuracy)
	{	
		this.totalPunchesLabel = total;
		this.scoreLabel = scor;
		this.outcomeLabel =out;
		this.accuracyLabel = accuracy;
		this.totalPunchesBean = new TotalPunches();
		this.model = new NinjaBoxerModel();


		setFocusable(true);

		InputMap im = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = this.getActionMap();

		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT_ARROW");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT_ARROW");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP_ARROW");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN_ARROW");

		am.put("RIGHT_ARROW", new ArrowAction("RIGHT_ARROW"));
		am.put("LEFT_ARROW", new ArrowAction("LEFT_ARROW"));
		am.put("UP_ARROW", new ArrowAction("UP_ARROW"));
		am.put("DOWN_ARROW", new ArrowAction("DOWN_ARROW"));

		initBoard();
		loadImage();

	}

	private void loadImage() {
		ImageIcon ii = new ImageIcon("NinjaBoxer.png");
		boxer = ii.getImage();
	}

	private void initBoard()
	{
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		setDoubleBuffered(true);


		x = INITIAL_X;
		y = INITIAL_Y;

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		timer = new Timer(DELAY, this);
		timer.start();

	}

	/*@Override
	public void addNotify() {
		super.addNotify();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		animator = new Thread(this);
		animator.start();
	}*/

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);


		if(initialLoad)
		{
			instructionId = getRandomInstruction();
			initialLoad=false;
		}
		else
		{

			instructionId = getRandomInstruction();
			while(previousId==instructionId)
				instructionId = getRandomInstruction();

		}
		previousId=instructionId;

		drawBoxer(g,instructionId);


		g.dispose();
	}

	/*@Override
	public void run() {
		// TODO Auto-generated method stub
		long beforeTime, timeDiff, sleep;
		Thread thisThread = Thread.currentThread();
		beforeTime = System.currentTimeMillis();
		//Thread thisThread = Thread.currentThread();
		do{

			//cycle();
			repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0) {
				sleep = 2;
			}

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("Interrupted: " + e.getMessage());
			}

			beforeTime = System.currentTimeMillis();
		}while(animator == thisThread);

	}*/

	/*public void stop() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		animator. = null;
	}*/

	public int findCenterPoint(int size)
	{
		int location_x = getSize().width;
		return ((location_x-size)/2)-1;

	}

	public int getRandomInstruction()
	{	
		random = new Random();
		return random.nextInt(4);
	}


	public void drawBoxer(Graphics g, int id)
	{	
		Graphics2D g2d = (Graphics2D) g;
		int designId = id;


		currentScore = score;
		currentPunches = punches;

		//currentAccuracy = (currentScore/currentPunches)*100;

		g2d.drawRect(findCenterPoint(30), findCenterPoint(30)-50, 30, 30);
		g2d.drawRect(findCenterPoint(30)-50, findCenterPoint(30), 30, 30);
		g2d.drawRect(findCenterPoint(30), findCenterPoint(30)+ 50, 30, 30);
		g2d.drawRect(findCenterPoint(30)+50, findCenterPoint(30), 30, 30);

		if(punches==10){
			outcomeLabel.setText("Stage 2");
			accuracyLabel.setText("Punch faster..");
		}
		else if (punches==20)
		{
			outcomeLabel.setText("Stage 3");
			accuracyLabel.setText("Much faster..");
		}
		else if (punches == 30)
		{
			//outcomeLabel.setText("Game Ends");
		}

		totalPunchesLabel.setText("Total Punches : "+Integer.toString(punches++));

		switch(designId)
		{
		case 2:
			g2d.drawImage(boxer,findCenterPoint(30)+50, findCenterPoint(30), null);
			//g2d.fillRect(findCenterPoint(30)+50, findCenterPoint(30), 30, 30);
			playFlyWhoosh();
			break;

		case 1:
			g2d.drawImage(boxer,findCenterPoint(30), findCenterPoint(30)+50,null);
			//g2d.fillRect(findCenterPoint(30), findCenterPoint(30)+50, 30, 30);
			playFlyWhoosh();
			break;

		case 0:
			g2d.drawImage(boxer,findCenterPoint(30), findCenterPoint(30)-50,null);
			//g2d.fillRect(findCenterPoint(30), findCenterPoint(30)-50, 30, 30);
			playFlyWhoosh();
			break;

		case 3:
			g2d.drawImage(boxer, findCenterPoint(30)-50, findCenterPoint(30), null);
			//g2d.fillRect(findCenterPoint(30)-50, findCenterPoint(30), 30, 30);
			playFlyWhoosh();
			break;

		default:
			System.exit(0);
		}

		keyFlag=true;
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}


	/*private void cycle() {

		x += 1;
		y += 1;

		if (y > B_HEIGHT) {

			y = INITIAL_Y;
			x = INITIAL_X;
		}
	}*/

	public void playPunch()
	{
		InputStream stream = new ByteArrayInputStream(punchSound.getSamples());
		punchSound.play(stream);
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void playFlyWhoosh()
	{
		InputStream stream1 = new ByteArrayInputStream(flySound.getSamples());
		flySound.play(stream1);
		try {
			stream1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void playNo()
	{
		InputStream stream2 = new ByteArrayInputStream(noSound.getSamples());
		noSound.play(stream2);
		try {
			stream2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

		if(keyFlag)
		{
			int keyCode = e.getKeyCode();
			if(keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40)
			{
				playPunch();

				int keyId=(int)SingletonImpl.getInstance().getKeyHashMap().get(keyCode);
				PunchMeHereModel.GameOutcome outcome = model.decisionPoint(instructionId,keyId);

				//System.out.println("InstructionId : "+instructionId);
				//System.out.println("KeyId : "+keyId);
				if(outcome == PunchMeHereModel.GameOutcome.PUNCHED){
					//outcomeLabel.setText("Punched");
					scoreLabel.setText("Score : "+Integer.toString(++score));



				}
				else if(outcome == PunchMeHereModel.GameOutcome.MISS){
					//outcomeLabel.setText("Missed");
				}else{
					outcomeLabel.setText("The game was a tie!");
				}
				keyFlag=false;
			}
			else
			{
				outcomeLabel.setText("Press Arrow Keys");
				keyFlag = true;
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}*/

	public class ArrowAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4186059015116591363L;
		private String cmd;

		public ArrowAction(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(keyFlag)
			{

				if (!cmd.equals(null))
				{
					int keyId=(int)SingletonImpl.getInstance().getKeyHashMap().get(cmd);

					NinjaBoxerModel.GameOutcome outcome = model.decisionPoint(instructionId,keyId);

					//System.out.println("InstructionId : "+instructionId);
					//System.out.println("KeyId : "+keyId);
					if(outcome == NinjaBoxerModel.GameOutcome.PUNCHED){
						playPunch();
						//outcomeLabel.setText("Punched");
						scoreLabel.setText("Your Score : "+Integer.toString(++score));

						//System.out.println(score);
						//System.out.println(punches);



					}
					else if(outcome == NinjaBoxerModel.GameOutcome.MISS){
						playNo();
						//outcomeLabel.setText("Missed");
					}else{
						outcomeLabel.setText("The game was a tie!");
					}
					keyFlag=false;
				}
			}

		}
	}



	@Override
	public void actionPerformed(ActionEvent e) {



		if(punches<10){
			repaint();
		}
		else if (punches >= 10 && punches <20)
		{
			timer.setDelay(1000);
			repaint();
		}
		else if(punches >= 20 && punches <=30)
		{
			timer.setDelay(800);
			repaint();
		}
		else if(punches>30)
		{

			timer.stop();
			float accuracyFloat = ((float)score/(float)(punches-1))*100;
			accuracyLabel.setText("Boxing is real easy. Life is much harder. \r\n Your Accuracy :"+String.format("%.2f",accuracyFloat)+" %");
			outcomeLabel.setText("Game Ends");
		}

	}

}
