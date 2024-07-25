/**
 * com.client.game.GameView
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Creates the User Interface for the Game Page and implements the GamePresenter.Display interface
 */

package com.client.game;

import java.util.ArrayList;
import java.util.Iterator;

import com.client.game.composite.CenterPanel;
import com.client.game.composite.CheatsPanel;
import com.client.game.composite.CurrentPlayerPanel;
import com.client.game.composite.EastPlayerPanel;
import com.client.game.composite.NorthPanel;
import com.client.game.composite.NorthPlayerPanel;
import com.client.SkipBoException;
import com.client.game.composite.StatsPanel;
import com.client.game.composite.WestPlayerPanel;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.server.model.BuildPile;
import com.server.model.Card;
import com.server.model.Computer;
import com.server.model.DiscardPile;
import com.server.model.Game;
import com.server.model.Hand;
import com.server.model.Player;
import com.server.model.StockPile;

public class GameView implements HasWidgets, GamePresenter.Display {
	
	//Container 
  private DockPanel layout;
    
  	//ArrayList of Player Composites
  	private ArrayList<Composite> playerLayouts;
    
    private NorthPanel topPanel;
    private CenterPanel buildPanel;
    private CheatsPanel cheatsPanel;
    private StatsPanel statsPanel;

  /**
   * Constructor for GameView
   * @param players The number of players
   */
  public GameView(int players) {
  	
  	//Initialize Components
  	layout = new DockPanel();
  	playerLayouts = new ArrayList<Composite>(4);
  	topPanel = new NorthPanel(players);
  	buildPanel = new CenterPanel();
  	cheatsPanel = new CheatsPanel();
  	statsPanel = new StatsPanel();
  	
  	//Add Composite Instances to playerLayouts based on number of Players
  	switch(players) {
  	case 4:
  		playerLayouts.add(0, new EastPlayerPanel() );
  	case 3:
  		playerLayouts.add(0, new WestPlayerPanel() );
  	case 2:
  		playerLayouts.add(0, new NorthPlayerPanel() );
  		playerLayouts.add(0, new CurrentPlayerPanel() );
  	}//end switch
  	
//  	if (players == 3) {
//    	playerLayouts.get(3).getElement().getStyle().setProperty("opacity", "0");
//    }//end if
  	
  	playerLayouts.trimToSize();
  	
  	//Load the Game Layout
  	loadView();
  }//end constructor
  
  /**
   * Assembles the Game Layout
   */
  public void loadView() {
    
    //Stores direction  
    DockPanel.DockLayoutConstant direction = DockPanel.CENTER;
    
    //Add NorthPanel to Game Layout
    layout.add(topPanel, DockPanel.NORTH);
    
    //Loads appropriate number of player layouts 
    for(int i = 0; i < playerLayouts.size(); i++) {
      
      //Set appropriate direction for the playerLayout
      switch (i) {
        case 0:
        	  direction = DockPanel.SOUTH;
        	  break;
        case 1:
          	direction = DockPanel.NORTH;
        	  break;
        case 2:
          	direction = DockPanel.WEST;
          	break;
        case 3:
        	  direction = DockPanel.EAST;
        	  break;
      }//end switch
      
      //Add the playerLayout to the Game Layout
      layout.add(playerLayouts.get(i), direction);
    }//end for
    
    //Add CenterPanel to the GameLayout
    layout.add(buildPanel, DockPanel.CENTER);
    
    //Align the children panels 
    layout.setCellHorizontalAlignment(playerLayouts.get(0), HasHorizontalAlignment.ALIGN_CENTER);
    layout.setCellHorizontalAlignment(playerLayouts.get(1), HasHorizontalAlignment.ALIGN_CENTER);
    layout.setCellVerticalAlignment(playerLayouts.get(1), HasVerticalAlignment.ALIGN_BOTTOM);
    layout.setCellVerticalAlignment(buildPanel, HasVerticalAlignment.ALIGN_MIDDLE);
    
    //Style Game layout
    layout.setStyleName("layout");
  }//end loadGameLayout   
  
  /** DISPLAY INTERFACE METHODS **/
  
  /**
   * Adds a widget to the view.
   * @param w The widget to be added
   */
	@Override
	public void add(Widget w) {
		layout.add(w);
	}//end add
	
	/**
	 * Returns this view as a Widget.
	 * @return Widget The layout widget
	 */
	@Override
	public Widget asWidget() {
		return layout;
	}//end asWidget

	/**
	 * Clears the view.
	 */
	@Override
	public void clear() {
		layout.clear();
	}//end clear

  /**
   * Displays the Cheats panel
   */
  public void displayCheats() {
  	cheatsPanel.asWidget().center();
  }//end displayCheats
	
