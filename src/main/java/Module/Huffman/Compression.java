package Module.Huffman;

import Module.Row;
import Module.hash.HashNode;
import Module.hash.HashMap;
import Module.heap.HeapNode;
import Module.heap.PriorityQueue;
import Module.io.stream.BitInputStream;
import Module.io.stream.BitOutputStream;

import java.io.File;
import java.util.ArrayList;

public class Compression {

    /**
     * An array that is used to store frequencies of a character/bytes in original
     * file
     */
    static int[] num;

    /**
     * Number of Distinct char in an original file
     */
    private int countDub;

    /**
     * in : used to read from a file as bytes
     */
    private BitInputStream inputStream;

    /**
     * out : is used to write on a file as bytes
     */
    private BitOutputStream outputStream;

    private int charsInFile;

    private int charsInHeader;

    private int charsInEncoding;

    private final StringBuilder encodedOfHeader = new StringBuilder();

    /**
     * This ArrayList will store rows of TableView
     */
    private final ArrayList<Row> rows = new ArrayList<Row>();

    private String extension;

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method is used to count frequencies of char in original file and store
     * it in a frequencies array (in above data fields )
     *
     **/
    private void charactersCount(File original) {

        num = new int[256];

        // Initialize an in to original file
        inputStream = new BitInputStream(original);

        int character;

        /*
         * The loop will terminate when a file ends / reach -1
         *
         * The read(number of bits) return int type that represent unsigned of read
         * bytes (8 bits as input)
         *
         */
        while ((character = inputStream.readBits(8)) != -1) {

            num[character]++;

            if (num[character] == 1) {

                countDub++;

            }

            charsInFile++;

        }

        // This method will return in stream to read from a begin of a file
        inputStream.reset();

        this.extension = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf('.') + 1);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method will use to create a huffman tree used a priority queue (Minimum
     * Heap) Depends on frequency array that fill in first step of compression
     * process.
     * <p>
     * module.huffmandemo.Huffman tree : will store each character in int type (code points / unsigned
     * bytes) and frequency of each character in leaves and the remains nodes will
     * be store 256 as character code point and 0 as frequency.
     * <p>
     * we will add an additional node to a tree
     *
     * @param count : frequency array that fill in first step of compression process.
     * @return The node that represent a huffman tree
     */
    private HuffNode createTree(int[] count) {

        PriorityQueue<HuffNode> huffTree = new PriorityQueue<>(countDub);

        // Add each character in Original file in Heap as Node
        for (int i = 0; i < count.length; i++) {

            if (count[i] > 0) {

                int frequency = count[i];
                HeapNode<HuffNode> heapNode = new HeapNode<HuffNode>();
                HuffNode huffNode = new HuffNode(i, frequency, null, null);
                heapNode.setKey(huffNode);
                huffTree.insert(heapNode);
            }

        }

        /*
         * A loop #nodes -1 (countDistinct +1 -1 = countDistinct) to build huffman tree
         * depends of the first loop.
         *
         * we pop two nodes and make new node with left: first pop , right :second pop
         * each iteration
         *
         *
         */
        for (int i = 0; i < countDub - 1; i++) {

            HeapNode<HuffNode> heapNode1 = huffTree.delete();
            HuffNode huffNode1 = heapNode1.getKey();
            HeapNode<HuffNode> heapNode2 = huffTree.delete();
            HuffNode huffNode2 = heapNode2.getKey();

            HeapNode<HuffNode> heapNode3 = new HeapNode<HuffNode>();
            HuffNode huffNode3 = new HuffNode(huffNode1.getFrequency() + huffNode2.getFrequency(), huffNode1, huffNode2);
            heapNode3.setKey(huffNode3);

            huffTree.insert(heapNode3);

        }

        return huffTree.delete().getKey();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 3rd step of module.huffmandemo.Huffman compression Process These two method is used to read a
     * module.huffmandemo.Huffman tree in the form of a single HuffNode to build a table Using HashMap
     * data structure contains all of the module.huffmandemo.Huffman codes you will need to compress
     * the body of the file. Rather than querying the tree and extracting a code for
     * every character in the file.
     *
     * @param root         : module.huffmandemo.Huffman tree .
     * @param huffmanCode
     * @param table        : Table that represent a codepoint of character and its
     *                     huffman code.
     */
    private void traverse(HuffNode root, String huffmanCode, HashMap<Integer, String> table) {

        if (root != null) {

            traverse(root.getLeft(), huffmanCode + "0", table);

            if (root.isLeaf()) {

                // When it reach a leaf it will read a code point and put it in a table.

                table.put(root.getCharacter(), huffmanCode);

            }

            traverse(root.getRight(), huffmanCode + "1", table);

        }

    }

    /**
     * This method will create a huffman table depends on module.huffmandemo.Huffman tree that create
     * in second step of compressed process and using a recursive method/travesal
     *
     * @param root : module.huffmandemo.Huffman tree
     * @return a huffman table as HashMap data structure
     */
    private HashMap<Integer, String> buildTable(HuffNode root) {

        HashMap<Integer, String> table = new HashMap<>(countDub);

        if (countDub != 1) {

            traverse(root, "", table);

        } else {

            table.put(root.getCharacter(), "0");
        }

        fillArrayList(table);

        return table;

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method is used to fill ArrayList Object that will be used to fill
     * TableView In GUI .
     *
     * @param table module.huffmandemo.Huffman Table
     */
    private void fillArrayList(HashMap<Integer, String> table) {

        HashNode<Integer, String>[] tableTemp = table.getHashTable();

        for (int i = 0; i < table.sizeOfData(); i++) {

            Character character = (char) tableTemp[i].getKey().intValue();
            String code = tableTemp[i].getValue();
            Integer length = code.length();
            Integer frequency = num[tableTemp[i].getKey()];

            Row row = new Row(character, code, length, frequency);

            rows.add(row);

        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This step will be a 4th step of a compression process it is to write a header
     * that will be used to decompress a file
     * <p>
     * We will write it in preorder root - left - right
     *
     * @param huffmanTree : module.huffmandemo.Huffman tree
     */
    private void encodingHeader(HuffNode huffmanTree) {

        if (huffmanTree != null) {


            if (huffmanTree.isLeaf()) {

                outputStream.writeBits(1, 1);
                charsInHeader++;
                outputStream.writeBits(8, huffmanTree.getCharacter());
                charsInHeader += 8;
                encodedOfHeader.append('1');
                encodedOfHeader.append((char) huffmanTree.getCharacter());

            } else {

                outputStream.writeBits(1, 0);
                charsInHeader++;
                encodedOfHeader.append('0');

            }

            encodingHeader(huffmanTree.getLeft());

            encodingHeader(huffmanTree.getRight());
        }

    }

    /**
     * This method will use to print flag to begin of header on compressed file and
     * flag of end of header on compressed file and header itself between two flag
     * using help recursive method encodingHeader
     *
     * @param huffmanTree
     * @param compreesedFile
     */
    private void printHeaderOnFile(HuffNode huffmanTree, File compreesedFile) {

        outputStream = new BitOutputStream(compreesedFile);

        outputStream.writeBits(32, charsInFile);

        outputStream.writeBits(8, this.extension.length());

        for (int i = 0; i < extension.length(); i++) {

            outputStream.writeBits(8, extension.charAt(i));
        }

        encodingHeader(huffmanTree);

        //out.writeBits(1, 0); // Ends Of Tree Encoding

        //countNumberOfCharInHeader++;

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 5th step of huffman compression process This method is used to print encoding
     * of original file contents based on huffman table by read each byte/character
     * and print its huffman code in compressed file
     *

     * @param huffmanTable
     */
    private void pritnEncodingMessage( HashMap<Integer, String> huffmanTable) {

        int character = 0;
        int count = 0;
        while ((character = inputStream.readBits(8)) != -1) {

            String value = huffmanTable.find(character);
            outputStream.writeBits(value.length(), Integer.parseInt(value, 2));
            count += value.length();

        }

        // #bytes or a character of a message in compressed file
        charsInEncoding = (count / 8);

        if (count % 8 > 0) {

            charsInEncoding++;
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method is used to do 1st , 2nd , 3rd , 4th ,5th steps of compression
     * process (All Process)
     *
     * @param file
     * @param compressedFile
     */
    public void compress(File file, File compressedFile) {

        charactersCount(file);// 1st
        HuffNode huffmanTree = createTree(num);// 2nd
        HashMap<Integer, String> huffmanTable = buildTable(huffmanTree);// 3rd
        printHeaderOnFile(huffmanTree, compressedFile);// 4th
        pritnEncodingMessage(huffmanTable); // 5th;
        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getCountDub() {
        return countDub;
    }

    public int getCountNumberOfCharInOriginalFile() {
        return charsInFile;
    }

    public int getCharsInHeader() {
        return charsInHeader;
    }

    public int getCharsInEncoding() {
        return charsInEncoding;
    }

    public StringBuilder getEncodedOfHeader() {
        return encodedOfHeader;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public String getExtension() {
        return "." + extension;
    }

}
