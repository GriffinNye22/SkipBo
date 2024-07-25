/**
 * com.client.StatRetrievalServiceClient
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Client Implementation of the StatsRetrievalService.
 */

package com.client;

import com.client.game.GamePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class StatRetrievalServiceClient implements StatRetrievalServiceClientInterface {
	private StatRetrievalServiceAsync statService;
	private GamePresenter presenter;
	
	/**
	 * Constructs the StatRetrievalService for the client side.
	 * @param url The URL of Server implementation
	 * @param presenter Instance of the Game Presenter
	 */
	public StatRetrievalServiceClient(String url, GamePresenter presenter) {
		this.statService = (StatRetrievalServiceAsync) GWT.create(StatRetrievalService.class);
		//this.statService = GWT.create(StatRetrievalService.class);
		this.presenter = presenter;
		ServiceDefTarget destTarget = (ServiceDefTarget) this.statService;
		destTarget.setServiceEntryPoint(url);
	}//end constructor
	
	/**
	 * Retrieves the Average Turns from StatTracker
	 */
	@Override
	public void retrieveAvgTurns() {
		this.statService.retrieveAvgTurns( new Callback() );
	}//end retrieveAvgTurns

	/**
	 * Retrieves the number of games played from StatTracker
	 */
	@Override
	public void retrieveGamesPlayed() {
		this.statService.retrieveGamesPlayed( new Callback() );
	}//end retrieveGamesPlayed

	/**
	 * Retrieves the max turns from StatTracker.
	 */
	@Override
	public void retrieveMaxTurns() {
		this.statService.retrieveMaxTurns( new Callback() );
	}//end retrieveMaxTurns

	/**
	 * Retrieves the min turns from StatTracker.
	 */
	@Override
	public void retrieveMinTurns() {
		this.statService.retrieveMinTurns( new Callback() );
	}//end retrieveMinTurns

	/**
	 * Passes the number of turns to StatTracker to be recorded
	 * @param turns The number of turns (as a string). 
	 */
	@Override
	public void recordGameStats(String turns) {
		this.statService.recordGameStats(turns, new Callback() );
	}//end recordGameStats
	
	private class Callback implements AsyncCallback {

		/**
		 * Operations to be carried out on failure.
		 */
		@Override
		public void onFailure(Throwable caught) {
			System.out.println("StatRetrievalService: An error occured in trying to reach the server.");
		}//end onFailure

		/**
		 * Operations to be carried out on success.
		 */
		@Override
		public void onSuccess(Object result) {
			
			if(result instanceof Float || result instanceof Integer) {
				//presenter.saveStat( (Float) result );
			}//end if
			
		}//end onSuccess
		
	}//end Callback
	
}//end StatRetrievalServiceClient
