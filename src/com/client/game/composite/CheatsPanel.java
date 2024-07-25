/**
 * com.client.game.composite.CheatsPanel.java
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Composite Class for the Cheats DialogBox
 */

package com.client.game.composite;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CheatsPanel extends Composite {
	//Create DialogBox and display error message  
	final DialogBox cheats;
	private VerticalPanel cheatsPanel;
	private HorizontalPanel buttonPanel;
	final TextBox cmdBox;
	private Button issue;
	private Button exit;
	
	/**
	 * Constructs the CheatsPanel
	 */
	public CheatsPanel() {
		//Intialize Members
		cheats = new DialogBox();
		cheatsPanel = new VerticalPanel();
		buttonPanel = new HorizontalPanel();
		cmdBox = new TextBox();
		issue = new Button("Issue Command");
		exit = new Button("Exit");
		
		//Assemble buttonPanel
		buttonPanel.add(issue);
		buttonPanel.add(exit);
	            
		//Assemble cheatsPanel
		cheatsPanel.add( new Label("Command List:") );
		cheatsPanel.add( new Label("add <Player Name> <hand|discard|stock> <idx (for hand and discard> <1-13 (13 = WILD)>"));
		cheatsPanel.add( new Label(" ") );
		cheatsPanel.add( new Label ("Enter Command:") );
		cheatsPanel.add(cmdBox);
		cheatsPanel.add(buttonPanel);
	       
		//Center Button
		cheatsPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		//Set Focus on Textbox
		cmdBox.setFocus(true);
		
		//Assemble DialogBox      
		cheats.setWidget(cheatsPanel);
	}//end constructor

	/**
	 * Returns this Panel as a widget
	 * @return DialogBox The cheats DialogBox
	 */
	public DialogBox asWidget() {
		return cheats;
	}//end getInstance
	
	/**
	 * Returns the Command entered by the user.
	 * @return String The command entered by the user
	 */
	public String getCommand() {
		return cmdBox.getValue();
	}//end getCommand
	
	/**
	 * Retrieves the Exit button
	 * @return The Exit button
	 */
	public Button getExitButton() {
		return exit;
	}//end getExitButton
	
	/**
	 * Retrieves the Issue Command button
	 * @return Button The Issue Command button
	 */
	public Button getIssueButton() {
		return issue;
	}//end getIssueButton

}//end CheatPanel
