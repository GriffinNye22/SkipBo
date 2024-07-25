/**
 * com.client.game.composite.NorthPanel
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Composite Class for the North Panel containing Scoreboard and Rules Button
 */

package com.client.game.composite;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.server.model.Player;

public class NorthPanel extends Composite {

	private HorizontalPanel north;
  private FlexTable scoreboard;
  private Button rules;
  
  /**
   * Constructs the NorthPanel
   * @param numPlayers the number of players
   */
  public NorthPanel(int numPlayers) {
  	
  	//Initialize Container & its components
  	north = new HorizontalPanel();
  	scoreboard = new FlexTable();
  	rules = new Button("?");
  
  	//Assemble the ScoreBoard
  	loadScoreboard(numPlayers);
  	
  	//Assemble North Panel
  	north.add(scoreboard);
  	north.add(rules);
  	
  	//Style North Panel and Rules
  	north.setWidth("100%");
    north.setCellHorizontalAlignment(rules, HasHorizontalAlignment.ALIGN_RIGHT);
    rules.setStyleName("rulesButton");
    
    //Wrap north in this Composite Class
  	initWidget(north);
  	
  }//end constructor
	
  /**
   * Assembles and Populates the Scoreboard with Player names and placeholder data
   * @param playerNames ArrayList of Player Names (For creating the scoreboard).
   */
	private void loadScoreboard( int numPlayers ) {
		//int playerRows = playerNames.size();
		
		//Style Scoreboard
    scoreboard.setStyleName("scoreboard");
    
    //Add Title and Column Headings
    scoreboard.setWidget(0,1, new Label("Points:"));
    scoreboard.setWidget(0,2, new Label("Stock:"));

    //Style Title and Column Headings
    scoreboard.getRowFormatter().addStyleName(0, "textCenter");
    scoreboard.getRowFormatter().addStyleName(0, "scoreHeader");
 
    //Add and Style Row for each Player
    for(int i = 0; i < numPlayers; i++) {
      
      //Add Row
      scoreboard.setWidget(i+1, 0, new Label( "Player" + i + ":" ) );
      scoreboard.setWidget(i+1, 1, new Label("0") );
      scoreboard.setWidget(i+1, 2, new Label("X/X") );
      
      //Style Player Column
      scoreboard.getRowFormatter().addStyleName(i+1, "textRight");
      scoreboard.getFlexCellFormatter().setStyleName(i+1, 0, "scoreHeader");
      
    }//end for
    
    //Add Current Turn Row
    String turn = "Current Turn: 0";
    scoreboard.setWidget( numPlayers + 1, 0, new Label(turn) );
    scoreboard.getFlexCellFormatter().setColSpan( numPlayers + 1, 0, 3); 
   
    //Style Current Turn Row
    scoreboard.getRowFormatter().addStyleName( numPlayers + 1, "textRight");
    scoreboard.getRowFormatter().addStyleName( numPlayers + 1, "scoreHeader");
	}//end loadScoreboard
	
	/**
	 * Retrieves the Rules Button.
	 * @return Button The Rules Button
	 */
	public Button getRulesButton() {
		return rules;
	}//end getRulesButton
	
	/**
	 * Retrieves the Scoreboard
	 * @return FlexTable The scoreboard
	 */
	public FlexTable getScoreboard() {
		return scoreboard;
	}//end getScoreboard
  
}//end NorthPanel
