package Elections;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import ADTs.ArrayList;
import ADTs.List;

/**
 * <h1> Candidates class </h1>
 * 
 * <p>
 * The Candidates class contains all the necessary methods in order to read, store and assign values to the candidates read from a provided
 * csv file.
 * </p>
 * The class is in charge of creating an object with a constructor that assigns a name and id to a candidate read from the csv candidates file
 * Each candidate has a name, an id, and an amount of 1s, in other words, the amount of Ballots in which a candidate was marked down as a 
 * first preference to win the election
 * 
 * <p>
 * Some useful methods are
 * </p>
 * {@code Candidates} : Class constructor that assigns the name and id to a candidate object upon creation
 * <p>
 * {@code GettersAndSetters} : A variety of methods used to retrieve and modify information in a Candidates object when needed
 * </p>
 * {@code printCandidates} : Helper method used to easily print out all candidates in a given list of candidates
 * <p>
 * {@code addCandidates} : Method that returns a list of candidates that it reads from the candidate csvFile
 * </p>
 * 
 * @author Yazet Sepulveda
 * @version 1.0
 * @since 2020-03-13
 */

public class Candidates {
	
	//Absolute path to the InputOutput package in the src folder, where all files to be read and created are stored
	private static final String PATH = "src//InputOutput";

	//Name of a given candidate
	private String Name;

	//ID of a given candidate
	private int ID;

	//Amount of ones that a candidate has
	private int amountOfOnes; 

	//Creates a new candidate with a name and ID
	public Candidates(String Name, int ID) {
		this.Name = Name;
		this.ID = ID;
	}

	public String getName() {
		return this.Name;
	}

	public int getID() {
		return this.ID;
	}
	
	public int getAmountOfOnes() {
		return this.amountOfOnes;
	}

	public void setAmountOfOnes(int amountOfOnes) {
		this.amountOfOnes = amountOfOnes;
	}	

	public static void printCandidates(List<Candidates> candidateList) {

		for (Candidates can :  candidateList) {

			System.out.println("Name: "  + can.getName() + " - ID: " + can.ID);

		}

	}

	//Reads a CSV file and returns the amount of candidates in a file
	public static List<Candidates> addCandidates(String csvFilePath) {

		BufferedReader br = null;
		String line = "";
		//Denotes the character that splits the columns of the csv elements, a comma
		String cvsSplitBy = ",";

		List<Candidates> newCandidateList = new ArrayList<Candidates>(5);

		try {

			br = new BufferedReader(new FileReader(Paths.get(PATH + csvFilePath).toString()));

			//Reads every row of the CSV file until it find an empty one
			while ((line = br.readLine()) != null) {
				// Stores every cell found in the current row in a array. The indexes of the array each hold a cell or column in the row
				String[] row = line.split(cvsSplitBy);
				//Creates a new candidate with the name that appears in the first column and gives them the ID in the next column
				Candidates newCandidate = new Candidates(row[0], Integer.parseInt(row[1]));
				//Adds created candidate into the candidate list
				newCandidateList.add(newCandidate);

			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return newCandidateList;

	}

}

