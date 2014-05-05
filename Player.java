//******************************************************************************
//
// File:    Player.java
// Package: ---
// Unit:    Class Player
//
//******************************************************************************

/**
 * Class Player represents a player in the Fifteen game.
 *
 * @author  Marko Galesic
 * @version 16-Mar-2014
 */
public class Player
{
	// Id, name, and current total sum of this player
	private int _id;
	private String _name;
	private int _sum;
	
	/*
	 * Player constructor initializing just the name of the player.
	 */
	public Player (String name)
	{
		_name = name;
	}
	
	/*
	 * Player constructor initializing both the name and id of the player.
	 */
	public Player (String name, int id)
	{
		_name = name;
		_id = id;
	}
	
	/**
	 * Set id of the player
	 *
	 * @param  id Id of the player
	 */
	public void setId (int id) {_id = id;}

	/**
	 * Set sum \ score of the player
	 *
	 * @param  sum sum \ score of the player
	 */
	public void setSum (int sum) {_sum = sum;}

	/**
	 * Add to score of player
	 * 
	 * @param
	 */
	public void addScore(int digit) {_sum += digit;} 
	

	/**
	 * Get id of the player
	 */
	public int getId () {return _id;}

	/**
	 * Get id of the player
	 */
	public String getName () {return _name;}

	/**
	 * Get sum of the player
	 */
	public int getSum () {return _sum;}
}
