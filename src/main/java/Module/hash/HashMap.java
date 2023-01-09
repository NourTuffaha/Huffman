package Module.hash;

/**
 * The class that used to create module.huffmandemo.hash table for String data
 *
 * We implement this class to create closed hashing , and solve The collision by
 * Quadratic probing
 *
 * The hashing is used to make an insert , find , delete as constant time in
 * average and O(n) in worst case if we have many collision in the same position
 * Or reHashing
 *
 * @author Mahran Yacoub
 *
 */
public class HashMap<K, V> {

	private HashNode<K, V>[] hashTable;

	private int sizeOfHashTable;

	private int currentSize = 0;

	/**
	 * A constructor that used to make a module.huffmandemo.hash table with input size
	 *
	 * @param size The size of module.huffmandemo.hash Table
	 *
	 */
	@SuppressWarnings("unchecked")
	public HashMap(int size) {

		this.sizeOfHashTable = size;
		hashTable = new HashNode[size];

	}

	public HashNode<K, V>[] getHashTable() {
		return hashTable;
	}

	/**
	 * This method is used to add a string data to module.huffmandemo.hash table
	 *
	 * @param key The data that will be added to module.huffmandemo.hash Table
	 *
	 */
	public void put(K key, V value) {

		if (sizeOfData() < sizeOfHashTable) {

			int index = key.hashCode() % sizeOfHashTable;

			for (int i = 0; i < sizeOfHashTable; i++) {

				int pos = (index + i) % sizeOfHashTable;

				if (hashTable[pos] == null) {

					hashTable[pos] = new HashNode<K, V>(key, value, 1);
					currentSize++;
					break;
				}
			}
		} else {

			reHashing();
			put(key, value);
		}

	}

	/**
	 * This method is used to remove a string data from module.huffmandemo.hash table
	 *
	 * @param key The data that will be removed from module.huffmandemo.hash Table
	 *
	 */
	public boolean remove(K key) {

		if (isContains(key)) {

			int index = key.hashCode() % sizeOfHashTable;

			for (int i = 0; i < sizeOfHashTable; i++) {

				int pos = (index + i) % sizeOfHashTable;

				if (hashTable[pos] != null && hashTable[pos].getStatus() == 1 && hashTable[pos].getKey().equals(key)) {

					hashTable[pos].setKey(null);
					hashTable[pos].setValue(null);
					hashTable[pos].setStatus(2);
					currentSize--;
					return true;

				} else if (hashTable[pos] == null) {

					return false;
				} else {
					continue;
				}
			}

			return false;

		} else {
			return false;
		}

	}

	/**
	 * This method is used to check if a string data is exist in module.huffmandemo.hash table or not
	 *
	 * @param key The data that will be searched in module.huffmandemo.hash Table
	 *
	 * @return true:exist false:not exist
	 *
	 */
	public boolean isContains(K key) {

		int index = key.hashCode() % sizeOfHashTable;

		for (int i = 0; i < sizeOfHashTable; i++) {
			int pos = (index + i) % sizeOfHashTable;
			if (hashTable[pos] != null && hashTable[pos].getStatus() == 1 && hashTable[pos].getKey().equals(key)) {

				return true;

			} else if (hashTable[pos] == null) {

				return false;
			} else {
				continue;
			}
		}

		return false;

	}


	/**
	 *
	 * @return ths size of data (full locations) in module.huffmandemo.hash table
	 */
	public int sizeOfData() {

		return currentSize;

	}

	/**
	 * This method is used to clear the data from module.huffmandemo.hash table
	 */
	@SuppressWarnings("unchecked")
	public void clear() {

		hashTable = new HashNode[sizeOfHashTable];
		currentSize = 0;
	}

	/**
	 * This method is used to make
	 *
	 * rehashing : refill the data in new Hash table with large size that the first
	 * one
	 *
	 *
	 */
	public void reHashing() {

		int size = hashTable.length * 2;

		HashMap<K, V> h = new HashMap<K, V>(size);

		for (int i = 0; i < hashTable.length; i++) {

			if (hashTable[i] != null) {

				h.put(hashTable[i].getKey(), hashTable[i].getValue());

			}
		}

		hashTable = h.hashTable;
		this.sizeOfHashTable = hashTable.length;

	}


	/**
	 * This method is used to find the position of input data we make this method to
	 * use after chech if the data is exist in module.huffmandemo.hash table
	 *
	 * @param key the data that will find its position in module.huffmandemo.hash table
	 *
	 * @return the position of input data in module.huffmandemo.hash table
	 *
	 */
	public V find(K key) {

		int index = key.hashCode() % sizeOfHashTable;

		Integer pos = null;

		for (int i = 0; i < sizeOfHashTable; i++) {

			pos = (index + i) % sizeOfHashTable;

			if (hashTable[pos] != null && hashTable[pos].getStatus() == 1 && hashTable[pos].getKey().equals(key)) {

				return hashTable[pos].getValue();

			}
		}

		return null;
	}


}
