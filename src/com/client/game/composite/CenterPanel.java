/**
 * com.client.game.composite.CenterPanel
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Composite Class for the Center Panel containing the Buildpiles
 */

package com.client.game.composite;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class CenterPanel extends Composite {
	
	 private HorizontalPanel center;
   private ArrayList<Image> buildPiles;
   
	/**
	 * Constructs the CenterPanel
	 */
	public CenterPanel() {
		
		//Initialize CenterPanel and its contents
		center = new HorizontalPanel();
		buildPiles = new ArrayList<Image>();
		
		//Center the CenterPanel
    center.addStyleName("center");
    
    //Assemble Center Panel
    loadBuildPiles();
    
    //Wrap center in this Composite Class
  	initWidget(center);
    
	}//end constructor
	
	/**
	 * Populates the BuildPiles and add to CenterPanel
	 */
	private void loadBuildPiles() {
		
		//Populate BuildPiles and add to centerPanel  
    for(int i = 0; i < 4; i++) {
      buildPiles.add(new Image("images/BACK.png"));
      
      //Set id for easier event handling  
      buildPiles.get(i).getElement().setId("build" + Integer.toString(i) );
      
      //Add to panel
      center.add(buildPiles.get(i));
    }//end for
	}//end loadBuildPiles
	
	/**
	 * Returns the BuildPiles
	 * @return buildPiles The BuildPiles
	 */
	public ArrayList<Image> getBuildPiles() {
		return buildPiles;
	}//end getBuildPiles
	
}//end CenterPanel
