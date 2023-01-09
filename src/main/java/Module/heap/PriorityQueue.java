package Module.heap;

public class PriorityQueue<T extends Comparable<T>> {

	private HeapNode<T>[] heapArray;
	private int size;

	@SuppressWarnings("unchecked")

	public PriorityQueue(int size) {

		heapArray = new HeapNode[size + 1];
		size = 0;

	}

	private int parentPos(int pos) {

		return pos / 2;
	}

	private int leftChild(int pos) {

		return 2 * pos;
	}

	private int rightChild(int pos) {

		return pos * 2 + 1;
	}

	public boolean isFull() {

		return (size == heapArray.length - 1) ? true : false;
	}

	public void insert(HeapNode<T> input) {

		if (!isFull()) {

			heapArray[size + 1] = input;
			size++;
			int j = size;

			while (j != 1) {

				if (heapArray[j].getKey().compareTo(heapArray[parentPos(j)].getKey()) < 0) {

					swap(j, parentPos(j));

				} else {

					return;
				}

				j = parentPos(j);

			}

		} else {
			System.out.println("Heap is Full");
		}

	}

	private void swap(int j, int parentPos) {

		HeapNode<T> temp = heapArray[j];
		heapArray[j] = heapArray[parentPos];
		heapArray[parentPos] = temp;

	}

	public boolean isLeaf(int j, int length) {

		if (j >= (length / 2) + 1 && j <= length) {
			return true;
		} else {
			return false;
		}
	}

	public void print() {

		for (int i = 1; i < heapArray.length; i++) {
			System.out.println(heapArray[i].getKey());
		}

	}

	public void minHeapify(HeapNode<T>[] a, int size, int i) {

		if (isLeaf(i, size)) {
			return;
		}

		int smallest = i;
		int r = rightChild(i);
		int l = leftChild(i);

		if (r <= size && l <= size) {

			int smallesttChild = (a[r].getKey().compareTo(a[l].getKey()) < 0 ? r : l);

			if (a[smallesttChild].getKey().compareTo(a[i].getKey()) < 0) {

				smallest = smallesttChild;
			}

		} else if (l <= size) {

			if (a[l].getKey().compareTo(a[i].getKey()) < 0) {

				smallest = l;

			}

		}

		if (i != smallest) {

			HeapNode<T> temp = a[i];
			a[i] = a[smallest];
			a[smallest] = temp;

			minHeapify(a, size, smallest);

		}

	}

	public HeapNode<T> delete() {

		HeapNode<T> root = heapArray[1];
		heapArray[1] = heapArray[size];
		heapArray[size] = root;
		size--;

		minHeapify(heapArray, size, 1);

		return heapArray[size + 1];

	}

	public HeapNode<T>[] buildHeap(HeapNode<T>[] array) {

		for (int i = 1; i < array.length; i++) {

			int j = i;

			while (j != 1) {

				if (array[j].getKey().compareTo(array[parentPos(j)].getKey()) < 0) {

					HeapNode<T> temp = array[j];
					array[j] = array[parentPos(j)];
					array[parentPos(j)] = temp;

				} else {

					break;
				}

				j = parentPos(j);

			}

		}

		return array;

	}

	public void sort() {

		while (size != 0) {
			delete();
		}
	}

	public HeapNode<T>[] heapfiy(HeapNode<T>[] a) {

		for (int i = (a.length - 1) / 2; i >= 1; i--) {

			minHeapify(a, a.length - 1, i);
		}

		return a;

	}

}
