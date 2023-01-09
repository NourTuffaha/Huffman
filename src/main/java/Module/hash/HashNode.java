package Module.hash;

/**
 * The Class will be used to make the module.huffmandemo.hash table array
 * It is contains data and status as Data field
 *
 */

public class HashNode<K,V> {

	private K key ;
	private V value ;
	private int status ;

	public HashNode() {


	}

	public HashNode(K key , V value , int status) {
		this.key = key ;
		this.value = value ;
		this.status = status ;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}





}
