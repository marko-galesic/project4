//******************************************************************************
//
// File:    FifteenModelListener.java
// Package: ---
// Unit:    Class FifteenModelListener
//
//******************************************************************************


import java.io.IOException;

/**
 * Class FifteenModelListener provides an interface the model proxy can call the view with.
 *
 * @author  Marko Galesic
 * @version 28-Mar-2014
 */
public interface FifteenModelListener
{
	/**
	 * Report the id of the local player
	 *
	 * @param id id of the local player
	 */
	public void localId(int id)  throws IOException;

	/**
	 * Report a players id and name
	 *
	 * @param id a players id
	 * @param String playerName
	 */
	public void playerIdAndName(int id, String playerName)  throws IOException;

	/**
	 * Report button states
	 *
	 * @param buttonStates 	an array that represents whether
	 *			or not buttons 1-9 are depressed or
	 *			not
	 */
	public void buttonStates(char[] buttonStates)  throws IOException;

	/**
	 * Report score for an id
	 *
	 * @param id id of the player the score report is for
	 * @param score score of the player
	 */
	public void scoreReport(int id, int score)  throws IOException;

	/**
	 * Report the turn id
	 *
	 * @param id id of the player whose turn it is
	 */
	public void turnId(int id)  throws IOException;

	/**
	 * Report a win.
	 *
	 * @param id id of the player who won
	 */
	public void winId(int id)  throws IOException;

	/**
	 * Send a message to quit
	 */
	public void quit()  throws IOException;
}
