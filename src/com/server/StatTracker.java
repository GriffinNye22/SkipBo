	/**
	 * com.server.StockLoader
	 * CSC421 Fall 2020
	 * @author Griffin Nye
	 * Loads and Records the Game Stats.
	 */

	package com.server;

	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.ArrayList;
	import java.util.Scanner;

	public class StatTracker {
		final private String FILENAME = "log.txt";
		private ArrayList<Integer> stats = new ArrayList<Integer>();
		private String filename;
		
		private float avgTurns;
		private int maxTurns;
		private int minTurns;
		private int numGamesPlayed;
	
		
		/**
		 * Constructs the StatTracker object
		 * @param file Name of the file containing Game statistics
		 * @throws FileNotFoundException 
		 */
		StatTracker() {
			
			try {
				//Load stats from file
				loadStats(FILENAME, stats);
			} catch(FileNotFoundException e) {
				System.out.println("Error retrieving stats: File " + filename + " not found.");
			}//end try-catch
			
			//Store Stats
			numGamesPlayed = stats.size();
			avgTurns = calcAvgTurns(stats);
			maxTurns = calcMaxTurns(stats);
			minTurns = calcMinTurns(stats);
			
		}//end constructor
		
		/**
		 * Calculates AvgTurnsPerGame
		 * @param stats ArrayList storing stats
		 * @return float AvgTurnsPerGame
		 */
		private float calcAvgTurns(ArrayList<Integer> stats) {
			float avg = 0;
			
			for (int i: stats) {
				avg += i;
			}//end for
			
			return ( avg/stats.size() );
		}//end calcMaxTurns
		
		/**
		 * Calculates Maximum Turns recorded for a single game
		 * @param stats ArrayList storing stats
		 * @return int MaxTurnsPerGame
		 */
		private int calcMaxTurns(ArrayList<Integer> stats) {
			int max = 0;
			
			for (int i: stats) {
				if (i > max) {
					max = i;
				}//end if
			}//end for
			
			return max;
		}//end calcMaxTurns
		
		/**
		 * Calculates Minimum Turns recorded for a single Game
		 * @param stats ArrayList storing stats
		 * @return int MinTurnsPerGame
		 */
		private int calcMinTurns(ArrayList<Integer> stats) {
			int min = -1;
			
			for (int turnRecord: stats) {
				
				if (turnRecord < min) {
					min = turnRecord;
				}//end if
				
			}//end for
			
			return min;
		}//end calcMinTurns
		
		/**
		 * Retrieves Average Number of Recorded Turns Per Game
		 * @return float The Average Number of Recorded Turns Per Game
		 */
		public float getAvgTurns() {
			return this.avgTurns;
		}//end getAvgTurnsPerGame
		
		/**
		 * Retrieves maximum number of recorded turns in a single Game
		 * @return Maximum number of recorded turns in a single Game
		 */
		public int getMaxTurns() {
			return maxTurns;
		}//end getMaxTurnsPerGame
		
		/**
		 * Retrieves minimum number of recorded turns in a single Game
		 * @return Minimum number of recorded turns in a single Game
		 */
		public int getMinTurns() {
			return minTurns;
		}//end getMaxTurnsPerGame
		
		/**
		 * Retrieves the number of completed Games for which stats were recorded
		 * @return int The number of completed Games
		 */
		public int getNumGamesPlayed () {
			return numGamesPlayed;
		}//end getNumGamesPlayed
		
		/**
		 * Reads the stats in from the file
		 * @param filename name of the file
		 * @param stats ArrayList for storing stats
		 */
		private void loadStats(String filename, ArrayList<Integer> stats) throws FileNotFoundException {
			File readFile;
			Scanner fileScan;
			String line;
			
			try {
				
				readFile = new File(filename);
				fileScan = new Scanner(readFile);
				
				//Read to end of file
				while ( fileScan.hasNextLine() ) {
					
					//Scan next line
					line = fileScan.nextLine();
					
					//Add contents of line to stats ArrayList
					stats.add(Integer.parseInt(line));
					
				}//end while
				
				//Close the Scanner
				fileScan.close();
				
			} catch (FileNotFoundException e) {
				//Throw to method caller
				throw(e);
			}//end try-catch
			
		}//end loadStats

		/**
		 * Writes the stats to the file
		 * @param filename name of the file
		 * @param stats ArrayList storing stats
		 */
		public void recordStats(String gameStats) throws IOException {
			PrintWriter writer;
			//String str;
			
			// filename = Input.FileReader.getOutputFilename();
			
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(this.filename)));
				
				writer.println(gameStats);
				
//				for(int i = 0; i < stats.size(); i++) {
//					str = String.valueOf( stats.get(i) ) + "\n";
//					writer.write(str);
//				}//end for
				
				writer.close();
			} catch (IOException e) {
				throw(e);
			}//end try-catch
			
		}//end loadStats
		
}//end StatTracker


