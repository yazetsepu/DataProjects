package Elections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Scanner;

import ADTs.ArrayList;
import ADTs.DynamicSet;
import ADTs.List;
import ADTs.Set;

/**
 * 
 * <h1>Poor Harbor Elections!</h1>
 * 
 * <p>
 * The Election class is a static class containing a variety of methods that perform the needed actions
 * in order to properly read, store, and process the data obtained from two main .csv files: one containing
 * candidates for the election, and the other containing the ballots in which votes for these candidates
 * are found. Upon processing of the given data, the program outputs a text file titled "results.txt" to the 
 * eclipse program directory, in the src file, under a package named: "InputOutput". Please be sure to
 * the project upon running it so that the results text file may appear in the InputOutput package
 * </p>
 * <p>
 * All desired files must be stored under the "InputOutput" package in the programs src folder to be read
 * and processed correctly by the program. The full path used for storage, and reading is the general 
 * path: src/InputOutput/(name of the file to be read). If a different file wants to be added and read,
 * the file name must be changed in this class' global variables, specifically, the variable named:
 * "csvBallotPath" and "csvCandidatePath" and the value that should be entered here is name of the desired file with the format: 
 * <p>
 * {@code "//(name of file).csv"}
 * </p>
 * </p>
 * 
 * <p>
 * This class acts as the launch point for all previously created objects and uses them heavily in order to
 * complete the required election process and pick a winner as stated in the project requirements. 
 * </p>
 * The class utilizes different data structures to store and manipulate the data it is given.
 * Specifically, these are: ArrayLists and DynamicSets. 
 * <p>
 * The main lists and sets found here were used for the following:
 * </p>
 * 
 * <p>
 * {@code candidateList} : is an ArrayList that stores all candidates given in the candidates file as "Candidates" objects. This list is not 
 *                         meant to be modified
 * </p>
 * 
 * <p>
 * {@code newS} : is an ArrayList containing Set ADTs which contain Ballot type objects. This list is the main list used to store and modify
 *           the ballots and the votes in them
 * </p>
 * 
 * <p>
 * Other notable lists used to store data temporarily and either iterate through it or classify it include:
 * </p>
 * 
 * <p>
 * {@code tempCandidateList} : ArrayList of Candidates used to store candidates and iterate through them, modifying if necessary
 * </p>
 * 
 * <p>
 * {@code candidateBallotList} : ArrayList of Sets used to store the ballots corresponding to the specific candidates
 * </p>
 * 
 * <p>
 * {@code newBallotList} : ArrayList of Sets used to parse data in the csv Ballot file and store them in the global static variable {@code newS}
 * </p>
 * 
 * <p>
 * {@code losingCandidates} : Set of candidates that stores all candidates that have been "eliminated" without having to actually remove them from any list
 * </p>
 * 
 * <p>
 * The methods contained in this class, in order of appearance include:
 * </p>
 * 
 * <p>
 * {@code main} : Launch point for the whole program, all files that want to be accessed, need to be written here with their corresponding name
 * </p>
 * 
 * <p>
 * {@code storeBallots} : Method that returns a List<Set<Ballot>>, in charge of reading the Ballot csv files and adding them to the list {@code newS}            
 * </p>
 * 
 * <p>
 * {@code countBallots} : Method that returns an integer, it reads the Ballot file provided and counts all ballots
 * </p>
 * 
 * <p>
 * {@code electionHelper} : Recursive method that assigns each candidate in the list the amount of 1s they received in all ballots corresponding to them
 * </p>
 * 
 * <p>
 * {@code electionLoser} : Method that returns the current losing candidate and takes them out of the running, has a case for lowest amount of ones, 
 *                         and for ties
 * </p>
 * 
 * <p>
 * {@code electionRounds} : Method that performs the actual rounds in the election, eliminating candidates when need be and assigning a winner 
 *                          when one is found. It also writes to the text file.
 * </p>
 * 
 * <p>
 * {@code getAmountOfNs} : Recursive method that returns a candidate, it acts as a tie breaker in the case that candidates share same amount of 1s. It
 *                         starts at 2, comparing the amount of times the candidates that are tied, were ranked as such, and continues down the ranks if
 *                         necessary, stopping when a candidate has a lower number of that N's preferences in the ballots, or if it reaches the end of the 
 *                         candidate list, in other words, the amount of candidates or sets that stores the preferences. (N being the current rank in which
 *                         the candidates are being compared). Uses a Ballot class to add up the amount of times a candidate is given the rank of N in a 
 *                         given ballot     
 * </p>                        
 *
 * <p>                                 
 * Any specific doubts regarding a method, please refer to said method for extra documentation.          
 * </p>                      
 * 
 * @author Yazet Sepulveda 
 * @version 1.0
 * @since 2020-03-13
 */ 

public class Election {

	//Stores initial path to the eclipse directory where the files are located
	private static final String PATH = "src//InputOutput//"; 
	//Stores the name of the file that the program will read
	public static String csvBallotPath = "//ballots2.csv";
	//Stores the name of the file that the program will read
	public static String csvCandidatePath = "//candidates (1).csv";
	//Global list that contains all candidates
	private static List<Candidates> candidateList = Candidates.addCandidates("//candidates (1).csv");
	//List that stores all candidates along with the ballots that had them cast as the first preference.
	static List<Set> newS = storeBallots(csvBallotPath);
	//Checks to see if there is a winner
	private static boolean isWinner = false;
	//Stores the total amount of ballots received 
	private static int ballotsReceived;
	//Stores the total amount of invalid ballots received 
	private static int invalidBallotsReceived;
	//Stores the total amount of valid ballots received 
	private static int validBallotsReceived;
	//Set of candidates that have been eliminated
	static Set<Candidates> losingCandidates = new DynamicSet<Candidates>(candidateList.size());
	//Pathing to eclipse directory where the text file will be created
	static String textPath = "//results.txt";

