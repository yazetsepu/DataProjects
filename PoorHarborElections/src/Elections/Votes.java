package Elections;
/**
 * <h1>Votes class</h1>
 * <p>
 * The Votes class is the class in charge of creating vote objects that are meant to store single votes found in every cell of the csv file 
 * that corresponds to a ballot.
 * </p>
 * 
 * <P> 
 * Each vote found is classified with an id, corresponding to a candidate id, and a rank corresponding to the voters preference of said candidate
 * This class contains basic getters and setters to easily retrieve the data stored in individual votes
 * </p>
 * 
 * 
 * @author Yazet Sepulveda 
 * @version 1.0
 * @since 2020-03-13
 */


public class Votes {
	private int ID;
	private int rank;

	public Votes (int ID, int rank) {
		this.ID = ID;
		this.rank = rank;
	}
	
	public int getID () {
		return this.ID;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
}
