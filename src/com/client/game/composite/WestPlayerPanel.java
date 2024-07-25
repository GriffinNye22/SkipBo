/**
 * com.client.game.composite.WestPlayerPanel
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Composite Class for West Player Panel
 */

package com.client.game.composite;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WestPlayerPanel extends Composite {

	//Container
  private HorizontalPanel westPlayer;
  	
  	//Name Label
  	private Label name;
    
  	//Hand and Stock Panel
  	private VerticalPanel handStockPanel;
  		private VerticalPanel handPanel;
      private ArrayList<Image> hand;
      private Image stock;
      
    //Discard Panel
    private VerticalPanel discardPanel;
      private ArrayList<Image> discardPiles;
	
      
  /**
   * Constructs the WestPlayerPanel
   */
  public WestPlayerPanel() {
  	
  	//Initialize Container
  	westPlayer = new HorizontalPanel();
  	
  	//Initialize & Style Name Label
  	name = new Label("West Player");
  	name.setStyleName("textCenter");
  	
  	//Initialize HandStock Panel, Hand Panel, & hand ArrayList
  	handStockPanel = new VerticalPanel();
  	handPanel = new VerticalPanel();
  	hand = new ArrayList<Image>();
  	
  	//Initialize Discard Panel & ArrayList of Pile Panels
  	discardPanel = new VerticalPanel();
  	discardPiles = new ArrayList<Image>();
  	//	discardPiles = new ArrayList<AbsolutePanel>();
  	
  	//Load & Assemble NortPlayerPanel components
  	loadHandStock();
  	loadDiscard();
  	
  	//Assemble WestPlayer
  	westPlayer.add(discardPanel);
  	westPlayer.add(handStockPanel);
  	westPlayer.add(name);
  	
  	//Position WestPlayer and its contents
  	westPlayer.setStyleName("left");
  	westPlayer.setCellVerticalAlignment(discardPanel, HasVerticalAlignment.ALIGN_MIDDLE);
  	westPlayer.setCellVerticalAlignment(handStockPanel, HasVerticalAlignment.ALIGN_MIDDLE);
  	westPlayer.setCellVerticalAlignment(name, HasVerticalAlignment.ALIGN_MIDDLE);
  	
    //Wrap westPlayer in this Composite Class
  	initWidget(westPlayer);
  	
  }//end constructor

  /**
   * Initializes Hand & Stock with placeholder card images and assembles HandStockPanel
   */
  private void loadHandStock() {
  	
  	//Populate Hand
  	for(int i = 0; i < 5; i++) {
    	hand.add(new Image("images/BACK_LEFT.png"));
    	handPanel.add(hand.get(i));
    }//end for
  	
  	//Set Stock Image
  	stock = new Image("images/BACK_LEFT.png");
  	
    //Assemble HandStockPanel
    handStockPanel.add(handPanel);
    handStockPanel.add(stock);
    
    //Style HandStockPanel & Stock
    stock.setStyleName("stockWest");
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
    	discardPiles.get(i).setUrl("images/BACK_LEFT.png");
    	// discardPiles.add(new AbsolutePanel());
      // discardPiles.get(i).add(new Image("images/BACK_LEFT.png"));
      
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
  
}//end WestPlayerPanel