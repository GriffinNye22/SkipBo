/**
 * com.client.game.composite.StatsPanel
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Composite Class for the Stats Panel
 */

package com.client.game.composite;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatsPanel extends Composite {
	
	final DialogBox statsBox;
	private VerticalPanel statsPanel;
	private FlexTable statsTable;
	private Label headerLabel;
	private Label gamesLabel;
	private Label minLabel;
	private Label maxLabel;
	private Label avgLabel;
	private Button close;
	
	public StatsPanel() {
		//Initialize Members
		statsBox = new DialogBox();
		statsPanel = new VerticalPanel();
		statsTable = new FlexTable();
		headerLabel = new Label("Stats:");
		gamesLabel = new Label("X");
		minLabel = new Label("X");
		maxLabel = new Label("X");
		avgLabel = new Label("X");
		
		close = new Button("close", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				statsBox.hide();
			}//end onClick
			
		});//end ClickHandler

		//Assemble statsTable
		statsTable.setWidget(0, 0, new Label("Games Played:") );
		statsTable.setWidget(0, 1, gamesLabel);
		statsTable.setWidget(1, 0, new Label("Min Turns:") );
		statsTable.setWidget(1, 1, minLabel);
		statsTable.setWidget(2, 0, new Label("Max Turns:") );
		statsTable.setWidget(2, 1, maxLabel);
		statsTable.setWidget(3, 0, new Label("Avg Turns:") );
		statsTable.setWidget(3, 1, avgLabel);
		
		//Style StatsTable
		statsTable.getColumnFormatter().addStyleName(0, "textRight");
		statsTable.getColumnFormatter().addStyleName(1, "textCenter");
		
		//Assemble statsPanel
		statsPanel.add(headerLabel);
		statsPanel.add(statsTable);
		statsPanel.add(close);
		
		//Center Elements
		statsPanel.setCellHorizontalAlignment(headerLabel, HasHorizontalAlignment.ALIGN_CENTER);
		statsPanel.setCellHorizontalAlignment(close, HasHorizontalAlignment.ALIGN_CENTER);
		
		//Assemble DialogBox
		statsBox.setWidget(statsPanel);
	}//end StatsPanel
	
	/**
	 * Returns this Panel as a widget
	 * @return DialogBox The Stats DialogBox
	 */
	public DialogBox asWidget() {
		return statsBox;
	}//end asWidget
	
	/**
	 * Updates the Stats in the table using the data in the provided ArrayList
	 * @param stats The provided ArrayList
	 */
	public void updateStats(ArrayList<Float> stats) {
		
		for(int i = 0; i < stats.size(); i++) {
			
			switch(i) {
			case 0:
				gamesLabel.setText( Float.toString(stats.get(i)) );
			case 1:
				minLabel.setText( Float.toString(stats.get(i)) );
			case 2:
				minLabel.setText( Float.toString(stats.get(i)) );
			case 3:
				maxLabel.setText( Float.toString(stats.get(i)) );
			}//end switch

		}//end for
		
	}//end updateStats
	
	
}//end StatsPanel
