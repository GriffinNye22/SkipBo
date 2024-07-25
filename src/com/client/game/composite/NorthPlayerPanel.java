/**
 * com.client.game.composite.NorthPlayerPanel
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Composite Class for North Player Panel
 */

package com.client.game.composite;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NorthPlayerPanel extends Composite {

	//Container
  private VerticalPanel northPlayer;
  	
  	//Name Label
  	private Label name;
    
  	//Hand and Stock Panel
  	private HorizontalPanel handStockPanel;
  		private HorizontalPanel handPanel;
      private ArrayList<Image> hand;
      private Image stock;
      
    //Discard Panel
    private HorizontalPanel discardPanel;
      private ArrayList<Image> discardPiles;
	
      
  /**
   * Constructs the NorthPlayerPanel
   */
  public NorthPlayerPanel() {
  	
  	//Initialize Container
  	northPlayer = new VerticalPanel();
  	
  	//Initialize & Style Name Label
  	name = new Label("North Player");
  	name.setStyleName("textCenter");
  	
  	//Initialize HandStock Panel, Hand Panel, & hand ArrayList
  	handStockPanel = new HorizontalPanel();
  	handPanel = new HorizontalPanel();
  	hand = new ArrayList<Image>();
  	
  	//Initialize Discard Panel & ArrayList of Pile Panels
  	discardPanel = new HorizontalPanel();
  	discardPiles = new ArrayList<Image>();
//  	discardPiles = new ArrayList<AbsolutePanel>();
  	
  	//Load & Assemble NortPlayerPanel components
  	loadHandStock();
  	loadDiscard();
  	
  	//Assemble NorthPlayer
  	northPlayer.add(discardPanel);
  	northPlayer.add(handStockPanel);
  	northPlayer.add(name);
  	
    //Wrap northPlayer in this Composite Class
  	initWidget(northPlayer);
  	
  }//end constructor
  
  /**
   * Initializes Hand & Stock with placeholder card images and assembles HandStockPanel
   */
  private void loadHandStock() {
  	
  	//Populate Hand
  	for(int i = 0; i < 5; i++) {
    	hand.add(new Image("images/BACK.png"));
    	handPanel.add(hand.get(i));
    }//end for
  	
  	//Set Stock Image
  	stock = new Image("images/BACK.png");
  	
    //Assemble HandStockPanel
    handStockPanel.add(stock);
    handStockPanel.add(handPanel);
    
    //Style HandStockPanel & Stock
    stock.setStyleName("stockNorth");
    handStockPanel.setStyleName("center");
    
  }//end loadHandStock
  
  /**
   * Populates DiscardPiles ArrayList and assembles the DiscardPanel
   */
  private void loadDiscard() {
  	
  	//Populate Discard Pile ArrayList and Assemble DiscardPanel
    for(int i = 0; i < 4; i++) {
      
    	//Create New Pile Panel & Set placeholder image
    	discardPiles.add(new Image() );
    	discardPiles.get(i).setUrl("images/BACK.png");
//      discardPiles.add(new AbsolutePanel());
//      discardPiles.get(i).add(new Image("images/BACK.png"));
      
      //Add Pile to DiscardPanel
      discardPanel.add( discardPiles.get(i) );
      
    }//end for
    
    //Style DiscardPanel
    discardPanel.setStyleName("center");
  	
  }//end loadDiscard

  /**
   * Returns the Hand.
   * @return ArrayList The hand
   */
	public ArrayList<Image> getHand() {
		return hand;
	}//end getHand
	
	/**
	 * Returns the Discard Pile with the provided pile number.
	 * @param pileNum The discard pile number
	 * @return Image The discard pile.
	 */
	public Image getDiscardPile(int pileNum) {
		return discardPiles.get(pileNum);
	}//end getDiscard
	
	/**
	 * Returns the Stock.
	 * @return Image The Stock
	 */
	public Image getStock() {
		return stock;
	}//end getStock
	
	/**
	 * Sets the Text for the name Label.
	 * @param playerName The Player's name
	 */
	public void setName(String playerName) {
		name.setText(playerName);
	}//end getName
  
}//end NorthPlayerPanel