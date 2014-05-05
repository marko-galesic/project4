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
import java.util.LinkedList;

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
	private List<FifteenModelListener> _listeners = new LinkedList<FifteenModelListener>();

	// Exported constructors.

	/**
	 * Construct a new Go model.
	 */
	public FifteenModel(Player player1)
	{
		_player1 = player1;
		_player1.setId(1);
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
		_player2.setId(2);
	}

	/**
	 * Add the given model listener to this Go model.
	 *
	 * @param  modelListener  Model listener.
	 */
	public synchronized void addModelListener(FifteenModelListener modelListener) throws IOException
	{
		_listeners.add(modelListener);
			
		// If the second player has been added, start the session
		if (_listeners.size() == 2)
		{
			for (FifteenModelListener listener : _listeners)
			{
				listener.buttonStates(_game.getButtonStati());
				listener.scoreReport(1, _player1.getSum());
				listener.scoreReport(2, _player2.getSum());
				listener.turnId(_game.getTurnId());
			}
		}
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
	public synchronized void digitButtonPressed(int i) throws IOException
	{
		_game.buttonPressed(i);
		// Add to score with respect to whose turn it is
		if (_game.getTurnId() == 1)
		{
			_player1.addScore(i);
			_game.setTurnId(2);
		}
		else
		{
			_player2.addScore(i);
			_game.setTurnId(1);
		}
	
		// Check win conditions
		if (_player1.getSum() == 15)
		{
			for (FifteenModelListener listener : _listeners)
			{
				// Disable all buttons for the win state
				for (int b = 1; b <= 9; b++)
				{
					_game.buttonPressed(b);
				}
				listener.buttonStates(_game.getButtonStati());
				listener.scoreReport(1, _player1.getSum());
				listener.scoreReport(2, _player2.getSum());
				listener.winId(1);
			}
		}
		else if(_player2.getSum() == 15)
		{
			for (FifteenModelListener listener : _listeners)
			{
				// Disable all buttons for the win state
				for (int b = 1; b <= 9; b++)
				{
					_game.buttonPressed(b);
				}
				listener.buttonStates(_game.getButtonStati());
				listener.scoreReport(1, _player1.getSum());
				listener.scoreReport(2, _player2.getSum());
				listener.winId(2);
			}
		}
	
		// Check for draw condition
		else if (_game.allButtonsDisabled())
		{
			for (FifteenModelListener listener : _listeners)
			{
				listener.buttonStates(_game.getButtonStati());
				listener.scoreReport(1, _player1.getSum());
				listener.scoreReport(2, _player2.getSum());
				listener.winId(0);
			}
		}
		
		// Continue game
		else
		{
			for (FifteenModelListener listener : _listeners)
			{
				listener.buttonStates(_game.getButtonStati());
				listener.scoreReport(1, _player1.getSum());
				listener.scoreReport(2, _player2.getSum());
				listener.turnId(_game.getTurnId());
			}
		}
	}

	/**
	 * Report when the new game button has been pressed
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public synchronized void newGamePressed() throws IOException
	{
		// Reset game state
		char[] buttonStates = new char[9];
		for (int b = 0; b < 9; b++)
		{
			buttonStates[b] = '1';
		}
		
		_game.setButtons(buttonStates);
		_player1.setSum(0);
		_player2.setSum(0);
		
		for (FifteenModelListener listener : _listeners)
		{	
			listener.buttonStates(_game.getButtonStati());
			listener.scoreReport(1, _player1.getSum());
			listener.scoreReport(2, _player2.getSum());
			listener.turnId(1);
		}
	}

	/**
	 * Report that a user quit
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public synchronized void quit() throws IOException
	{
		for (FifteenModelListener listener : _listeners)
		{
			listener.quit();
		}
	}
}
