/**
 * com.client.SkipBoException
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Custom Exception for exceptions encountered in the Model.
 */

package com.client;

import java.io.Serializable;

public class SkipBoException extends RuntimeException implements Serializable {
	
	public SkipBoException() {
		
	}//end constructor
	
	public SkipBoException(String message) {
	  super(message);
	}//end constructor	
	
}//end SkipBoException