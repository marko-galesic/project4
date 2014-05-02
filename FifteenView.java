//******************************************************************************
//
// File:    FifteenView.java
// Package: ---
// Unit:    Class FifteenView
//
//******************************************************************************

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * Class FifteenView provides the user interface for the Fifteen network game.
 *
 * @author  Alan Kaminsky
 * @author  Marko Galesic
 * @version 16-Mar-2014
 */
public class FifteenView extends JFrame implements FifteenModelListener
{
	// Constants for the interface
	private static final int GAP = 10;
	private static final int COLS = 12;
	
	// Hidden member variables
	private FifteenViewListener _viewListener;
	private FifteenGame _game;
	private Player _myPlayer;
	private Player _theirPlayer;

	/**
	 * Class DigitButton provides a button labeled with a digit.
	 */
	private class DigitButton extends JButton
	{
		private int digit;
		private boolean enabled = true;
		private boolean available = true;

		/**
		 * Construct a new digit button.
		 *
		 * @param  digit  Digit for the button label.
		 */
		public DigitButton (int digit)
		{
			super ("" + digit);
			this.digit = digit;
			addActionListener (new ActionListener()
				{
					public void actionPerformed (ActionEvent e)
					{
						onDigitButton (DigitButton.this.digit);
					}
				});
		}

		/**
		 * Make this digit button available or unavailable. When available, the
		 * button displays its digit. When not available, the button is blank.
		 *
		 * @param  available  True if available, false if not.
		 */
		public void available (boolean available)
		{
			this.available = available;
			setText (available ? "" + digit : " ");
			updateButton();
		}

		/**
		 * Enable or disable this digit button. When enabled and available,
		 * clicking the button performs the appropriate action. Otherwise,
		 * clicking the button has no effect.
		 *
		 * @param  enabled  True if enabled, false if not.
		 */
		public void setEnabled (boolean enabled)
		{
			this.enabled = enabled;
			updateButton();
		}

		/**
		 * Update this digit button's label and enabled state.
		 */
		private void updateButton()
		{
			super.setEnabled (available && enabled);
		}
	}

	/**
	 * User interface widgets.
	 */
	private String myName;
	private DigitButton[] digitButton;
	private JTextField myScoreField;
	private JTextField theirScoreField;
	private JTextField winnerField;
	private JButton newGameButton;

