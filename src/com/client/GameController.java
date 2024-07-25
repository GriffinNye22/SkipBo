/**
 * com.client.GameController
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Handles navigation between pages and calling the StatRetrievalService
 */

package com.client;

import com.client.game.GamePresenter;
import com.client.settings.ConnectionService;
import com.client.settings.ConnectionServiceAsync;
import com.client.settings.SettingsPresenter;
import com.client.settings.SettingsView;
import com.client.settings.WaitForHostPresenter;
import com.client.settings.WaitForHostView;
import com.client.settings.event.SettingsConfirmEvent;
import com.client.settings.event.SettingsConfirmEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GameController {
	
	//Pages & Events
	private HasWidgets container;
	private HandlerManager eventBus;
	private WaitForHostPresenter waitPage;
	private SettingsPresenter settingsPage;
	private GamePresenter gamePage;
	private ConnectionServiceAsync connService;
	
	/**
	 * Constructs the GameController
	 * @param eventBus The EventBus for the GameController
	 */
	public GameController(HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.connService = GWT.create(ConnectionService.class);
		ServiceDefTarget destTarget = (ServiceDefTarget) this.connService;
		destTarget.setServiceEntryPoint(GWT.getModuleBaseURL() + "connection");
		
		//Create the Settings Page and bind its events
		settingsPage = new SettingsPresenter( new SettingsView(), eventBus);
		bindEvents();
		
		//Create the Wait Page and bind its events
		waitPage = new WaitForHostPresenter( new WaitForHostView() );
	}//end constructor
	
	
	/**
	 * Binds events between the GamePresenter and this controller
	 */
	public void bindEvents() {
		
		eventBus.addHandler(SettingsConfirmEvent.TYPE, new SettingsConfirmEventHandler() {
			
			/**
			 * Handles the GameLoad event which switches from SettingsPage to GamePage
			 */
			public void onSettingsConfirm(SettingsConfirmEvent event) {
				final int numPlayers;
				int numCPUs;
				int numStock;
				int numPoints;
				String playerName;
				 
				SettingsPresenter.Display view = settingsPage.getView();
	      
	      //Store Game Settings
				numPlayers = Integer.parseInt( view.getPlayerValue() );
				numCPUs = Integer.parseInt( view.getCPUValue() );
				numStock = Integer.parseInt( view.getStockValue() );
				numPoints = Integer.parseInt( view.getPointValue() );
				playerName = view.getPlayerName();
				
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					
					@Override
					public void onFailure(Throwable caught) {
						displayError("The System admin has some work to do. There was an error creating the game.");
					}//end onSuccess

					@Override
					public void onSuccess(Void noResult) {
						waitForPlayers(numPlayers, 0);
					}//end onSuccess
				};//end AsyncCallback
				
				//Start the Game
				connService.startGame(numPlayers, numCPUs, numStock, numPoints, playerName, callback);
				
//				//Initialize StatRetrievalService
//				statService = new StatRetrievalServiceClient( GWT.getModuleBaseURL() + "StatRetrievalService", gamePage);
//				
//				//Retrieve Stats
//				statService.retrieveGamesPlayed();
//				statService.retrieveMinTurns();
//				statService.retrieveMaxTurns();
//				statService.retrieveAvgTurns();
			
			}//end onSettingsConfirm
		});//end SettingsConfirmEventHandler
		
	}//end bindEvents

	/**
	 * Creates the Game Presenter and Displays the Game View for the client
	 * @param numPlayers The number of players in the Game
	 * @param thisPlayer The player index assigned to this client
	 */
	private void createGamePage(int numPlayers, int thisPlayer) {
		
		//Create Game Presenter
		gamePage = new GamePresenter(eventBus, numPlayers, thisPlayer);
	
		//Update Stats in Stats Panel
		gamePage.getView().getStatsPanel().updateStats( gamePage.getStats() );
		
		//Show the Stats Panel
		gamePage.getView().getStatsPanel().asWidget().center();

		//Display Game page
		gamePage.display(container);
	}//end createGame
	
	/**
	 * Connects non-Host Clients to the Game providing them with their assigned player index
	 * @param numPlayers The number of players in the Game
	 * @param name The client's player name
	 */
	private void connectClient(final int numPlayers, String name) {
		
		//ConnectClient callback
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				displayError( caught.getMessage() );
			}//end onFailure

			@Override
			public void onSuccess(Integer playerIdx) {
				
				//Display GamePage for Accepted clients
				if(playerIdx != -1) {
					waitForPlayers(numPlayers, playerIdx);
				//Display Error Message for Refused clients
				} else {
					displayError("The Game has reached capacity. Please try again in a few minutes.");
				}//end if-else
				
			}//end onSuccess
		};//end AsyncCallback
		
		//Connect non-host client to game
		connService.connectClient(name, callback);
		
	}//end connectClient
	
	/**
	 * Display an error message to the user
	 */
	private void displayError(String message) {
		DialogBox errorMsg = new DialogBox();
		Label msg = new Label(message);
		errorMsg.add(msg);
		errorMsg.center();
	}//end displayError
	
	/**
	 * Displays the Settings page
	 */
	private void displaySettings() {
		settingsPage.display(container);
	}//end displaySettings
	
	/**
	 * Displays the WaitForHost page
	 */
	private void displayWait() {
		
		//Display WaitForHost Page
		waitPage.display(container);
		
		Timer gameStartTimer = new Timer() {

			/**
			 * Polls the Server every 3 seconds to see if the Game has started
			 */
			@Override
			public void run() {
				
				AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						displayError("Error checking Game start state.");
					}//end onFailure

					@Override
					public void onSuccess(Integer numPlayers) {
						
						if(numPlayers != 0) {
							cancel();
							promptName(numPlayers);
						}//end if
						
					}//end onSuccess
				};//end AsyncCallback
				
				//Check if the Game has started
				connService.checkGameStart(callback);
			}//end run
			
		};//end Timer;
		
		//Run Server Poll every 3 seconds
		gameStartTimer.scheduleRepeating(3000);
	}//end displayWait
	
	/**
	 * Launches the Game.
	 * @param container The container for the Game Application
	 */
	public void launch(HasWidgets container) {
		
		//Store the container for the Application
		this.container = container;
		
		//Display the Settings Page inside this container
		
		//Create callback object
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				displayError("An error has occurred communicating with the server.");
			}//end onFailure

			@Override
			public void onSuccess(Boolean hostClient) {
					
				//Display Settings page for Host client, Display Wait page for all other clients
				if (hostClient) {
					displaySettings();
				} else {
					displayWait();
				}//end if-else
						
			}//end onSuccess
		};//end callback
				
		//Create Connection to Server, Determine if Host client
		connService.connectHostClient(callback);
	}//end launch
	
	/**
	 * Prompts the Player for their name
	 */
	private void promptName(final int numPlayers) {
		final DialogBox prompt = new DialogBox();
		VerticalPanel promptPanel = new VerticalPanel();
		final TextBox nameBox = new TextBox();
		Button submit = new Button("submit");
		Label nameLabel = new Label("Please enter your player name:");
	
		submit.addClickHandler(new ClickHandler() {

		  @Override
		  public void onClick(ClickEvent event) {
		    connectClient( numPlayers, nameBox.getValue() );
		    prompt.hide();
		  }//end OnClick
		});//end ClickHandler
		
		//Assemble Panel
		promptPanel.add(nameLabel);
		promptPanel.add(nameBox);
		promptPanel.add(submit);
		
		//Center Panel contents
		promptPanel.setCellHorizontalAlignment(nameLabel, HasHorizontalAlignment.ALIGN_CENTER);
		promptPanel.setCellHorizontalAlignment(nameBox, HasHorizontalAlignment.ALIGN_CENTER);
		promptPanel.setCellHorizontalAlignment(submit, HasHorizontalAlignment.ALIGN_CENTER);	
	
		//Assemble prompt
		prompt.setWidget(promptPanel);
		
		//Display prompt
		prompt.center();

		//Place focus in textbox
		nameBox.setFocus(true);
		
	}//end promptName
	
	/**
   * Makes client wait for all players to connect to game
   * @param numPlayers Number of players in the game
   * @param playerIdx The client's associated player index
   */
  private void waitForPlayers(final int numPlayers, final int playerIdx) {
  	
  	//Notify user Waiting for Players to connect
  	final DialogBox waitMsg = new DialogBox();
		Label msg = new Label("Waiting for other players to finish connecting.");
		waitMsg.add(msg);
		waitMsg.center();
  	
		Timer connectionTimer = new Timer() {

			/**
			 * Polls the Server every 3 seconds to see if the Game is full
			 */
			@Override
			public void run() {
				
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						displayError("Error checking if Game full.");
					}//end onFailure

					@Override
					public void onSuccess(Boolean gameFull) {
						
						if(gameFull) {
							cancel();
							waitMsg.hide();
							createGamePage(numPlayers, playerIdx);
						}//end if
					
					}//end onSuccess
				};//end AsyncCallback
				
				//Check if the Game has started
				connService.checkGameFull(callback);
			}//end run
		};//end Timer;
		
		//Run every 3 seconds
		connectionTimer.scheduleRepeating(3000);
	}//end waitForPlayers
	
}//end GameController
