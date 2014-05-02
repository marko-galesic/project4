//******************************************************************************
//
// File:    GoModel.java
// Package: ---
// Unit:    Class GoModel
//
//******************************************************************************

import java.awt.Color;
import java.io.IOException;
import java.util.List;

/**
 * Class FifteenModel provides the server-side model object in the Fifteen Game.
 *
 * @author  Alan Kaminsky
 * @author  Marko Galesic
 * @version 28-Sep-2013
 */
public class FifteenModel implements FifteenViewListener
{
	// Hidden data members.
	private FifteenGame _game = new FifteenGame();
	private FifteenModelListener _listener;
	private Player _player1;
	private Player _player2;

	// Exported constructors.

	/**
	 * Construct a new Go model.
	 */
	public FifteenModel(Player player1)
	{
		_player1 = player1;
	}

	// Exported operations.
	/**
	 * Add the second player to the model
	 *
	 * @param  player2  the second player to add
	 */
	public void addSecondPlayer(Player player2)
	{
		_player2 = player2;
	}

	/**
	 * Add the given model listener to this Go model.
	 *
	 * @param  modelListener  Model listener.
	 */
	public synchronized void addModelListener(FifteenModelListener modelListener)
	{
		// Pump up the new client with the current state of the Fifteen game.
	
		// Record listener.
		_listener = modelListener;
	}

	/**
	 * Ask to join with specified playerName
	 *
	 * @param playerName player's name that wants to join
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void join(FifteenViewProxy proxy, String playerName) throws IOException
	{
	}

	/**
	 * Report when a digit button has been pressed
	 *
	 * @param i number the button represents
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void digitButtonPressed(int i) throws IOException
	{
		char[] buttonStates = new char[9];
		for(int b = 0; b < 9; b++)
		{
			buttonStates[b] = _game.getButtonStatus(b) == true ? '1' : '0'; 
		}
		_listener.buttonStates(buttonStates);
	}

	/**
	 * Report when the new game button has been pressed
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void newGamePressed() throws IOException
	{
	}

	/**
	 * Report that a user quit
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void quit() throws IOException
	{
	}
}
