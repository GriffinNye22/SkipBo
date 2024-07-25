/**
 * com.client.settings.ConnectionService
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Client-side interface for the ConnectionService
 */

package com.client.settings;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("connection")
public interface ConnectionService extends RemoteService {

	Boolean checkGameFull();
	Boolean connectHostClient();
	Void startGame(int players, int CPUs, int stock, int points, String hostName);
	Integer checkGameStart();
	Integer connectClient(String name);
	
}//end ConnectionService