	public static void main(String[] args) throws IOException {

		countBallots(csvBallotPath);

		BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(PATH + textPath).toString()));

		writer.write("Total amount of ballots: " + ballotsReceived);

		writer.write(" ");

		writer.write("Total amount of  blank ballots: " + (ballotsReceived - (validBallotsReceived+invalidBallotsReceived)));

		writer.write(" ");


		writer.write("Total amount of invalid ballots: " + invalidBallotsReceived);

		writer.write(" ");



		electionResults(writer);

		writer.close();


	}

	@SuppressWarnings("rawtypes")
	public static List<Set> storeBallots(String csvFilePath){

		List<Candidates> tempCandidateList = Candidates.addCandidates("//candidates (1).csv");

		List<Set> newBallotList = new ArrayList<Set>(tempCandidateList.size());

		File file = new File(Paths.get(PATH + csvFilePath).toString());

		try {

			for(Candidates can: tempCandidateList) 
			{

				Set<Ballot> newBallotSet = new DynamicSet<>(tempCandidateList.size());

				Scanner s = new Scanner(file, StandardCharsets.UTF_8.name());

				while (s.hasNextLine()) 
				{
					Ballot newBallot = new Ballot(s.nextLine());

					if(newBallot.getRankByCandidate(can.getID()) == 1) 
					{
						if(newBallot.isInvalid())
							invalidBallotsReceived++;

						if(!newBallot.isBlank() && !newBallot.isInvalid()) 
						{	
							validBallotsReceived++;
							newBallotSet.add(newBallot);
						}
					}

				}

				newBallotList.add(newBallotSet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newBallotList;
	}

	public static void countBallots(String csvFilePath) {
		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(Paths.get(PATH + csvFilePath).toString()));
			//Reads every row of the CSV file until it find an empty one
			while ((line = br.readLine()) != null) {

				ballotsReceived++;

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

	}

	//This method is honestly the proudest thing I have produced from this project, I am very happy with it
	@SuppressWarnings("rawtypes")
	public static void electionHelper(int step, int current) {

		List<Set> candidateBallotList = newS;

		//Base case
		if(current <= 0)
			return; 


		/*Where step represents moving through a list of candidates and current is amount of candidates in the list, in other words, this will only recur an
		 * an amount of times equal to candidateList.size()
		 */
		if(losingCandidates.isMember(candidateList.get(step))){
			electionHelper(step+1, --current);
		}

		candidateList.get(step).setAmountOfOnes(candidateBallotList.get(step).size());

		//Recursive case
		electionHelper(step+1, --current);

		return;	
	}

	public static Candidates electionLoser() {

		Candidates losing = candidateList.get(0);

		electionHelper(0, candidateList.size());

		for (Candidates candidate : candidateList) {

			if(losingCandidates.isMember(candidate)) 
			{
				continue;
			}


			if(candidate.getAmountOfOnes() < losing.getAmountOfOnes()) {

				losing = candidate;
			}
			else if (candidate.getAmountOfOnes() == losing.getAmountOfOnes()) {

				if(candidate.getID() != losing.getID()) {

					losing = getAmountOfNs(candidate, losing, 2);
				}
			}
		}

		return losing;
	}

	public static void electionResults(BufferedWriter bw) throws IOException {

		electionLoser();

		int totalAmountOfOnes = 0;
		int rounds = 1;

		//Total amount of 1s that all candidates have as a collective
		for (Candidates candidates : candidateList) {
			totalAmountOfOnes += candidates.getAmountOfOnes();
		}

		while(!isWinner) {
			for (Candidates candidates : candidateList) 
			{
				if(losingCandidates.isMember(candidates)) 
				{
					continue;
				}
				if(candidates.getAmountOfOnes() > Math.ceil(totalAmountOfOnes/2)) 
				{
					isWinner = true;
					bw.write("Winner: "  + candidates.getName() + " wins with " + candidates.getAmountOfOnes() + " #1's.");
					bw.write(" ");

					return;
				}
			}

			Candidates loser = electionLoser();

			bw.write("Round " + rounds + ": " + loser.getName() + " was eliminated with " + loser.getAmountOfOnes() + " #1's");

			bw.write(" ");


			totalAmountOfOnes -= loser.getAmountOfOnes();

			losingCandidates.add(loser);

			rounds++;
		}
	}

	public static Candidates getAmountOfNs(Candidates can1, Candidates can2, int N) {

		Candidates candidateToEliminate = null;
		int can1AmountOfNs = 0;
		int can2AmountOfNs = 0;

		if(N == candidateList.size())
		{
			if(can1.getID() > can2.getID()) 
			{
				return can1;
			}
			else 
			{
				return can2;
			}

		}

		for (Set set : newS) 
		{
			for (Object object : set) {
				Ballot newBallot = (Ballot) object;
				if(newBallot.getAmountOfN(can1.getID(), N))
				{
					can1AmountOfNs++;
				}	
			}
		}

		for (Set set : newS) 
		{
			for (Object object : set) {
				Ballot newBallot = (Ballot) object;
				if(newBallot.getAmountOfN(can2.getID(), N))
				{
					can2AmountOfNs++;
				}	
			}
		}

		if(can1AmountOfNs == can2AmountOfNs)
			getAmountOfNs(can1, can2, ++N);

		if(can1AmountOfNs > can2AmountOfNs) 
		{
			candidateToEliminate = can1;
		}
		else 
		{
			candidateToEliminate = can2;
		}

		return candidateToEliminate;
	}

}



