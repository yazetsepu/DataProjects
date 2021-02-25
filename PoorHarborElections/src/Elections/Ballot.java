package Elections;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import ADTs.ArrayList;
import ADTs.DynamicSet;
import ADTs.List;
import ADTs.Set;

/**
 * <h1> Ballots class</h1>
 * 
 * <p>
 * The Ballot class contains all the necessary methods to read, process and store the given ballots csv files.
 * It uses a constructor to run some static methods that read the csv file and return an id and a list of 
 * Votes objects that store every candidate's id and their preference given by a voter (in this case stated in
 * the file in the following format: "(candidate id):(assigned preference or rank)".
 * </p>
 * Some useful methods contained are:
 * <p>
 * {@code Ballot} : Constructor that runs two static methods, one that reads the id of the ballot, corresponding 
 *                  to the column 0 (or 1 with "1" indexing), and one that creates a list of votes found in the 
 *                  ballots csv file, and creates a new Ballot object with them. This constructor also runs a 
 *                  variety of calculations that aid when determining if it is invalid
 * </p>
 * {@code GettersAndSetters} : Variety of methods that allow easy access to ballot values and modification of them if needed.
 * <p>
 * {@code printVotes} : Helper method that prints to the console the ballots in a certain list of ballots if these are valid,
 *                      if it encounters a blank or invalid ballot, it will indicate it and not print the actual ballot
 * </p>
 * {@code isBlank} : Method that returns a boolean value to whether or not a given ballot is empty (or blank)
 * <p>
 * {@code isInvalid} : Method that returns a boolean value to whether or not a given ballot is invalid
 * </p>
 * {@code readVotes} : Method that returns a list of votes that are read from the ballot input csv file, the method
 *                     receives an input from a scanner that runs in the elections class, receiving a row from the
 *                     csv file and adding Votes objects with their corresponding id and rank to a list that 
 *                     correspond to the votes in a given ballot
 * <p>
 * {@code readID} : Method that returns an integer value that corresponds to the id of any given ballot, the 
 *                  method received a scanner in the same manner as {@code readVotes} but stores the contents
 *                  of the first column of every csv file as the id for a corresponding ballot that a row 
 *                  represents
 * </p>
 * 
 * <p>
 * {@code getRankByCandidate} : Method that returns an integer representing the rank of a candidate that is passed to it
 * </p>
 * <p>
 * {@code getCandidateByRank}: Method that returns a given candidate depending on the rank value passed to it
 * </p>
 * <p>
 * {@code eliminate} Method that returns a boolean if a candidate whose id was passed to this method is successfully eliminated 
 * </p>
 * <p>
 * {@code getAmountOfN} Method that returns a boolean value to whether or not a candidate was given the rank of N in a given ballot 
 * </p>
 * 
 * 
 * 
 * @author Yazet Sepulveda
 * @version 1.0
 * @since 2020-03-13
 */

public class Ballot {

	//ID of the individual ballot
	private int ID;

	//List of votes with preference and the id of the candidates they are linked to
	private List<Votes> votes = new ArrayList<Votes>(10);

	//Checks if the current ballot is false
	private boolean blankBallot = false;

	//Checks if the current ballot is invalid
	private boolean isInvalid = false;

	//Allows storing of single votes to compare in the future
	private Votes checker = null;

	//Stores the total sum of all votes, in other words the expect total preference
	private int sumOfVotes = 0; 

	//Stores the total sum of all ranks, in other words the actual total preference
	private int sumOfRanks = 0;

	public Ballot(String scannerIn) {

		this.ID = readID(scannerIn);	
		this.votes = readVotes(scannerIn);

		if(!this.isBlank()) {

			for(int i = 1; i < this.votes.size()+1; i++) {

				this.sumOfVotes += i; 
			}

			for (Votes vote : votes) {

				this.sumOfRanks+=vote.getRank();
				
			}
		}

	}

	public int getBallotNumber() {
		return this.ID;
	}

	public int getsumOfRanks() {
		return this.sumOfRanks;
	}

	public int getsumOfVotes() {
		return this.sumOfVotes;
	}

	//Tester class that allows the easy printing of votes
	public void printVotes() {

		if(this.isBlank()) {
			System.out.println("Ballot ID: " + this.getBallotNumber() + " This ballot is blank");
			return;
		}

		else if(this.isInvalid()) {
			System.out.println("Ballot ID: " + this.getBallotNumber() + " This ballot is invalid!");
			return;
		}

		System.out.println("Ballot " + this.ID + ": ");
		for (Votes vote : votes) {
			System.out.print(vote.getID() + ":" + vote.getRank() + " , ");
		}
	}

	//Checks if the votes list is empty, if yes, then the ballot is blank
	public boolean isBlank() {
		if(this.votes.isEmpty())
			this.blankBallot = true;

		return blankBallot;
	}

	//Checks to see if a ballot is invalid 
	public boolean isInvalid() {

		Set<Integer> iDSet = new DynamicSet<Integer>(this.votes.size());
		
		for (Votes votes: votes) {
			
			iDSet.add(votes.getID());
			
		}
		
		if(iDSet.size() < votes.size())
			return isInvalid = true;
		
		if(this.sumOfRanks != this.sumOfVotes) {
			return this.isInvalid = true;
		}
		
		

		for (Votes vote : votes) {

			//Invalid if CandidateRank < 1 OR > than the amount of candidates
			if(vote.getRank() < 1 || vote.getRank() > Candidates.addCandidates("//candidates (1).csv").size()) {
				return this.isInvalid = true;
			}
			

		}
		return isInvalid;
	}

	public static List<Votes> readVotes(String scannerIn) {	

		List<Votes> newVote = new ArrayList<Votes>(10);

		//Stores the contents of a cell in the row array
		String[] row = scannerIn.split(",");

		for (int i = 1; i < row.length; i++)
		{	
			String[] div = row[i].split(":");

			for (int j = 0; j < 1; j++) 
			{
				Votes vote = new Votes(Integer.parseInt(div[j]),Integer.parseInt(div[++j]));
				newVote.add(vote);
			}
		}
		return newVote;
	}

	public static int readID(String scannerIn) {	

		int ID = 0;		

		String[] row = scannerIn.split(",");
		ID = Integer.parseInt(row[0]);

		return ID;
	}

	public int getRankByCandidate(int candidateId) {		

		for (Votes vote : this.votes) {
			if(vote.getID() == candidateId) {
				return vote.getRank();
			}
		}

		return -1;
	}

	public int getCandidateByRank(int rank) {

		for (Votes vote : votes) {
			if(vote.getRank() == rank)
				return vote.getID();
		}
		return -1;
	}
	
	public boolean getAmountOfN(int iD, int n) {
		
		int amountOfN = 0;
		
		for (Votes vote : votes) {
			
			if(vote.getRank() == n && vote.getID() == iD)
				return true;
			
		}
		
		
		return false;
	}

	public boolean eliminate(int candidateId) {

		Votes removedVote;

		for (Votes vote : votes) {

			if(vote.getID() == candidateId) {

				removedVote = vote; 

				votes.remove(vote);

				for(Votes voteCRemoved: votes) {
					if(voteCRemoved.getRank() > removedVote.getRank())
						voteCRemoved.setRank(voteCRemoved.getRank()-1);
				}

				return true;
			}
		}
		return false;
	}

}