package com.heap;

public class MaxHeap {
	public int maxSize;
	public int currentSize;
	public int[] heapArray;
	public int[] fringeValues;

	public MaxHeap(int maxSize, int[] _fringeValues) {
		this.maxSize = maxSize;
		this.currentSize = 0;
		this.heapArray = new int[maxSize];
		this.fringeValues = _fringeValues;
		for (int i = 0; i < this.maxSize; i++)
			heapArray[i] = i;
	}

	public int peekMaximum() {
		return heapArray[0];
	}

	public int pollMaximum() {
		heapify();
		int rv = heapArray[0];
		delete();
		return rv;
	}

	public void insert(int val) {
		heapArray[currentSize++] = val;
	}

	public void delete() {
		int tmp = heapArray[0];
		heapArray[0] = heapArray[currentSize - 1];
		heapArray[currentSize - 1] = tmp;
		currentSize = currentSize - 1;
	}

	/* Function to build a heap */
	public void heapify() {
		if (currentSize > 1) {
			for (int i = (currentSize - 2) / 2; i >= 0; i--)
				heapyfyUtil(i);
		}

	}

	private int leftChild(int index) {
		return (2 * index) + 1;
	}

	private int rightChild(int index) {
		return (2 * index) + 2;
	}

	public void heapyfyUtil(int i) {
		int left = leftChild(i);
		int right = rightChild(i);
		int largest = -1;
		if (left < currentSize && fringeValues[heapArray[left]] > fringeValues[heapArray[i]])
			largest = left;
		else
			largest = i;

		if (right < currentSize && fringeValues[heapArray[right]] > fringeValues[heapArray[largest]])
			largest = right;

		if (largest != i) {
			int tmp = heapArray[i];
			heapArray[i] = heapArray[largest];
			heapArray[largest] = tmp;
		}
	}
}