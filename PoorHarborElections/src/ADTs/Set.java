package ADTs;

public interface Set<E> extends Iterable<E>{

	//Set Manipulations
	public boolean add(E e);
	
	public boolean remove(E e);
	
	public void clear();
	
	//Set Operations
	public Set<E> intersection (Set<E> e);
	
	public Set<E> union(Set<E> e);
	
	public Set<E> difference(Set<E> e);
	
	//Set Tests
	public boolean isEmpty();
	
	public int size();
	
	public boolean isMember(E e);
	
	public boolean isSubset(Set<E> e);

	public void printList();

	boolean equals(Set<E> S2);

}
