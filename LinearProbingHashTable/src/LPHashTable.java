import java.io.IOException;

/*
Project #3
Due Dates:  Wednesday, March 25 at 11:59pm 
Submit:    eLearning
Late Policy:  -10 points per hour late
Instructions: This is an individual assignment.  All work should be your own.

Objective:
     Work with hash tables by creating a hash table using linear probing.

Submit to eLearning:
     LinearProbingHashTable.java
*/

@SuppressWarnings("unused")
public class LPHashTable<K,V> {

	private static final int DEFAULT_SIZE = 4;
	int size;			// Size of the table
	int N;				// Number of elements in the table
	Entry<K,V> table[];	// Array storing the Entries themselves
	
	@SuppressWarnings("unchecked")
	LPHashTable(){
		size = DEFAULT_SIZE;
		table = new Entry[size];
	}
	
	@SuppressWarnings("unchecked")
	LPHashTable(int s){
		size = s;
		table = new Entry[size];
	}
	
	// inserts entry, rehashes if half full, can re-use deleted entries, throws exception if key is null, returns true if inserted, false if duplicate.
	public boolean insert(K key, V value){ 
		Entry<K,V> newEntry = new Entry<K,V>(key, value);
		
		// If the key is null, throw an exception
		if(key == null)		throw new NullPointerException("Key must not be null"); 
		
		// If the number of entries in the hash table is greater than half the size, rehash the table
		if(N >= size/2)		rehash();
		
		// Going through the hash table to find the place to insert it.
		for(int x=getHashValue(key); x < size; x = (x+1)%size)
		{	
			// If the current place we are at already has something in it.
			if( table[x] != null)
			{
				// If the value to be inserted already exists, end the loop
				if(table[x].value == value){
					System.out.println(value.toString() + " is a duplicate value");
					return false;
				}
				
				// If the place where we
				if(table[x].key == newEntry.key)
				{ 
					N++;
					table[x].value = newEntry.value;
					return true; 
				}
			}
			else{
				N++;
				table[x] = newEntry;
				return true; 
			}
		}
		
		return false;
		
	}
	
	// returns value for key, or null if not found
	public V find(K key){
		System.out.println("Finding the value associated with '" + key.toString() + "'");
		
        for (int i = getHashValue(key); table[i] != null; i = (i + 1) % size) 
            if (table[i].key == key)
                return table[i].value;
       
        System.out.println("The value associated with '" + key.toString() + "' was not found");
        return null;
	}
	
	// marks the entry deleted but leaves it there, returns true if deleted, false if not found
	public boolean delete(K key){
	       if (find(key) == null) 
	    	   return false;

	        // find position i of key
	        int i = getHashValue(key);
	        
	        while (!key.equals(table[i].key)) {
	            i = (i + 1) % size;
	        }

	        // delete key and associated value
	        table[i] = null;

	        // rehash all keys in same cluster
	        N--;        
	        rehash();
	        
	        return true;
	}
	
	// doubles the table size, hashes everything to the new table, omitting items marked deleted
	private void rehash(){
		
		LPHashTable<K, V> temp;
		
		if(N > size/2)
			temp = new LPHashTable<K, V>(size*2);
		else if(N < size/4)
			temp = new LPHashTable<K, V>(size/2);
		else
			temp = new LPHashTable<K, V>(size);
		
        for (int i = 0; i < size; i++) {
            if (table[i] != null) {
                temp.insert(table[i].key, table[i].value);
            }
        }
        size = temp.size;
        table = temp.table;
	}
	
	// returns the hash value for the given key, or -1 if not found. (this is the value before probing occurs)
	public int getHashValue(K key){
        return (key.hashCode() & 0x7fffffff) % size;
	}
	
    // returns the location for the given key, or -1 if not found. (this is the value after probing occurs)
	public int getLocation(K key){
		for(int i = 0; i < size; i++){
			if(table[i] != null)
				if(table[i].key == key)
					return i;
		}
		return -1;
			
	}
	
	/* returns a formatted string of the hash table:
      *  0  null
      *  1  xxxxx
      *  2  yyyyy
      *  ...
	*/
	public String toString(){
	
		System.out.println("Printing hash table");
		
		for(int x =0; x<size; x++){
			if(table[x] != null)
				System.out.println(" " + table[x].key + " " + table[x].value);
		}
		
		return "";
	}
	
	private static class Entry<K,V>{
		K key;
		V value;
		
		@SuppressWarnings("unused")
		Entry(){
			key = null;
			value = null;
		}
		Entry(K k, V v){
			key = k;
			value = v;
		}
	}
	
	public static void main(String[] args) {

		// Creating a new Linear Probing Hash Table
		LPHashTable<String, Integer> test = new LPHashTable<String, Integer>();
		
		// Inserting several new keys and values
		test.insert("Hello" , 6);
		test.insert("Goodbye", 28);
		test.insert("Maybe", 99);
		test.insert("Definitely", 11);
		test.insert("Forsure", 1414);
		
		// Inserting a duplicate, should not be inserted
		test.insert("However", 11);
		
		// Print out Hash Table
		test.toString();
		
		// Prints out what it found
		test.find("Goodbye");
		
		// Prints out what it did not find. 
		test.find("Blkahblha");
		
		// Deletes "Hello" from the table
		test.delete("Hello");
		test.toString();
		
		// Prints out the size of the hash table
		System.out.println(test.size);
		
		// Finds the location of "Definitely" in the has table
		System.out.println(test.getLocation("Definitely"));
		
		System.out.println("end of program");
	}

}