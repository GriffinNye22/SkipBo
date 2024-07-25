/**
 * com.client.settings.SettingsView
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Creates the User Interface for the Settings Page and implements the 
 * SettingsPresenter.Display interface
 */

package com.client.settings;

import java.util.Iterator;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SettingsView implements HasWidgets, SettingsPresenter.Display {

	//Container
	private VerticalPanel settingsPanel = new VerticalPanel();
  	
		//Game Title
		private Label header;
  
		//Input Table
  	private FlexTable input;
  		private ListBox playerList;
  		private ListBox cpuList;
  		private ListBox stockList;
  		private ListBox pointList;
  
  	//Name Table
    private FlexTable names;
  		private TextBox txtPlayerName;
  		private Button play;
  
  /**
   * Constructs the SettingsView object
   */
  public SettingsView() {
  	
  	//Initialize container
  	settingsPanel = new VerticalPanel();
  	
  	//Initialize Game Title
  	header = new Label("Skip-Bo");
  	
  	//Initialize Input Table & its components
  	input = new FlexTable();
  	playerList = new ListBox();
  	cpuList = new ListBox();
  	stockList = new ListBox();
  	pointList = new ListBox();
  	
  	//Initialize Names Table & its components
  	names = new FlexTable();
  	txtPlayerName = new TextBox();
  	
  	//Initialize Play Button
  	play = new Button("Play");
  	
  	//Populate and assemble the view
  	loadView();
  	
  }//end constructor
	
  /**
   * Populates and assembles the view
   */
  private void loadView() {

    //Populate the Lists
    for(int i = 2; i < 5; i++) {
      playerList.addItem(Integer.toString(i));
    }//end for

    for(int i = 0; i < 2; i++) {
      cpuList.addItem(Integer.toString(i));
    }//end for
    
    for(int i = 5; i <= 30; i += 5) {
      stockList.addItem(Integer.toString(i));
    }//end for
   
    for(int i = 50; i <= 500; i += 50) {
      pointList.addItem(Integer.toString(i));
    }//end for

    //Set Listboxes as Drop-down boxes
    playerList.setVisibleItemCount(1);
    cpuList.setVisibleItemCount(1);
    stockList.setVisibleItemCount(1);
    pointList.setVisibleItemCount(1);

    //Assemble Input table
    input.setWidget(0,0, new Label("Settings:"));
    input.setWidget(1,0, new Label("Number of Players:"));
    input.setWidget(1,1, playerList);
    input.setWidget(2,0, new Label("Number of CPU Players:"));
    input.setWidget(2,1, cpuList);
    input.setWidget(3,0, new Label("Cards in Stockpile:"));
    input.setWidget(3,1, stockList);
    input.setWidget(4,0, new Label("Point Goal:"));
    input.setWidget(4,1, pointList);
    input.getFlexCellFormatter().setColSpan(0,0,2);
    
    //Assemble Settings Panel
    settingsPanel.add(header);
    settingsPanel.add(input);
    
    //Style Settings Panel and its contents
    settingsPanel.addStyleName("settingsPanel");
    settingsPanel.addStyleName("center");
    
    //Style the header
    header.setStyleName("header");
    
    //Style the Input Table
    input.addStyleName("inputTable");
    input.addStyleName("center");
    
    //Style the Input Table cells
    input.getCellFormatter().addStyleName(0,0,"textCenter");
    input.getCellFormatter().addStyleName(0, 0, "settingsHeader");
    input.getCellFormatter().addStyleName(1,0,"textRight");
    input.getCellFormatter().addStyleName(1,0,"settings");
    input.getCellFormatter().addStyleName(2,0,"textRight");
    input.getCellFormatter().addStyleName(2,0,"settings");
    input.getCellFormatter().addStyleName(3,0,"textRight");
    input.getCellFormatter().addStyleName(3,0,"settings");  
    input.getCellFormatter().addStyleName(4,0,"textRight");
    input.getCellFormatter().addStyleName(4,0,"settings");
    
    //Add Player Name Label, TextBox, and Play button to Names table
    names.setWidget(0, 0, new Label("Player Name:"));
    names.setWidget(0, 1, txtPlayerName);
    names.setWidget(names.getRowCount(), 0, play);
    names.getFlexCellFormatter().setColSpan(names.getRowCount()-1, 0, 2);
    
    //Set Focus on Textbox
    txtPlayerName.setFocus(true);
  
    //Style Name Table and its contents
    names.addStyleName("center");
    names.addStyleName("namesTable");
    play.addStyleDependentName("play");
    play.addStyleName("center");
    
    //Add names table to settingsPanel
    settingsPanel.add(names);
    
  }//end loadSettings

	
	/**
	 * Adds a widget to the view.
	 * @param w The widget to be added
	 */
	@Override
	public void add(Widget w) {
		settingsPanel.add(w);
	}//end add
  
	/**
	 * Returns this view as a Widget.
	 * @return Widget The settingsPanel widget
	 */
	@Override
	public Widget asWidget() {
		return settingsPanel;
	}//end asWidget
	
	/**
	 * Clears the view.
	 */
	@Override
	public void clear() {
		settingsPanel.clear();
	}//end clear
	
	/**
	 * Returns the cpuList ListBox from this view. 
	 * @return ListBox The cpuList ListBox
	 */
	public ListBox getCPUListBox() {
		return cpuList;
	}//end getCPUListBox
	
	/**
	 * Returns the selected value of cpuList.
	 * @return String The selected value
	 */
	@Override
	public String getCPUValue() {
		return cpuList.getSelectedValue();
	}//end getCPUValue
	
	/**
   * Returns the Play Button from this view.
   * @return HasClickHandlers The Play Button
   */
	@Override
	public HasClickHandlers getPlayButton() {
		return play;
	}//end getPlayButton
	
	/**
	 * Returns the playerList ListBox from this view.
	 * @return ListBox The playerList ListBox
	 */
	public ListBox getPlayerListBox() {
		return playerList;
	}//end getPlayerListBox
	
  /**
   * Returns the Host client's name
   * @return String The Host client's name
   */
	@Override
	public String getPlayerName() {
  	return txtPlayerName.getValue();
	}//end getPlayerNames
  
	/**
	 * Returns the selected value of playerList.
	 * @return String The selected value
	 */
	@Override
	public String getPlayerValue() {
		return playerList.getSelectedValue();
	}//end getPlayerValue
  
	/**
	 * Returns the selected value of pointList.
	 * @return String The selected value
	 */
	@Override
	public String getPointValue() {
		return pointList.getSelectedValue();
	}//end getPointValue
	
	/**
	 * Returns the selected value of stockList.
	 * @return String The selected value
	 */
	@Override
	public String getStockValue() {
		return stockList.getSelectedValue();
	}//end getStockValue
	
	/**
	 * Returns the instance of this view.
	 * @return SettingsView "this" instance
	 */
	@Override
	public SettingsView getViewInstance() {
		return this;
	}//end getView
  
  /**
	 * Returns an iterator for the Widgets in the view
	 * @return Iterator The iterator for the widgets in the view.
	 */
	@Override
	public Iterator<Widget> iterator() {
		return settingsPanel.iterator();
	}//end iterator
 
	/**
	 * Removes a widget from the view.
	 * @param w The widget to be removed
	 * @return Boolean True: Widget removed successfully False: Failed to remove widget
	 */
	@Override
	public boolean remove(Widget w) {
		return settingsPanel.remove(w);
	}//end remove
	
}//end SettingsView
