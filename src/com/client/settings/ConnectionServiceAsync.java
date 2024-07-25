/**
 * com.client.settings.ConnectionServiceAsync
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Asynchronous Client-side interface for the ConnectionService
 */

package com.client.settings;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConnectionServiceAsync {
	
	void checkGameFull(AsyncCallback<Boolean> callback);
	void checkGameStart(AsyncCallback<Integer> callback);
	void connectClient(String name, AsyncCallback<Integer> callback);
	void connectHostClient(AsyncCallback<Boolean> callback);
	void startGame(int players, int CPUs, int stock, int points, String hostName, AsyncCallback<Void> callback);

	
	
}//end ConnectionServiceAsync
