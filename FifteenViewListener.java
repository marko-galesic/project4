//******************************************************************************
//
// File:    FifteenViewListener.java
// Package: ---
// Unit:    Class FifteenViewListener
//
//******************************************************************************

import java.io.*;

/**
 * Class FifteenViewListener provides the interface that the view can communicate with the model
 * or model proxy.
 *
 * @author  Marko Galesic
 * @version 16-Mar-2014
 */
public interface FifteenViewListener
{
	/**
	 * Ask to join with specified playerName
	 *
	 * @param playerName player's name that wants to join
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void join(FifteenViewProxy proxy, String playerName) throws IOException;

	/**
	 * Report when a digit button has been pressed
	 *
	 * @param i number the button represents
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void digitButtonPressed(int i) throws IOException;

	/**
	 * Report when the new game button has been pressed
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void newGamePressed() throws IOException;

	/**
	 * Report that a user quit
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void quit() throws IOException;
}
