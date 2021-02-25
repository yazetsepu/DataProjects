package ADTs;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicSet<E> implements Set<E> {

	private int currentSize;
	private int DEFAULT_CAPACITY;
	private E elements[];

	@SuppressWarnings("unchecked")
	public DynamicSet(int initialCapacity ) {

		if (initialCapacity < 1)
			throw new IllegalArgumentException();

		this.currentSize = 0;
		this.DEFAULT_CAPACITY = initialCapacity;
		this.elements = (E[]) new Object[DEFAULT_CAPACITY];

	}

	private class SetIterator implements Iterator<E> {
		private int currentPosition;

		public SetIterator() {
			this.currentPosition = 0;
		}

		@Override
		public boolean hasNext() {
			return this.currentPosition < size();
		}

		@Override
		public E next() {
			if (this.hasNext()) {
				E result = (E) elements[this.currentPosition++];
				return result;
			}
			else
				throw new NoSuchElementException();				
		}
	}

	@Override
	public boolean add(E e) {

		if(this.currentSize == DEFAULT_CAPACITY) {
			E newSet[] = (E[]) new Object[DEFAULT_CAPACITY*2];

			DEFAULT_CAPACITY = DEFAULT_CAPACITY*2;

			this.copySet(newSet);
			this.elements = newSet;
		}

		if(this.isMember(e)) {
			System.out.println("---------------------------------------------");
			System.out.println("Object "+ e +" is already part of the set!!!");
			System.out.println("---------------------------------------------");
			
			return false;
		}

		this.elements[currentSize++] = e;

		return true;
	}

	@Override
	public boolean isMember(E obj) {
		for (int i = 0; i < this.size(); i++)
			if (this.elements[i].equals(obj))
				return true;
		return false;
	}

	@Override
	public boolean remove(E obj) {
		if(!this.isMember(obj)) {
			System.out.println("Object " + obj + " is not in the set.");
			return false;
		}
		for (int i = 0; i < this.size(); i++)
			if (this.elements[i].equals(obj))
			{
				this.elements[i] = this.elements[--this.currentSize];
				this.elements[this.currentSize] = null;
				return true;
			}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public int size() {
		return this.currentSize;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.size(); i++)
			this.elements[i] = null;
		this.currentSize = 0;

	}

	@Override
	public Set<E> union(Set<E> S2) {
		Set<E> S3 = new DynamicSet<E>(DEFAULT_CAPACITY);
		// Copy S1
		for (E e : this)
			S3.add(e);

		// Copy elements of S2 not already in S1
		for (E e : S2)
		{
			if (!S3.isMember(e))
				S3.add(e);
		}

		return S3;
	}

	@Override
	public Set<E> difference(Set<E> S2) {
		Set<E> S3 = new DynamicSet<E>(DEFAULT_CAPACITY);

		for (E e : this)
		{
			if (!S2.isMember(e))
				S3.add(e);
		}
		return S3;
	}

	@Override
	public Set<E> intersection(Set<E> S2) {
		return this.difference(this.difference(S2));
	}

	@Override
	public boolean equals(Set<E> S2) {
		//check if the the target set is a subset of the parameter set (which means parameter set contains target set in its entirety)
		//and then check if their sizes are the same, if both conditions are met, return true, otherwise, return false
		if(this.isSubset(S2) && this.size() == S2.size())
			return true;

		return false;
	}

	public static boolean checkDisjoint (Object[] sets) {
		//Copied from DynamicSet
		Set newSet = (Set) sets[0];

		for (int i = 1; i < sets.length; i++) {

			newSet =  newSet.intersection((Set)sets[i]);

		}

		if(!newSet.isEmpty())
			return false;

		return true;
	}

	private void copySet(E[] newSet) {
		for (int i = 0; i < this.size(); i++) {
			newSet[i] = this.elements[i];
		}

	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new SetIterator();
	}

	@Override
	public void printList() {

		if(this.isEmpty()) {
			System.out.println("-------------------------");
			System.out.println("The set is empty!!!");
			System.out.println("-------------------------");
			return;
		}

		System.out.println("---------------------------------------------");
		for (int i = 0; i < this.size(); i++) {
			System.out.println(this.elements[i]);
		}
		System.out.println("");
		System.out.println("The currentSize of the set is " + this.size());
		System.out.println("The max allocation currently available are: " + DEFAULT_CAPACITY);
		System.out.println("---------------------------------------------");

	}

	@Override
	public boolean isSubset(Set<E> e) {
		for (E a : this)
			if (!e.isMember(a))
				return false;
		
		return true;
	}
}
