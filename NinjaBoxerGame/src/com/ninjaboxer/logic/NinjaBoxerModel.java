package com.ninjaboxer.logic;

import java.util.Random;

public class NinjaBoxerModel {

	private int score = 0;
	private int punches = 0;
	private Random random;

	public static enum GameOutcome{
		MISS, PUNCHED;
	}

	public GameOutcome decisionPoint(int instructionId, int userKeyId)
	{
		if(instructionId==userKeyId)
		{
			score++;
			punches++;
			return GameOutcome.PUNCHED;
		}
		else
		{
			punches++;
			return GameOutcome.MISS;
		}
	}

	public int getRandomInstruction()
	{	
		random = new Random();
		return random.nextInt(4);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPunches() {
		return punches;
	}

	public void setPunches(int punches) {
		this.punches = punches;
	}


	public static void main(String[] args)
	{
		NinjaBoxerModel model = new NinjaBoxerModel();
		System.out.println(model.getRandomInstruction());
	}
}
