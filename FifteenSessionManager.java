//******************************************************************************
//
// File:    FifteenView.java
// Package: ---
// Unit:    Class FifteenView
//
//******************************************************************************

import java.util.*;
import java.io.IOException;

/**
 * Class FifteenSessionManager manages sessions the the server creates for Fifteen games
 *
 * @author  Marko Galesic
 * @version 21-Apr-2014
 */
public class FifteenSessionManager implements FifteenViewListener
{
	private Player currentPlayer1;
	private FifteenModel currentModel;
	
	/*
	 * FifteenSessionManager constructor
	 */
	public FifteenSessionManager()
	{
	}

	
	/**
	 * Handles when a player wants to join a session
	 *
	 * @param playerName player's name that wants to join
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public synchronized void join(FifteenViewProxy proxy, String name) throws IOException
	{	
		if (currentPlayer1 == null)
		{
			currentPlayer1 = new Player(name);
			currentModel = new FifteenModel(currentPlayer1);
			currentModel.addModelListener (proxy);
			proxy.setViewListener (currentModel);
		}
		else
		{
			currentModel.addSecondPlayer(new Player(name));
			currentPlayer1 = null;
		}
	}

	/**
	 * Report when a digit button has been pressed
	 *
	 * @param i number the button represents
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void digitButtonPressed(int i) throws IOException{}

	/**
	 * Report when the new game button has been pressed
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void newGamePressed() throws IOException{}

	/**
	 * Report that a user quit
	 * @exception IOException Thrown if an I/O error occurred.
	 */
	public void quit() throws IOException{}
}
