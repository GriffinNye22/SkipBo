/**
 * com.client.game.GamePresenter
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Handles communication between the Model and GameView. Handles user interaction
 * for GameView and updates the UI for these interactions.
 */

package com.client.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.client.game.composite.CenterPanel;
import com.client.game.composite.CheatsPanel;
import com.client.game.composite.CurrentPlayerPanel;
import com.client.game.composite.EastPlayerPanel;
import com.client.game.composite.NorthPanel;
import com.client.game.composite.NorthPlayerPanel;
import com.client.game.composite.StatsPanel;
import com.client.game.composite.WestPlayerPanel;
import com.client.game.event.CardDragEnterHandler;
import com.client.game.event.CardDragHandler;
import com.client.game.event.CardDragLeaveHandler;
import com.client.game.event.CardDragOverHandler;
import com.client.game.event.CardDropHandler;
import com.client.game.event.DiscardEvent;
import com.client.game.event.DiscardEventHandler;
import com.client.game.event.EventType;
import com.client.game.event.PlayFromDiscardEvent;
import com.client.game.event.PlayFromDiscardEventHandler;
import com.client.game.event.PlayFromHandEvent;
import com.client.game.event.PlayFromHandEventHandler;
import com.client.game.event.PlayFromStockEvent;
import com.client.game.event.PlayFromStockEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class GamePresenter {

	public interface Display {
	  CheatsPanel getCheatsPanel();
	  CenterPanel getBuildPanel();
	  CurrentPlayerPanel getCurrentPlayer();
	  EastPlayerPanel getEastPlayer();
	  NorthPlayerPanel getNorthPlayer();
	  NorthPanel getTopPanel();
	  StatsPanel getStatsPanel();
	  WestPlayerPanel getWestPlayer();
	  
	  GameView getViewInstance();
	  
	  void displayMessage(String msg);
	  void displayRules();
	  
	  Widget asWidget();
	}//end Display
	
	private ArrayList<Float> statsList;
	private Boolean myTurn;
	private final Display view;
	private final HandlerManager eventBus;
	private HandlerRegistration[] dragDropHandlers;
	private GameServiceAsync gameService;
	private int currentPlayer;
	private int numPlayers;
	private int thisPlayer;
	private String[] playerNames;
	
	//Timer for Waiting for Game Events
	private Timer checkEventTimer = new Timer() {

		@Override
		public void run() {
			//Callback for checkForEvent
	  	AsyncCallback<Boolean> checkEventCallback = new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					view.displayMessage( caught.getMessage() );
				}//end onFailure

				@Override
				public void onSuccess(Boolean eventFired) {
					if(eventFired) {
						getGameEvent();
					}//end if
				}//end onSuccess
	  	};//end checkEventCallback
	  	
	  	//Check for fired GameEvent
	  	gameService.checkForEvent(checkEventCallback);
		}//end run
	};//end Timer
	 
	
	/**
	 * Constructs the Game Presenter
	 * @param eventBus The EventBus for the application
	 * @param numPlayers The number of players
	 * @param thisPlayer index of this player
	 */
	public GamePresenter(HandlerManager eventBus, int numPlayers, int thisPlayer) {
		
		//Instantiate Members
		this.dragDropHandlers = new HandlerRegistration[41];
		this.eventBus = eventBus;
		this.gameService = GWT.create(GameService.class);
		this.numPlayers = numPlayers;
		this.playerNames = new String[numPlayers];
		this.statsList = new ArrayList<Float>();
		this.thisPlayer = thisPlayer;
		this.view = new GameView(numPlayers);
	
		//Establish Connection to GameService
		ServiceDefTarget destTarget = (ServiceDefTarget) this.gameService;
		destTarget.setServiceEntryPoint(GWT.getModuleBaseURL() + "game");
		
		//GameService retrieves Game instance from ConnectionService
		initGame();
		
		//Load the Layout
		loadLayoutData();

		//Get the Current Player and determine route of action for turn
		getCurrentPlayer();
		
	}//end constructor

	/**
	 * Adds the Drag and Drop Handlers to the Current Player Hand, Stock, Discard and the BuildPiles
	 * after GameView load.
	 */
	private void addInitialDragDropHandlers() {
		ArrayList<Image> BuildPiles = view.getBuildPanel().getBuildPiles();
		AbsolutePanel DiscardPile;
		ArrayList<Image> Hand = view.getCurrentPlayer().getHand();
		Image curr;
		
		//Add Handlers to BuildPiles
		for(int i = 0; i < BuildPiles.size(); i++) {
			curr = BuildPiles.get(i);
			
			dragDropHandlers[i*4] = curr.addDragEnterHandler(new CardDragEnterHandler() );
			dragDropHandlers[1 + i*4] = curr.addDragOverHandler(new CardDragOverHandler() );
			dragDropHandlers[2 + i*4] = curr.addDragLeaveHandler(new CardDragLeaveHandler() );
			dragDropHandlers[3 + i*4] = curr.addDropHandler(new CardDropHandler(view, eventBus, thisPlayer, gameService) );
		}//end for
		
		//Add Handler to DiscardPiles
		for(int i = 0; i < 4; i++) {
			DiscardPile = view.getCurrentPlayer().getDiscardPile(i);
			curr = (Image) DiscardPile.getWidget(0);
			
			dragDropHandlers[16 + i*5] = curr.addDragStartHandler(new CardDragHandler() );
			dragDropHandlers[17 + i*5] = curr.addDragEnterHandler(new CardDragEnterHandler() );
			dragDropHandlers[18 + i*5] = curr.addDragOverHandler(new CardDragOverHandler() );
			dragDropHandlers[19 + i*5] = curr.addDragLeaveHandler(new CardDragLeaveHandler() );
			dragDropHandlers[20 + i*5] = curr.addDropHandler(new CardDropHandler(view, eventBus, thisPlayer, gameService) );
		}//end for
		
		//Add Handler to Cards in Hand
		for(int i = 0; i < Hand.size(); i++) {
			curr = Hand.get(i);
			
			//Add Drag Handler to Card
			dragDropHandlers[36+i] = curr.addDragStartHandler(new CardDragHandler() );
		}//end for
		
	}//end addInitialDragDropHandlers
	
	/**
	 * Binds Events from the View and DropHandler to this Presenter
	 */
	public void bindEvents() {
		
		//Display Rules on Clicking Rules Button
		view.getTopPanel().getRulesButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//Display the Rules
				view.displayRules();
				
			}//end onClick
		});//end ClickHandler
		
		//Execute Cheats Command on Clicking "Issue Command" Button
		view.getCheatsPanel().getIssueButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String cmd = view.getCheatsPanel().getCommand();
				
				//Execute the entered Cheat command
				executeCheat(cmd);
			}//end onClick
		});//end ClickHandler
		
		//Hide CheatsPanel and Update UI on exit of CheatsPanel
		view.getCheatsPanel().getExitButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				view.getCheatsPanel().asWidget().hide();
