package Module.Huffman;

public class HuffNode implements Comparable<HuffNode> {

	private int frequency;
	private HuffNode left;
	private HuffNode right;
	private int character = 256;

	public HuffNode(int character, int frequency, HuffNode left, HuffNode rigth) {

		this.character = character;
		this.frequency = frequency;
		this.left = left;
		this.right = rigth;

	}

	public HuffNode(int frequency, HuffNode left, HuffNode rigth) {

		this.frequency = frequency;
		this.left = left;
		this.right = rigth;

	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public HuffNode getLeft() {
		return left;
	}

	public void setLeft(HuffNode left) {
		this.left = left;
	}

	public HuffNode getRight() {
		return right;
	}

	public void setRight(HuffNode right) {
		this.right = right;
	}


	public int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}

	public boolean isLeaf() {

		return this.left == null && this.right == null;
	}

	@Override
	public int compareTo(HuffNode o) {

		if (frequency > o.frequency) {

			return 1;

		} else if (frequency < o.frequency) {

			return -1;

		} else {

			return 0;

		}

	}

	@Override
	public String toString() {

		if (this.left != null && this.right != null) {

			return "[ " + this.character + ", " + this.frequency + " ] \n" + this.left.toString()
					+ this.right.toString();

		} else {

			return "[" + this.character + ", " + this.frequency + " ] \n" ;
		}

	}

}
