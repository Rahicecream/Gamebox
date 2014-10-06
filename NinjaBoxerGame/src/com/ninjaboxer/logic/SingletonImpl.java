package com.ninjaboxer.logic;

import java.util.HashMap;

public class SingletonImpl {

	/**
	 * @param args
	 */
	static HashMap instructionHashMap;
	static HashMap keyHashMap;

	public HashMap getInstructionHashMap() {
		return instructionHashMap;
	}
	public void setInstructionHashMap(HashMap instructionHashMap) {
		this.instructionHashMap = instructionHashMap;
	}
	public HashMap getKeyHashMap() {
		return keyHashMap;
	}
	public void setKeyHashMap(HashMap keyHashMap) {
		this.keyHashMap = keyHashMap;
	}
	private static SingletonImpl instance = null;


	protected SingletonImpl() {
		// Exists only to defeat instantiation.
	}
	public static SingletonImpl getInstance() {
		if(instance == null) {
			instance = new SingletonImpl();
			loadHashMap();
		}
		return instance;
	}
	public static void loadHashMap() {
		instructionHashMap = new HashMap();
		keyHashMap = new HashMap();
		// Put elements to the map
		instructionHashMap.put(0, "Up Arrow");
		instructionHashMap.put(1, "Down Arrow");
		instructionHashMap.put(2, "Right Arrow");
		instructionHashMap.put(3, "Left Arrow");
		keyHashMap.put("UP_ARROW", 0);
		keyHashMap.put("DOWN_ARROW", 1);
		keyHashMap.put("RIGHT_ARROW", 2);
		keyHashMap.put("LEFT_ARROW", 3);

	}



}