//				updateHand();
//				updateDiscard();
//				updatePlayers();
			}//end onClick
		});//end ClickHandler
		
//		eventBus.addHandler(CPUTurnEvent.TYPE, new CPUTurnEventHandler() {
//
//			@Override
//			public void onCPUTurn(CPUTurnEvent event) {
//				final int currPlayerIdx = SBGame.getCurrentPlayer();
//		  	final Computer currCPU = (Computer) SBGame.getPlayer( currPlayerIdx );
//		  	
//		    //Timer to wait between UI updates
//			  Timer updateTimer = new Timer() {
//				
//			  	private Boolean moveMade;
//			  	
//			  	/**
//			  	 * Determines CPU Move and calls appropriate UI Updates
//			  	 */
//			  	@Override
//			  	public void run() {
//			  		
//			  		//Make CPU move and store whether turn should continue
//			  		moveMade = currCPU.decideMove( SBGame.getBuild() );
//					
//			  		//Handle CPU's Move Decision
//			  		switch( currCPU.getMove() ) {
//			  		case DISCARDMOVE:
//			  			eventBus.fireEvent(new PlayFromDiscardEvent() );
//			  		case HANDMOVE:
//			  			eventBus.fireEvent(new PlayFromHandEvent() );
//			  			break;
//			  		case STOCKMOVE:
//			  			eventBus.fireEvent(new PlayFromStockEvent() );
//			  			break;
//			  		case DISCARD:
//			  			eventBus.fireEvent(new DiscardEvent() );
//			  			break;
//			  		}//end switch
//		  		
//			  		
//			  		//If CPU discarded, Disable the timer
//			  		if(!moveMade) {
//			  			cancel();
//			  			return;
//			  		}//end if
//			  		
//			  	}//end run
//				
//			  };//end Timer
//		  	
//		  	//Wait 3 seconds between CPU moves
//		  	updateTimer.scheduleRepeating(3000);
//			}//end onCPUTurn
//		});//end CPUTurnEventHandler
		
		//Handle Playing from the Hand
		eventBus.addHandler(PlayFromHandEvent.TYPE, new PlayFromHandEventHandler() {

			@Override
			public void onPlayFromHand(PlayFromHandEvent event) {
			  //Check if this Player's hand needs to be refilled
				refillHand();
				
				//Update this Player's Hand
				getHandData();

				//Update the BuildPiles
				getBuildData();
			}//end onPlayFromHand
		});//end PlayFromHandEventHandler
		
		//Handle Playing from the Stock
		eventBus.addHandler(PlayFromStockEvent.TYPE, new PlayFromStockEventHandler() {

			@Override
			public void onPlayFromStock(PlayFromStockEvent event) {
		  	
				//Check Player has Won and perform StockMove Events
				checkPlayerWon();
				
			}//end onPlayFromStock
		});//end PlayFromStockEventHandler
		
		//Handle Playing from the Discard Piles
		eventBus.addHandler(PlayFromDiscardEvent.TYPE, new PlayFromDiscardEventHandler() {

			@Override
			public void onPlayFromDiscard(PlayFromDiscardEvent event) {
				//Update Current Player Discards
				getDiscardData();
			
				//Update the Build Piles
				getBuildData();
			}//end onPlayFromDiscard
		});//end PlayFromDiscardEventHandler
		
		//Handle Discards
		eventBus.addHandler(DiscardEvent.TYPE, new DiscardEventHandler() {

			@Override
			public void onDiscard(DiscardEvent event) {
				//Update this Player's Hand & Discard Piles
				getHandDiscardData();
				
				//Update the Scoreboard
				getScoreboardData();
				
				//Get the Current Player
				getCurrentPlayer();
			}//end onDiscard
		});//end DiscardEventHandler

	}//end bindEvents
	
	/**
	 * Checks for Cleared BuildPiles, updating BuildPiles on the UI if necessary
	 */
	public void checkCompletedBuildPiles() {
		AsyncCallback<String[]> completeCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(String[] buildData) {
				
				//Update the BuildPiles on the UI if BuildPile was cleared
				if(buildData != null) {
					updateBuild(buildData);
				}//end if
				
			}//end onSuccess
		};//end refillCallback

		//Check if BuildPiles 
		gameService.checkCompletedBuildPiles(completeCallback);
	}//end checkCompletedBuildPiles
	
	/**
	 * Checks if a player has won the game or the round and executes the appropriate events
	 */
	private void checkPlayerWon() {
		AsyncCallback<Integer> winCallback = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage(caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(Integer winResult) {
				
				switch ( winResult ) {
		  	//Game Won Case
		  	case 1:
		  	
		  		//Transmit player won Game
		  	  getWinningPlayer();
		  	  
		  	  //Update the scoreboard
		  	  getScoreboardData();
		  	  
		  	  break;
		  	//Round Won Case 
		  	case 0:
		  	  
		  		//Transmit player won Round
		  		getWinningPlayer();
		  	  
		  	  //Get the new current Player
		  	  getCurrentPlayer();
		  	  
		  	  //Load layout
		  	  loadLayoutData();
		  		
		  	  //update the Discard Piles
		  	  getDiscardData();
		  	  
		  	  //update all Players in Layout
		  	  //updatePlayers();
		  	
		  	//No Win Case (Contents also executed on case of Won Round)
		  	case -1:
		  		
		  	  //Update this Player's Hand and Stock
		      getHandStockData();
		    	
		      ///Update the BuildPiles
		      getBuildData();
		    	
		      //Update the Scoreboard
		      getScoreboardData();
		  	}//end switch
			}//end onSuccess
		};//end winCallback
		
		//check Player Won
		gameService.checkPlayerWon(winCallback);
	}//end checkPlayerWon
	
	/**
	 * Presents the GameView by placing it in the provided container.
	 * @param container The container for the view. (RootPanel in this case)
	 */
	public void display(HasWidgets container) {
		
		//Clear container
		container.clear();
		
		//Add view to container
		container.add( view.asWidget() );
		
		//Bind the view events to the controller
		bindEvents();
		
	}//end display
	
	/**
	 * Executes cheat command and updates the UI
	 * @param cmd The cheat command
	 */
	private void executeCheat(String cmd) {
		
		//Callback for executeCheat
		AsyncCallback<Void> cheatCallback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(Void result) {
				
				//Retrieve this Player's data and update the UI
				getPlayerData();
			}//end onSuccess
		};//end cheatCallback
		
		//Execute the Cheat command 
		gameService.executeCheat(cmd, cheatCallback);
		
	}//end executeCheat
	
	/**
	 * Retrieves the BuildPiles data and updates the UI
	 */
	public void getBuildData() {
		
		//Callback for getBuildPiles
		AsyncCallback<String[]> buildCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(String[] buildData) {
				updateBuild(buildData);
			}//end onSucess
		};//end buildCallback
		
		//Retrieve build data and update on success
		gameService.getBuildPiles(buildCallback);
	}//end getBuildData
	
	/**
	 * Retrieves the current Player
	 * @return int Index of the currentPlayer
	 */
	private void getCurrentPlayer() {
		
		//Callback for getCurrentPlayer of GameService
		AsyncCallback<Integer> currPlayerCallback = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(Integer currPlayer) {
				//Handle the New Turn
				view.displayMessage(playerNames[currPlayer] + "'s Turn");
				handleNewTurn(currPlayer);
			}//end onSuccess
		};//end currPlayercallback
		
		//Get the Current Player
		gameService.getCurrentPlayer(currPlayerCallback);
	}//end getCurrentPlayer
	
	/**
	 * Retrieves this Player's Discard data and updates the UI
	 */
	public void getDiscardData() {
		
		//Callback for getPlayerDiscards
		AsyncCallback<String[]> discCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage(caught.getMessage());
			}//end onFailure

			@Override
			public void onSuccess(String[] discardData) {
				updateDiscard( parsePlayerDiscardData(discardData) );
			}//end onSuccess
		};//end discCallback
		
		//Retrieve Discard data
		gameService.getPlayerDiscards(thisPlayer, discCallback);
	}//end getDiscardData() 
	
	/**
	 * Continuously makes requests for Game Events and updates the UI accordingly
	 */
	public void getGameEvent() {
		
		//Callback for getLastEvent from the GameService
		AsyncCallback<EventType> eventCallback = new AsyncCallback<EventType>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(EventType event) {
				
				switch(event) {
				case ROUNDWIN:
				case GAMEWIN:
					getWinningPlayer();
					break;
				case DISCARD:
					getCurrentPlayer();
				case DISCARDMOVE:
				case HANDMOVE:
				case STOCKMOVE:		
					//getBuildData();
					//getScoreboardData();
					checkPlayerWon();
					updatePlayers();
					break;
				}//end switch
				
			}//end onSuccess
		};//end eventCallback

		//Get the last Remote Event fired
		gameService.getLastEvent(eventCallback);
	}//end getGameEvents
	
	/**
	 * Retrieves this Player's Hand Data and updates the UI
	 */
	public void getHandData() {
		
		//Callback for getPlayerHand
		AsyncCallback<String> handCallback = new AsyncCallback<String>() {
	
			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure
	
			@Override
			public void onSuccess(String handData) {
				String[] hand = parsePlayerHandData(handData);
				updateHand(hand);
			}//end onSuccess
		};//end refillCallback
		
		//Get Player Hand Data
		gameService.getPlayerHand(thisPlayer, handCallback);
	}//end getHandData()
	
	/**
	 * Retrieves Hand and Discard Data and updates the UI
	 */
	public void getHandDiscardData() {
		
		//Callback for getPlayerHandDiscards
		AsyncCallback<String[]> handDiscCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage(caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(String[] result) {
				String hand = result[0];
				String[] disc = Arrays.copyOfRange(result, 1, result.length);
				
				//Update this Player's Hand & Discard Piles
				updateHand(parsePlayerHandData(hand) );
				updateDiscard(parsePlayerDiscardData(disc));
			}//end onSuccess
		};//end handDiscCallback
		
		
		//Get Player Hand & Discard Data
		gameService.getPlayerHandDiscards(thisPlayer, handDiscCallback);
	}//end getHandDiscardData
	
	/**
	 * Retrieves Hand and Stock Data and updates the UI
	 */
	public void getHandStockData() {
		
		//Callback for getPlayerHandStock
		AsyncCallback<String[]> handStockCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage("Error retrieving Player data");
			}//onFailure

			@Override
			public void onSuccess(String[] playerData) {
				String hand = playerData[0];
				String stock = playerData[1];
				String[] parsedHand = parsePlayerHandData(hand);
			
				
				//Update this Player's Hand & Stock on the UI
				updateHand(parsedHand);
				updateStock(stock);
			}//end onSuccess
		};//end AsyncCallback
		
		//Get Player Hand & Stock Data
		gameService.getPlayerHandStock(this.thisPlayer, handStockCallback);
	}//end getHandStockData
	
	private void getNorthPlayerData(int playerIdx) {
		//Callback for getOtherPlayer
		AsyncCallback<String[]> northCallback = new AsyncCallback<String[]>() {
	
			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage("Error retrieving NorthPlayer data");
			}//end onFailure
			
			@Override
			public void onSuccess(String[] northData) {
				updateNorthPlayer( Integer.parseInt(northData[0]), northData[1], 
													 parsePlayerDiscardData( Arrays.copyOfRange(northData, 2, northData.length) ) );
			}//end onSuccess
		};//end AsyncCallback
		
		//Retrieve North Player Data
		gameService.getOtherPlayer( playerIdx, northCallback );
	}//end getNorthPlayerData
	
	/**
	 * Returns a list of the associated Player indexes of the other clients
	 * @return int[] The list of associated Player indexes of the other clients
	 */
	private int[] getOtherPlayerIndexes() {
		//Array for indexes of other players
	  int[] otherPlayers = new int[numPlayers - 1]; 
	  
	  //Populate Playerlist indexes for the other players
	  for(int i = 0; i < otherPlayers.length; i++) {
	  	int idx = thisPlayer + (i+1);
		 
	  	//If index exceeds last index, adjust accordingly
	  	if(idx > otherPlayers.length) {
	  		idx -= otherPlayers.length + 1;
	  	}//end if
		 
	  	//Store index in other players index array
	  	otherPlayers[i] = idx;
	  }//end for	
	  
	  return otherPlayers;
	}//end getOtherPlayerIndexes
	
	/**
	 * Retrieves this Player's Player Data and updates the UI
	 */
	public void getPlayerData() {
		
		//Callback for getPlayer
		AsyncCallback<String[]> playerCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(String[] playerData) {
				
				//Update this Player on the UI
				updateThisPlayer(playerData);
			}//end onSuccess
		};//end playerCallback
		
		//Get Player data
		gameService.getPlayer(thisPlayer, playerCallback);
	}//end getPlayerData
	
	/**
	 * Returns the view from this presenter
	 * @return Display The view
	 */
	public Display getView() {
		return view;
	}//end getView
	
	/**
	 * Returns the statsList
	 * @return ArrayList the statsList
	 */
	public ArrayList<Float> getStats() {
		return statsList;
	}//end getStats
	
	public void getScoreboardData() {
		//Callback for getScoreboardData
		AsyncCallback<String[]> scoreCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage("Error retrieving Scoreboard data");
			}//end onFailure
			
			@Override
			public void onSuccess(String[] scoreData) {
				updateScoreboard(scoreData);
			}//end onSuccess
		};//end AsyncCallback
		
		//Update the Scoreboard
		gameService.getScoreboardData(scoreCallback);
	}//end getScoreboardData
	
	/**
	 * Displays the player has won
	 */
	private void getWinningPlayer() {
		//Callback for getWinningPlayerMessage
		AsyncCallback<String> winCallback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure
			
			@Override
			public void onSuccess(String winMessage) {
				view.displayMessage(winMessage);
			}//end onSuccess
		};//end AsyncCallback
		
		//Update the Scoreboard
		gameService.getWinningPlayerMessage(winCallback);
	}//end getWinningPlayer
	
	/**
	 * Determines if it is the clients turn and handles appropriately
	 * @param currentPlayer Index of the Player taking their turn
	 */
	private void handleNewTurn(int currentPlayer) {
		
		//Determine route of action
		//It is this Player's turn
		if(thisPlayer == currentPlayer) {
			addInitialDragDropHandlers();
			checkEventTimer.cancel();
			getHandData();
			
			//Update Current Turn
			getScoreboardData();
		//This Player just took their turn
		} else if(thisPlayer == currentPlayer - 1 || (thisPlayer == numPlayers - 1 && currentPlayer == 0) ){
			removeDragDropHandlers();
			
//			//Update Current Turn
//			getScoreboardData();
			checkEventTimer.scheduleRepeating(1000);
		}//end if
		
	}//end handleNewTurn
	
	/**
	 * Labels the players on the client's interface
	 * @param names The list of names of all Players in the Game
	 * @param otherPlayersIdx The list of Player indexes associated with each client
	 */
	private void labelPlayers(String[] names, int[] otherPlayersIdx) {
	  //Store the Player name list
		playerNames = names;
		
		
		switch(numPlayers) {
	  case 2:
	  	view.getCurrentPlayer().setName( names[thisPlayer] );
	  	view.getNorthPlayer().setName( names[ otherPlayersIdx[0] ] );
	  	break;
	  case 3:
	  	view.getCurrentPlayer().setName( names[thisPlayer] );
	  	view.getNorthPlayer().setName( names[ otherPlayersIdx[0] ] );
	  	view.getWestPlayer().setName(  names[ otherPlayersIdx[1] ] );
	  	break;
	  case 4:
	  	view.getCurrentPlayer().setName( names[thisPlayer] );
	  	view.getNorthPlayer().setName(  names[ otherPlayersIdx[0] ] );
	  	view.getWestPlayer().setName( names[ otherPlayersIdx[1] ] );
	  	view.getEastPlayer().setName(  names[ otherPlayersIdx[2] ] );
	  	break;
	  }//end switch
		
	}//end labelPlayers
	
	/**
	 * Calls GameService to retrieve instance of Game from ConnectionService
	 */
	private void initGame() {
		//Callback for initGame
		AsyncCallback<Void> gameCallback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage(caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(Void result) {
			}//end onSuccess
		};//end gameWinCallback
		
		//Transmit player won game
		gameService.initGame(gameCallback);
	}//end initGame
	
	/**
	 * Retrieves Layout Data from GameService for loading initial layout
	 */
	public void loadLayoutData() {
		
		//Get Scoreboard Data and update Scoreboard
		getScoreboardData();
		
		//Get Hand & Stock Data and update them
		getHandStockData();
		
		//Callback for getPlayerNames
		AsyncCallback<String[]> nameCallback = new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage("Error retrieving Player names");
			}//onFailure

			@Override
			public void onSuccess(String[] playerNames) {
				
				//Set Player Names and Update other Players in the view
				labelPlayers(playerNames, getOtherPlayerIndexes() );
				updatePlayers();
			}//end onSuccess
		};//end AsyncCallback
		
		//Get Player Names
		gameService.getPlayerNames(nameCallback);
	}//end loadLayoutData

	/**
	 * Parses the Player's Discard pile Data
	 * @param discard The array of Discard Pile data
	 * @return String[][] Returns the Discard Pile data
	 */
	public String[][] parsePlayerDiscardData(String[] discard) {
		String[][] parsedDiscard = new String[4][];
		
		//Parse Discard Pile strings
		for(int i = 0; i < discard.length; i++) {
			parsedDiscard[i] = discard[i].split(">");
		}//end for
		
		return parsedDiscard;
	}//end parsePlayerDiscardData
	
	/**
	 * Parses this Player's Hand Data
	 * @param hand The Hand string to be parsed
	 * @return String[] The parsed Hand data
	 */
	public String[] parsePlayerHandData(String hand) {
		return hand.split(" ");
	}//end parsePlayerHandData
	
	/**
	 * Checks if the player's hand needs to be refilled, refilling it, if it does
	 */
	public void refillHand() {
		AsyncCallback<String> refillCallback = new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				view.displayMessage( caught.getMessage() );
			}//end onFailure
	
			@Override
			public void onSuccess(String handData) {
				
				if(handData != null) {
					String[] hand = parsePlayerHandData(handData);
					updateHand(hand);
				}//end if
				
			}//end onSuccess
		};//end refillCallback
		
		//Check if hand needs to be refilled
		gameService.checkHandRefill(refillCallback);
	}//end refillHand
	
	/**
	 * Removes all Drag and Drop Handlers from the Client's interface
	 */
	private void removeDragDropHandlers() {
		
		if(dragDropHandlers[0] != null) {
			for(int i = 0; i < dragDropHandlers.length; i++) {
				dragDropHandlers[i].removeHandler();
			}//end for
		}//end if
		
	}//end removeDragDropHandlers
	
	/**
	 * Updates the BuildPile UI
	 * @param build Array of build pile values
	 */
	private void updateBuild(String[] build) {
		String currPile;
		Image currUIPile;
		
		//Loop through each BuildPile, updating its image and adding drop handler
    for(int i = 0; i < 4; i++) {
    	currPile = build[i];
      currUIPile = view.getBuildPanel().getBuildPiles().get(i);
      
      //Update BuildPile image if it is not empty
      if( !currPile.equals("X") ) {
      	currUIPile.setUrl("images/" + currPile + ".png");   
      } else {
        currUIPile.setUrl("images/BACK.png");
      }//end if
      
    }//end for
	}//end updateBuild
	
	/**
	 * Updates the Player's Discard Pile UI
	 * @param discardPiles 2d Array containing discardPile data. 
	 */
	private void updateDiscard(String[][] discardPiles) {
		AbsolutePanel currPile;
		String[] pile;
		Image img;
		String firstCardRank;
	
		
		//Loop through each DiscardPile
		for(int i = 0; i < 4; i++) {
			
			//Retrieve the UI element for Discard Pile and rank of the top card 
			currPile = view.getCurrentPlayer().getDiscardPile(i);
			firstCardRank = currPile.getWidget(0).getElement().getAttribute("src");
	      
			//Store DiscardPile object
			pile = discardPiles[i];
			
			//Clear pile if not empty or in case where last card removed
			if ( !pile[0].equals("X") || (pile.length == 1 && !( firstCardRank.contains("BACK") ) ) ) {
				currPile.clear();
	    }//end if
	      	
			//Add placeholder to pile if last card in pile was played
			//pile.length == 1 && currPile.getWidgetCount() == 0 && 
			if ( pile[0].equals("X") && currPile.getWidgetCount() == 0 ) {
				img = new Image("images/BACK.png");
				
				//Add Handlers for pile
				img.getElement().setId( "discard" + String.valueOf(i) );
				dragDropHandlers[17 + i*5] = img.addDragEnterHandler(new CardDragEnterHandler() );
				dragDropHandlers[18 + i*5] = img.addDragLeaveHandler(new CardDragLeaveHandler() );
				dragDropHandlers[19 + i*5] = img.addDragOverHandler(new CardDragOverHandler() );
				dragDropHandlers[20 + i*5] = img.addDropHandler(new CardDropHandler(view, eventBus, thisPlayer, gameService) );
					
				//Add images to Pile panel
				currPile.add(img);
				
				//Add Cards to pile if pile is not an empty pile
	    } else if ( !pile[0].equals("X") ){
	      
				//Add cards to the pile
				for(int j = 0; j < pile.length; j++) {
					String rankString = pile[j];
					img = new Image("images/" + rankString + ".png");
					
					//Only apply absolute position for the first img
					if (j == 0) {
						currPile.add(img);
		      } else {
		      	currPile.add(img,0, j*10);
		      }//end if
		        
					//Add Id and Handlers to the top card of the pile
					if (j == pile.length - 1) {
						img.getElement().setId("discard" + String.valueOf(i));
						dragDropHandlers[16 + i*5] = img.addDragStartHandler(new CardDragHandler() );
						dragDropHandlers[17 + i*5] = img.addDragEnterHandler(new CardDragEnterHandler() );
						dragDropHandlers[18 + i*5] = img.addDragLeaveHandler(new CardDragLeaveHandler() ); 
						dragDropHandlers[19 + i*5] = img.addDragOverHandler(new CardDragOverHandler() );
						dragDropHandlers[20 + i*5] = img.addDropHandler(new CardDropHandler(view, eventBus, thisPlayer, gameService) );
					}//end if
		        
				}//end for	  
	    }//end if
		}//end for	
	}//end updateDiscard
	
  /**
   * Updates the East Player's Visible Cards
   * @param playerIdx index of the eastPlayer in PlayerList
   */
