/**
 * com.client.game.composite.CurrentPlayerPanel
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Composite Class for the CurrentPlayerPanel
 */

package com.client.game.composite;

import java.util.ArrayList;

import com.client.game.event.CardDragEnterHandler;
import com.client.game.event.CardDragHandler;
import com.client.game.event.CardDragLeaveHandler;
import com.client.game.event.CardDragOverHandler;
import com.client.game.event.CardDropHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CurrentPlayerPanel extends Composite {

	//Container
  private VerticalPanel currentPlayer;
  	
  	//Name Label
  	private Label name;
  	
  	//Hand and Stock Panel
  	private HorizontalPanel handStockPanel;
  		private HorizontalPanel handPanel;
      private ArrayList<Image> hand;
      private Image stock;
      
    //Discard Panel
    private HorizontalPanel discardPanel;
      private ArrayList<AbsolutePanel> discardPiles;
	
      
  /**
   * Constructs the CurrentPlayerPanel
   */
  public CurrentPlayerPanel() {
  	
  	//Initialize Container
  	currentPlayer = new VerticalPanel();
  	
  	//Initialize & Style Name Label
  	name = new Label("Current Player");
  	name.setStyleName("textCenter");
  	
  	//Initialize HandStock Panel, Hand Panel, & hand ArrayList
  	handStockPanel = new HorizontalPanel();
  	handPanel = new HorizontalPanel();
  	hand = new ArrayList<Image>();
  	
  	//Initialize Discard Panel & ArrayList of Pile Panels
  	discardPanel = new HorizontalPanel();
  	discardPiles = new ArrayList<AbsolutePanel>();
  	
  	//Load & Assemble CurrentPlayerPanel components
  	loadHand();
  	loadHandStock();
  	loadDiscard();
  	
  	//Assemble CurrentPlayer
  	currentPlayer.add(name);
  	currentPlayer.add(handStockPanel);
  	currentPlayer.add(discardPanel);
  	
    //Wrap currentPlayer in this Composite Class
  	initWidget(currentPlayer);
  	
  }//end constructor
  
  /**
   * Populates Hand ArrayList with placeholder card images and assembles the HandPanel
   */
  private void loadHand() {
  	
  	//Create 5 card images, set their IDs and add to HandPanel
  	for(int i = 0; i < 5; i++) {
    	hand.add(new Image("images/BACK.png"));
    	hand.get(i).getElement().setId( "hand" + Integer.toString(i) );
    	handPanel.add(hand.get(i));
    }//end for
  	
  }//end loadHand
  
  /**
   * Initializes Stock with placeholder card image and assembles the HandStockPanel
   */
  private void loadHandStock() {
  	
  	//Set Stock Image & ID
  	stock = new Image("images/BACK.png");
    stock.getElement().setId("stock");
    stock.addDragStartHandler(new CardDragHandler() );
  	
    //Assemble HandStockPanel
    handStockPanel.add(handPanel);
    handStockPanel.add(stock);
    
    //Style HandStockPanel & Stock
    stock.setStyleName("stockSouth");
    handStockPanel.setStyleName("center");
    
  }//end loadHandStock
  
  /**
   * Populates DiscardPiles ArrayList and assembles the DiscardPanel
   */
  private void loadDiscard() {
  	Image img;
  	
  	
  	//Populate Discard Pile ArrayList and Assemble DiscardPanel
    for(int i = 0; i < 4; i++) {
      
    	//Create New Pile Panel & Set placeholder image
      discardPiles.add(new AbsolutePanel());
      discardPiles.get(i).add(new Image("images/BACK.png"));
      
      //Set Discard Pile ID
      img = (Image) discardPiles.get(i).getWidget(0);
      img.getElement().setId( "discard" + Integer.toString(i) );
      
      //Add Pile to DiscardPanel
      discardPanel.add( discardPiles.get(i) );
    }//end for
    
    //Style DiscardPanel
    discardPanel.setStyleName("center");
  	
  }//end loadDiscard

	/**
	 * Returns the Discard Pile with the provided pile number.
	 * @param pileNum The discard pile number
	 * @return AbsolutePanel The discard pile.
	 */
	public AbsolutePanel getDiscardPile(int pileNum) {
		return discardPiles.get(pileNum);
	}//end getDiscard
  
  /**
   * Returns the Hand.
   * @return ArrayList The hand
   */
	public ArrayList<Image> getHand() {
		return hand;
	}//end getHand
	
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

}//end CurrentPlayerPanel