	/**
	 * Construct a new FifteenView object.
	 *
	 * @param  myName  Player's name.
	 */
	public FifteenView (String myName)
	{
		super ("Fifteen -- " + myName);
		this.myName = myName;
		
		_myPlayer = new Player(myName);
		_game = new FifteenGame();
		JPanel panel = new JPanel();
		add (panel);
		panel.setLayout (new BoxLayout (panel, BoxLayout.X_AXIS));
		panel.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
		JPanel panel_a = new JPanel();
		panel.add (panel_a);
		panel_a.setLayout (new BoxLayout (panel_a, BoxLayout.Y_AXIS));
		digitButton = new DigitButton [9];
		for (int i = 0; i < 9; ++ i)
		{
			panel_a.add (digitButton[i] = new DigitButton (i + 1));
			digitButton[i].setAlignmentX (0.5f);
			digitButton[i].setEnabled (false);
			digitButton[i].setMinimumSize (digitButton[i].getPreferredSize());
			digitButton[i].setMaximumSize (digitButton[i].getPreferredSize());
			digitButton[i].setSize (digitButton[i].getPreferredSize());
		}
		panel.add (Box.createHorizontalStrut (GAP));
		JPanel panel_b = new JPanel();
		panel.add (panel_b);
		panel_b.setLayout (new BoxLayout (panel_b, BoxLayout.Y_AXIS));
		panel_b.add (myScoreField = new JTextField (COLS));
		myScoreField.setAlignmentX (0.5f);
		myScoreField.setEditable (false);
		myScoreField.setMaximumSize (myScoreField.getPreferredSize());
		myScoreField.setText(myName);
		panel_b.add (Box.createRigidArea (new Dimension (0, GAP)));
		panel_b.add (theirScoreField = new JTextField (COLS));
		theirScoreField.setAlignmentX (0.5f);
		theirScoreField.setEditable (false);
		theirScoreField.setMaximumSize (theirScoreField.getPreferredSize());
		theirScoreField.setText("Waiting for partner");
		panel_b.add (Box.createRigidArea (new Dimension (0, GAP)));
		panel_b.add (winnerField = new JTextField (COLS));
		winnerField.setAlignmentX (0.5f);
		winnerField.setEditable (false);
		winnerField.setMaximumSize (winnerField.getPreferredSize());
		panel_b.add (Box.createVerticalGlue());
		panel_b.add (newGameButton = new JButton ("New Game"));
		newGameButton.setAlignmentX (0.5f);
		newGameButton.setMaximumSize (newGameButton.getPreferredSize());
		newGameButton.setEnabled (false);
		newGameButton.addActionListener (new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				onNewGameButton();
			}
		});
		addWindowListener (new WindowAdapter()
		{
			public void windowClosing (WindowEvent e)
			{
				onClose();
			}
		});

		pack();
		
		this.setVisible(true);
	}

	/**
	 * Set the view listener for this Fifteen view.
	 *
	 * @param  viewListener  Fifteen view listener.
	 */
	public void setViewListener (FifteenViewListener viewListener)
	{
		_viewListener = viewListener;
	}

	/**
	 * Take action when a digit button is clicked.
	 *
	 * @param  digit  Digit that was clicked.
	 */
	private void onDigitButton (int digit)
	{
		try{_viewListener.digitButtonPressed(digit);}
		catch(IOException e){}
	}

	/**
	 * Take action when the New Game button is clicked.
	 */
	private void onNewGameButton()
	{
		try
		{
			_viewListener.newGamePressed();
			winnerField.setText("");
		}
		catch(IOException e){}
	}

	/**
	 * Take action when the Fifteen window is closing.
	 */
	private void onClose()
	{
		try{_viewListener.quit();}
		catch(IOException e){}
		System.exit (0);
	}

	/**
	 * Report the id of the local player
	 *
	 * @param id id of the local player
	 */
	public void localId(int id)
	{
		_myPlayer.setId(id);
	}

	/**
	 * Report a players id and name
	 *
	 * @param id a players id
	 * @param String playerName
	 */
	public void playerIdAndName(int id, String playerName)
	{
		// Check if we've been reported the remote player, assign if yes
		if (_myPlayer.getId() != id)
		{
			_theirPlayer = new Player(playerName, id);
			newGameButton.setEnabled(true);
		}
	}

	/**
	 * Report button states
	 *
	 * @param buttonStates 	an array that represents whether
	 *			or not buttons 1-9 are depressed or
	 *			not
	 */
	public void buttonStates(char[] buttonStates)
	{
		_game.setButtons(buttonStates);
		for (int button = 0; button < 9; ++ button)
		{
			digitButton[button].available (_game.getButtonStatus(button));
		}
	}

	/**
	 * Report score for an id
	 *
	 * @param id id of the player the score report is for
	 * @param score score of the player
	 */
	public void scoreReport(int id, int score)
	{
		// Clear the textfield if score report is 0
		if (score == 0)
		{
			winnerField.setText("");
		}

		// Update my text field
		if (_myPlayer.getId() == id)
		{
			_myPlayer.setSum(score);
			myScoreField.setText(_myPlayer.getName() + " = " + _myPlayer.getSum());
		}
		// Update their text field
		else
		{
			_theirPlayer.setSum(score);
			theirScoreField.setText(_theirPlayer.getName() + " = " + _theirPlayer.getSum());
		}
	}

	/**
	 * Report the turn id
	 *
	 * @param id id of the player whose turn it is
	 */
	public void turnId(int id)
	{
		// If it is my turn to go, enable buttons that are available
		if (_myPlayer.getId() == id)
		{
			for (int button = 0; button < 9; ++ button)
			{
				digitButton[button].setEnabled (true);
			}
		}
		else
		{
			for (int button = 0; button < 9; ++ button)
			{
				digitButton[button].setEnabled (false);
			}
		}
	}

	/**
	 * Report a win.
	 *
	 * @param id id of the player who won
	 */
	public void winId(int id)
	{
		if (id == _myPlayer.getId())
		{
			// Update winner field with text for my player
			winnerField.setText(_myPlayer.getName() + " wins!");
			
		}
		else if (id == _theirPlayer.getId())
		{
			// Update winner field with text for their player
			winnerField.setText(_theirPlayer.getName() + " wins!");
		}
		else
		{
			// Update winner field with the text for draw
			winnerField.setText("Draw!");
		}
	}

	/**
	 * Send a message to quit
	 */
	public void quit(){ System.exit(0); }
}