//  private void updateEastPlayer(int playerIdx) {
//  	Card topCard;
//    Image discardPile;
//    Image stock = view.getEastPlayer().getStock();
//  	Player thisPlayer = SBGame.getPlayer(playerIdx);
//  	
//	  //Set the East Player's Stock Image
//	  stock.setUrl("images/" + thisPlayer.getStock().getTop().toString() + "_RIGHT.png");
//	  
//	  //Update the 4 discard piles
//	  for(int i = 0; i < 4; i++) {
//	  	topCard = thisPlayer.getDiscard(i).getTop();
//	  	discardPile = view.getEastPlayer().getDiscardPile(3-i);
//	  	
//	  	//If no cards in pile, set to placeholder
//	  	if( topCard == null ) {
//	  		discardPile.setUrl("images/BACK_RIGHT.png");
//	  	} else {
//	  		discardPile.setUrl("images/" + topCard.toString() + "_RIGHT.png");
//	  	}//end if
//	
//	  }//end for
//	  
//  }//end updateEastPlayer
	
	/**
	 * Updates this Player's Hand for the UI
	 * @param hand The Player's hand data
	 */
	private void updateHand(String[] hand) {
		                   
    //UI Elements
    ArrayList<Image> handUI = view.getCurrentPlayer().getHand();
    int length;
    
    if( hand[0].equals("X") ) {
    	length = 0;
    } else {
    	length = hand.length;
    }//end if
    
    //Set Card Images for cards in thisPlayer's Hand and make them visible
    for(int i = 0; i < length; i++) {
      handUI.get(i).setUrl("images/" + hand[i] + ".png" );
      handUI.get(i).setVisible(true);
    }//end for
    
    //Hides Cards in the case where thisPlayer's Hand contains less than 5 cards
    for(int i = length; i < handUI.size(); i++) {
      handUI.get(i).setVisible(false);
    }//end for
  
	}//end updateHand
	
	/**
	 * Updates the North Player's Visible Cards
	 * @param handCount Size of the North player's hand
	 * @param stock Value of the top card of Stockpile
	 * @param discard The Discard Pile data for the north player
	 */
  private void updateNorthPlayer(int handCount, String stock, String[][] discard) {
  	ArrayList<Image> handUI = view.getNorthPlayer().getHand();
  	String topCard;
  	Image discardPile;
    Image stockUI = view.getNorthPlayer().getStock();
    
    
    //Set Card Images for cards in NorthPlayer's Hand and make them visible
    for(int i = 0; i < handCount; i++) {
      handUI.get(i).setVisible(true);
    }//end for
    
    //Hides Cards in the case where NorthPlayer's Hand contains less than 5 cards
    for(int i = handCount; i < handUI.size(); i++) {
      handUI.get(i).setVisible(false);
    }//end for
    
  
    //Set the North Player's Stock Image
    stockUI.setUrl("images/" + stock + ".png");
  
    //Update the 4 discard piles
    for(int i = 0; i < 4; i++) {
    	topCard = discard[i][discard[i].length - 1];
    	discardPile = view.getNorthPlayer().getDiscardPile(3-i);
	 
    	//If no cards in pile, set to placeholder
    	if( topCard.equals("X") ) {
    		discardPile.setUrl("images/BACK.png");
    	} else {
    		discardPile.setUrl("images/" + topCard + ".png");
    	}//end if

    }//end for
	  
  }//end updateNorthPlayer
	
  /**
   * Updates cards for all other Players on the client's Interface
   */
  private void updatePlayers() {
  	int[] otherPlayers = getOtherPlayerIndexes();
	  
	  //Update the appropriate number of players 
	  //In the proper order
	  switch(numPlayers) {
	  case 2:
	  	getNorthPlayerData( otherPlayers[0] );
	  	break;
//	  case 3:
//			getNorthPlayerData( otherPlayers[0] );
//	  	getWestPlayerData( otherPlayers[1] );
//	  	break;
//	  case 4:
//			getNorthPlayerData( otherPlayers[0] );
//	  	getWestPlayerData( otherPlayers[1] );
//			getEastPlayerData( otherPlayers[2] );
//	  	break;
	  default:
	  	view.displayMessage("See what you did by touching things you weren't supposed to?" +
	  								 "Now it's broken. Nice work. ");
		  break;
	  }//end switch
	  
  }//end updatePlayers
  
  /**
   * Updates the Scoreboard
   * @param scoreData Array of the Scoreboard data
   */
  private void updateScoreboard(String[] scoreData) {
  	FlexTable scoreboard = view.getTopPanel().getScoreboard();
  	Label nameLabel;
  	Label pointsLabel;
  	Label stockLabel;
  	Label turnLabel = (Label) scoreboard.getWidget(scoreboard.getRowCount()-1,0);
	    
  	//Update Player Name labels
  	for(int i = 0; i < numPlayers; i++) {
  		nameLabel = (Label) scoreboard.getWidget(i+1, 0);
  		nameLabel.setText( scoreData[i] + ":");
  	}//end for
	  
  	//Update Player Point labels
  	for(int i = numPlayers; i < 2 * numPlayers; i++) {
  		pointsLabel = (Label) scoreboard.getWidget(i - numPlayers + 1, 1);
  		pointsLabel.setText( scoreData[i] );
  	}//end for
  	
  	//Update Player Stock labels
  	for(int i = 2 * numPlayers; i < 3 * numPlayers; i++) {
  		stockLabel = (Label) scoreboard.getWidget(i - (2 * numPlayers) + 1, 2);
  		stockLabel.setText( scoreData[i] + "/" + scoreData[3 * numPlayers] );
  	}//end for
  	
    //Update currentTurns Label
  	turnLabel.setText("Current Turn: " + scoreData[(3 * numPlayers) + 1] );
  	
  }//end updateScoreboard
  
  /**
	 * Updates the Stockpile for the UI
	 * @param stock The Player's stock data
	 */
	private void updateStock(String stock) {                 
    //UI Element
    Image stockUI = view.getCurrentPlayer().getStock();
    
    //Set Card Image for ThisPlayer's Stock
    stockUI.setUrl("images/" + stock + ".png");
	}//end updateHand
  
  /**
   * Updates the UI for thisPlayer. 
   * Divides Player data into its respective parts, parses the data, and updates each UI part. 
   * @param thisPlayer Array containing player data
   */
  private void updateThisPlayer(String[] thisPlayer) {
  	String[][] parsedDiscard = new String[4][];
  	String[] parsedHand;
  	String[] discardPiles = new String[4];
  	String hand;
  	String stock;
  	
  	//Store Hand and Stock data
  	hand = thisPlayer[0];
  	stock = thisPlayer[1];
  	
  	//Store DiscardPile data
  	for(int i = 0; i < 4; i++) {
  		discardPiles[i] = thisPlayer[i+2];
  	}//end for
  	
  	//Parse Hand Data
  	parsedHand = hand.split(" ");
  	
  	//Parse DiscardPile data
  	for(int i = 0; i < 4; i++) {
  		parsedDiscard[i] = discardPiles[i].split(">");
  	}//end for
  	
  	//Update this Player's Hand, Stock, and Discard Piles on the UI
  	updateDiscard(parsedDiscard);
  	updateHand(parsedHand);
  	updateStock(stock);
  }//end updateThisPlayer
  
  /**
   * Updates the West Player's Visible cards
   * @param playerIdx
   */
//  private void updateWestPlayer(int playerIdx) {
//    Card topCard;
//    Image discardPile;
//    Image stock = view.getWestPlayer().getStock();
//  	Player thisPlayer = SBGame.getPlayer(playerIdx);
//    
//	  //Set the North Player's Stock Image
//	  stock.setUrl("images/" + thisPlayer.getStock().getTop().toString() + "_LEFT.png");
//	  
//	  //Update the 4 discard piles
//	  for(int i = 0; i < 4; i++) {
//	  	topCard = thisPlayer.getDiscard(i).getTop();
//	  	discardPile = view.getWestPlayer().getDiscardPile(i);
//		 
//	  	//If no cards in pile, set to placeholder
//	  	if( topCard == null ) {
//	  		discardPile.setUrl("images/BACK_LEFT.png");
//	  	} else {
//	  		discardPile.setUrl("images/" + topCard.toString() + "_LEFT.png");
//	  	}//end if
//	
//	  }//end for
//  }//end updateWestPlayer
	
}//end GamePresenter