  /**
   * Creates Dialog Box to display message (most commonly Exceptions)
   * @param msg the message string
   */
	@Override
  public void displayMessage(String msg) {
    
    //Create DialogBox and display error message  
    final DialogBox error = new DialogBox();
    VerticalPanel errorPanel = new VerticalPanel();
    Label errorLabel = new Label(msg);
    Button ok = new Button("OK", new ClickHandler() {
          
      public void onClick(ClickEvent event) {
        error.hide();
      }//end onClick
                
    });//end ClickHandler
            
    errorPanel.add(errorLabel);
    errorPanel.add(ok);
            
    errorPanel.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);
            
    error.setWidget(errorPanel);
    error.center();
  }//end displayMessage
	
	 /**
   * Displays the Rules
   */
  public void displayRules() {
  	final DialogBox rulesBox = new DialogBox();
  	VerticalPanel dialogPanel = new VerticalPanel(); 
  	TabPanel rulesPanel = new TabPanel();
  	Button close = new Button("Close");
  	Button cheats = new Button("Cheats");
  	
  	//Tab Titles
  	String objectTitle = "Object:";
  	String beginTitle = "Beginning Play:";
  	String buildTitle = "Build Piles:";
  	String discardTitle = "Discard Piles:";
  	String playTitle = "Playing Skip-Bo:";
  	String winTitle = "Winning the Game:";
  	String UITitle = "Interaction:";
  	
  	//Tab Page Contents
  	Label objectLabel = new Label("Be the first player to play every card in your "
  			                           + "Stock pile, by playing all of your cards in "
  			                           + "numerical order, 1 to 12.  ");
  	Label beginLabel = new Label("The dealer deals the number of cards specified"
  			                           + "in the pre-game settings to each player to "
  			                           + "form his/her stockpile. These cards are dealt "
  			                           + "face down. Once all Stock piles have been "
  			                           + "dealt, each player flips the top card of "
  			                           + "their stockpile right side up and places "
  			                           + "it on top of the pile. The dealer then deals "
  			                           + "5 cards to each player to form the hand.");
  	Label buildLabel = new Label("Build piles are where players build 1-12 sequences "
  																+ "and can only be started with a 1 or a Wild card. "
  																+ "Once a pile has completed the 1-12 sequence, "
  																+ "it is removed from play to begin a new build "
  																+ "pile.");
  	Label discardLabel = new Label("Each player may form sequences on any of their "
  			                            + "four discard piles. There is no limit to "
  			                            + "the number of cards in each pile, nor is "
  			                            + "there a restriction on the order. The top "
  			                            + "card of each of your discard piles is available "
  			                            + "for play.");
  	Label playLabel = new Label("Draw from the deck to bring your hand up to five "
  			                         + "cards (unnecessary for the first rotation). "
  			                         + "You can use a 1 or a Wild card to begin one "
  			                         + "of the four build piles in the center. You may "
  			                         + "continue to play cards from your hand onto the "
  			                         + "build piles (1-12 in sequence only). If you "
  			                         + "play all five cards from your hand, draw back "
  			                         + "up to 5 (this process can be repeated if needed)."
  			                         + " You may also play the top card from your stockpile "
  			                         + "onto a build pile, and can continue to play "
  			                         + "from the stockpile as long as the play is "
  			                         + "legal. Remember, you win by running out of "
  			                         + "your stockpile, so play from there when you"
  			                         + " can. Your turn ends when you can't or refuse"
  			                         + " to make a play. Discard one card from your "
  			                         + "hand onto one of your four discard piles. You "
  			                         + "may play the top card of any of your discard piles "
  			                         + "on any turn after the first. Play continues "
  			                         + "clockwise.");
  	Label winLabel = new Label("A player wins when he/she reaches the point total "
  			                        + "specified in the pre-game settings. The winner "
  			                        + "of each round scores 25 pts for winning the "
  			                        + "round and five additional points per card "
  			                        + "remaining in each of the other player's "
  			                        + "stockpiles.");
  	Label UILabel = new Label("If you're stuck on how to play the game once you've"
  			                       + "gotten started, simply drag and drop the cards "
  			                       + "wherever you want to place them. Do remember though,"
  			                       + "discarding a card will end your turn.");
  	
    //Add Close ClickHandler
  	close.addClickHandler(new ClickHandler() {
  		
  		/**
  		 * Closes the Rules Dialog Box on Click event
  		 * @param event the event fired on clicking the button
  		 */
  		public void onClick(ClickEvent event) {
  			rulesBox.hide();
  		}//end onClick
  		
  	});//end close ClickHandler
  	
  	//Add Display Cheats ClickHandler
  	cheats.addClickHandler(new ClickHandler() {
  		
  		/**
  		 * Displays the cheats Dialog Box on click event
  		 * @param event the event fired on clicking the button
  		 */
  		public void onClick(ClickEvent event) {
  			displayCheats();
  		}//end onClick
  		
  	});//end cheats ClickHandler
  	
  	//Make cheats Visible on Hover
  	cheats.addMouseOverHandler(new MouseOverHandler() {
  		
  		/**
  		 * Makes the Button visible when hovered over by the mouse
  		 * @param event the event fired on hovering the mouse on the button
  		 */
  		public void onMouseOver(MouseOverEvent event) {
  			( (Button) event.getSource() ).getElement().getStyle().setProperty("opacity", "100");
  		}//end onMouseOver
  		
  	});//end cheats MouseOverHandler
  	
  	//Make cheats Invisible on Un-hover
  	cheats.addMouseOutHandler(new MouseOutHandler() {
  		
  		/**
  		 * Makes the Button invisible after un-hovering the mouse
  		 * @param event the event fired on un-hovering the mouse on the button
  		 */
  		public void onMouseOut(MouseOutEvent event) {
  			( (Button) event.getSource() ).getElement().getStyle().setProperty("opacity", "0");
  		}//end onMouseOut
  		
  	});//end cheats MouseOutHandler	
  
  	cheats.getElement().getStyle().setProperty("opacity", "0");
  	
  	//Assemble rulesPanel
  	rulesPanel.add(objectLabel, objectTitle);
  	rulesPanel.add(beginLabel, beginTitle);
  	rulesPanel.add(buildLabel, buildTitle);
  	rulesPanel.add(discardLabel, discardTitle);
  	rulesPanel.add(playLabel, playTitle);
  	rulesPanel.add(winLabel, winTitle);
  	rulesPanel.add(UILabel, UITitle);
  	rulesPanel.selectTab(0);
  	
  	//Assemble DialogBox Contents
  	dialogPanel.add(rulesPanel);
  	dialogPanel.add(cheats);
  	dialogPanel.add(close);
  	
    //Center the Buttons
  	dialogPanel.setCellHorizontalAlignment(close, HasHorizontalAlignment.ALIGN_RIGHT);
  	dialogPanel.setCellHorizontalAlignment(cheats, HasHorizontalAlignment.ALIGN_LEFT);
 
  	//Assemble Dialog Box
  	rulesBox.setWidget(dialogPanel); 
  	
  	//Center and Show Dialog Box
  	rulesBox.center();
  	rulesBox.setWidth("50%");
  	rulesBox.show();
  }//end displayRules
	
	/**
	 * Returns the CenterPanel containing the BuildPiles
	 * @return CenterPanel The CenterPanel containing the BuildPiles
	 */
	@Override
	public CenterPanel getBuildPanel() {
		return buildPanel;
	}//end getBuildPanel
	
	/**
	 * Returns the CheatsPanel
	 * @return CheatsPanel The CheatsPanel
	 */
	@Override
	public CheatsPanel getCheatsPanel() {
		return cheatsPanel;
	}//end getCheats
	
	/**
	 * Returns the CurrentPlayerPanel.
	 * @return CurrentPlayerPanel The CurrentPlayerPanel
	 */
	@Override
	public CurrentPlayerPanel getCurrentPlayer() {
		return (CurrentPlayerPanel) playerLayouts.get(0);
	}//end CurrentPlayerPanel
	
	/**
	 * Returns the EastPlayerPanel.
	 * @return EastPlayerPanel The EastPlayerPanel
	 */
	@Override
	public EastPlayerPanel getEastPlayer() {
		return (EastPlayerPanel) playerLayouts.get(3);
	}//end getEastPlayer
	
	/**
	 * Returns the NorthPlayerPanel.
	 * @return NorthPlayerPanel The NorthPlayePanel
	 */
	@Override
	public NorthPlayerPanel getNorthPlayer() {
		return (NorthPlayerPanel) playerLayouts.get(1);
	}//end getNorthPlayer

	/**
	 * Returns the StatsPanel
	 * @return StatsPanel The StatsPanel
	 */
	@Override
	public StatsPanel getStatsPanel() {
		return statsPanel;
	}//end getStatsPanel
	
	/**
	 * Returns the NorthPanel
	 * @return NorthPanel The NorthPanel
	 */
	@Override
	public NorthPanel getTopPanel() {
		return topPanel;
	}//end getNorthPanel
	
	/**
	 * Returns the WestPlayerPanel.
	 * @return WestPlayerPanel The WestPlayerPanel
	 */
	@Override
	public WestPlayerPanel getWestPlayer() {
		return (WestPlayerPanel) playerLayouts.get(2);
	}//end getWestPlayer
	
	/**
	 * Returns the instance of this view.
	 * @return GameView "this" instance
	 */
	@Override
	public GameView getViewInstance() {
		return this;
	}//end getViewInstance
	
	/**
	 * Returns an iterator for the Widgets in the view
	 */
	@Override
	public Iterator<Widget> iterator() {
		return layout.iterator();
	}//end iterator

	/**
	 * Removes a widget from the view.
	 * @param w The widget to be removed
	 * @return Boolean True: Widget removed successfully False: Failed to remove widget
	 */
	@Override
	public boolean remove(Widget w) {
		return layout.remove(w);
	}//end remove
	
}//end GameView
