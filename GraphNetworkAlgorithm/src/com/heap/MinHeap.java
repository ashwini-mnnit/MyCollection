package com.heap;

/**
 * @author Ashwini Singh This is a minHeap structure can be used in our
 *         algorithm. This is referenced from
 *         http://www.sanfoundry.com/java-program-implement-min-heap/
 * 
 */
public class MinHeap {
	private int maxSize;
	private int size;
	private int[] arrayH;
	private int[] arrayD;
	private int[] nodeHeap;

	private static final int FRONT = 1;
	private static final int INFINITY = Integer.MIN_VALUE;

	public MinHeap(int size) {
		super();
		this.maxSize = size;
		this.size = 0;
		this.arrayD = new int[maxSize + 1];
		this.arrayH = new int[maxSize + 1];
		this.nodeHeap = new int[maxSize + 1];
		for (int i = 0; i < maxSize; i++) {
			this.arrayD[i + 1] = INFINITY;
			this.arrayH[i + 1] = i;
		}
	}

	public void heapify() {
		heapifyutil(FRONT);

	}

	public int getminimum() {
		return getNodeValue(nodeHeap[FRONT]);
	}

	// remove from front
	public int delete() {
		int rv = getNodeValue(nodeHeap[FRONT]);
		arrayD[nodeHeap[FRONT] + 1] = INFINITY;

		nodeHeap[FRONT] = nodeHeap[size--];
		heapify();
		return rv;
	}

	private int getNodeValue(int node) {
		return arrayD[node + 1];
	}

	public void insert(int name, int value) {
		this.arrayD[name + 1] = value;
		nodeHeap[++size] = name;

		int start = size;
		while (start !=1 && getNodeValue(nodeHeap[start]) < getNodeValue(nodeHeap[parent(start)])) {
			swap(start, parent(start));
			start = parent(start);
		}
	}

	public void printHeap() {
		for (int i = 1; i <= size / 2; i++) {
			System.out.print(" PARENT : " + nodeHeap[i] + "(" + getNodeValue(nodeHeap[i]) + ")" + " LEFT CHILD : " + nodeHeap[2 * i] + "(" + getNodeValue(nodeHeap[2 * i]) + ")" + " RIGHT CHILD :" + nodeHeap[2 * i + 1] + "(" + getNodeValue(nodeHeap[2 * i + 1]) + ")");
			System.out.println();
		}
	}

	private boolean isLeaf(int index) {
		if (index > (size / 2) && index <= size) {
			return true;
		}
		return false;
	}

	private void heapifyutil(int i) {
		if (isLeaf(i))
			return;

		int smallest = -1;
		int left = leftChild(i);
		int right = rightChild(i);

		if (left <= size && getNodeValue(nodeHeap[left]) < getNodeValue(nodeHeap[i]))
			smallest = left;
		else
			smallest = i;

		if (right <= size && getNodeValue(nodeHeap[right]) < getNodeValue(nodeHeap[smallest]))
			smallest = right;

		if (smallest != i) {
			swap(smallest, i);
			heapifyutil(smallest);
		}

	}

	public void printD() {
		System.out.print("[ ");
		for (int i = 0; i < maxSize; i++) {
			System.out.print(this.arrayD[i] + ", ");
		}
		System.out.println(" ]");
	}

	public void printnodeHeap() {
		System.out.print("[ ");
		for (int i = 0; i < maxSize; i++) {
			System.out.print(getNodeValue(this.nodeHeap[i]) + ", ");
		}
		System.out.println(" ]");
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private int parent(int index) {
		return index / 2;
	}

	private int leftChild(int index) {
		return (2 * index);
	}

	private int rightChild(int index) {
		return (2 * index) + 1;
	}

	private void swap(int i, int j) {
		int tmp;
		tmp = nodeHeap[i];
		nodeHeap[i] = nodeHeap[j];
		nodeHeap[j] = tmp;
	}

	public static void main(String[] args) {
		System.out.println("The Min Heap is ");
		MinHeap minHeap = new MinHeap(10);
		minHeap.insert(1, 5);
		minHeap.insert(0, 4);
		minHeap.insert(2, 3);
		minHeap.insert(4, 7);
		minHeap.insert(3, 1);
		minHeap.insert(5, 17);
		minHeap.insert(6, 10);
		minHeap.insert(8, 84);
		minHeap.insert(7, 19);
		minHeap.insert(9, 22);
		minHeap.heapify();

		while (!minHeap.isEmpty()) {
			// minHeap.printHeap();
			System.out.println("The Min val is " + minHeap.getminimum());
			minHeap.delete();

		}
		minHeap.printHeap();
	}

	public int[] getArrayH() {
		return arrayH;
	}

	public void setArrayH(int[] arrayH) {
		this.arrayH = arrayH;
	}

	public int[] getArrayD() {
		return arrayD;
	}

	public void setArrayD(int[] arrayD) {
		this.arrayD = arrayD;
	}
}
