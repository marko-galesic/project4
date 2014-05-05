//******************************************************************************
//
// File:    FifteenGame.java
// Package: ---
// Unit:    Class FifteenGame
//
//******************************************************************************

/**
 * Class FifteenGame provides the model for the Fifteen network game.
 *
 * @author  Marko Galesic
 * @version 28-Mar-2014
 */
public class FifteenGame
{
	// Hidden data members
	private boolean[] _buttons;	// Representation of the on \ off button states
	private int turnId;		// The Id of the player whose turn it is
	/**
	 * Constructor for the FifteenGame
	 *
	 * @param p1 local Player
	 */
	public FifteenGame ()
	{
		// Initalize the buttons' state representations
		_buttons = new boolean[10];
		
		for(int b = 0; b < _buttons.length; b++)
		{
			_buttons[b] = true;
		}
		
		// Set turn id to the first player
		turnId = 1;
	}

	/**
	 * Sets the Fifteen buttons state
	 *
	 * @param buttonStates the states of the buttons currently
	 */
	public void setButtons(char[] buttonStates)
	{
		// Go through all of the elements of button states to
		for(int b = 0; b < 9; b++)
		{
			// Enable or disable with respect to '0' char or '1' chars
			if (buttonStates[b] == '0')
			{
				_buttons[b] = false;
			}
			else
			{
				_buttons[b] = true;
			}
		}
	}

	/*
	 * Sets the status to one button to disabled
	 *
	 * @param i the identifier of the button that was pressed
	 */
	public void buttonPressed(int i)
	{
		_buttons[i - 1] = false;
	}	
	
	/**
	 * Gets the Fifteen buttons state of the ith button
	 *
	 * @param i the number of the button whose state we want
	 */
	public boolean getButtonStatus(int i)
	{
		return _buttons[i];
	}	

	/**
	 * Converts the internal boolean representation of the buttons into a char
	 * array for convenient network transport.
	 *
	 */
	public char[] getButtonStati()
	{
		char[] buttons = new char[9];
		// Go through all of the elements of button states to
		for(int b = 0; b < 9; b++)
		{
			buttons[b] = _buttons[b] ? '1' : '0';
		}
		return buttons;
	}

	/**
	 * Get turn id
	 */
	public int getTurnId()
	{
		return turnId;
	}

	/**
	 * Set the turn id of the game
	 *
	 * @param i id of the current player whose turn it is
	 */
	public void setTurnId(int i)
	{
		turnId = i;
	}

	/**
	 * Query for the game state whether or not the buttons are all disabled
	 */
	public boolean allButtonsDisabled()
	{
		boolean disabled = true;
		for(int b = 0; b < 9; b++)
		{
			if (_buttons[b]) disabled = false;
		}
		return disabled;
	}
}
