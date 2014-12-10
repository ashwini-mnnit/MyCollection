package com.routing.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import com.graph.Graph;
import com.graph.GraphNode;
import com.heap.MaxHeap;

public class DijkstraModified {

	private int[] dadArray;
	private int[] status;
	private int[] distance;
	private int[] inserted;

	int graphsize;

	private static final int StatusUNSEEN = 1;
	private static final int StatusFRINGE = 2;
	private static final int StatusINTREE = 3;

	public DijkstraModified(int graphsize) {
		super();
		this.graphsize = graphsize;
		this.dadArray = new int[graphsize];
		this.status = new int[graphsize];
		this.distance = new int[graphsize];
		this.inserted = new int[graphsize];

		for (int i = 0; i < graphsize; i++) {
			inserted[i] = -1;
			distance[i] = Integer.MAX_VALUE;
			dadArray[i] = -1;
			status[i] = StatusUNSEEN;
		}
	}

	@Override
	public String toString() {
		return "DijkstraModified [dadArray=" + Arrays.toString(dadArray) + ", status=" + Arrays.toString(status) + ", distance=" + Arrays.toString(distance) + ", inserted=" + Arrays.toString(inserted) + ", graphsize=" + graphsize + "]";
	}

	public GraphNode getMaximumFringe(List<GraphNode> fringeList) {
		GraphNode rv_node = null;
		int max = Integer.MIN_VALUE;
		for (GraphNode graphNode : fringeList) {

			if (distance[graphNode.getVal()] > max && status[graphNode.getVal()] != StatusINTREE) {
				rv_node = graphNode;
				max = distance[graphNode.getVal()];
			}
		}
		return rv_node;
	}

	public void createMaxBandwidthPathWithoutHeap(Graph graph, GraphNode source) {

		List<GraphNode> fringeList = new ArrayList<GraphNode>();
		status[source.getVal()] = StatusINTREE;
		dadArray[source.getVal()] = -1;// root
		distance[source.getVal()] = 0;

		for (int i = 0; i < source.getNeighbour().size(); i++) {

			GraphNode node = source.getNeighbour().get(i);
			int edgeWeight = source.getWeightList().get(i);
			status[node.getVal()] = StatusFRINGE;
			dadArray[node.getVal()] = source.getVal();
			distance[node.getVal()] = edgeWeight;
			fringeList.add(node);
		}

		while (IsfringeNodePresent(fringeList)) {
			GraphNode node = getMaximumFringe(fringeList);
			// Get the actual node rather than the node in the list.
			GraphNode processingNode = graph.getAdjList().get(node.getVal());

			status[processingNode.getVal()] = StatusINTREE;

			for (int i = 0; i < processingNode.getNeighbour().size(); i++) {

				GraphNode neighbour = processingNode.getNeighbour().get(i);
				int edgeWeight = processingNode.getWeightList().get(i);

				if (status[neighbour.getVal()] == StatusUNSEEN) {
					status[neighbour.getVal()] = StatusFRINGE;
					dadArray[neighbour.getVal()] = processingNode.getVal();
					distance[neighbour.getVal()] = Math.min(distance[processingNode.getVal()], edgeWeight);
					fringeList.add(neighbour);

				} else if (status[neighbour.getVal()] == StatusFRINGE && (distance[neighbour.getVal()] < Math.min(distance[processingNode.getVal()], edgeWeight))) {
					distance[neighbour.getVal()] = Math.min(distance[processingNode.getVal()], edgeWeight);
					dadArray[neighbour.getVal()] = processingNode.getVal();
				}
			}

		}

	}

	public void createMaxBandwidthPathWithHeap2(Graph graph, GraphNode source) {

		MaxHeap fringePriorityQueue = new MaxHeap(graph.getSize(), distance);
		// List<GraphNode> fringeList = new ArrayList<GraphNode>();
		status[source.getVal()] = StatusINTREE;
		dadArray[source.getVal()] = -1;// root
		distance[source.getVal()] = 0;
		

		for (int i = 0; i < source.getNeighbour().size(); i++) {

			GraphNode node = source.getNeighbour().get(i);
			int edgeWeight = source.getWeightList().get(i);
			status[node.getVal()] = StatusFRINGE;
			dadArray[node.getVal()] = source.getVal();
			distance[node.getVal()] = edgeWeight;
			node.setFringeValue(distance[node.getVal()]);
			fringePriorityQueue.insert(node.getVal());
		}

		while (!(fringePriorityQueue.currentSize == 0)) {
			int node = fringePriorityQueue.pollMaximum();
			
			GraphNode processingNode = graph.getAdjList().get(node);
			status[processingNode.getVal()] = StatusINTREE;

			for (int i = 0; i < processingNode.getNeighbour().size(); i++) {

				GraphNode neighbour = processingNode.getNeighbour().get(i);
				int edgeWeight = processingNode.getWeightList().get(i);

				if (status[neighbour.getVal()] == StatusUNSEEN) {
					status[neighbour.getVal()] = StatusFRINGE;
					dadArray[neighbour.getVal()] = processingNode.getVal();
					distance[neighbour.getVal()] = Math.min(distance[processingNode.getVal()], edgeWeight);
					neighbour.setFringeValue(distance[neighbour.getVal()]);
					
					fringePriorityQueue.insert(neighbour.getVal());

				} else if (status[neighbour.getVal()] == StatusFRINGE && (distance[neighbour.getVal()] < Math.min(distance[processingNode.getVal()], edgeWeight))) {
					distance[neighbour.getVal()] = Math.min(distance[processingNode.getVal()], edgeWeight);
					dadArray[neighbour.getVal()] = processingNode.getVal();
					neighbour.setFringeValue(distance[neighbour.getVal()]);
				}
			}

		}

	}

	
	private boolean IsfringeNodePresent(List<GraphNode> fringeList) {
		for (GraphNode graphNode : fringeList) {
			if (status[graphNode.getVal()] == StatusFRINGE)
				return true;
		}
		return false;
	}

	// Comparator for GraphNode class implementation
	public static Comparator<GraphNode> graphFringeComparator = new Comparator<GraphNode>() {

		@Override
		public int compare(GraphNode n1, GraphNode n2) {
			return (int) (n2.getFringeValue() - n1.getFringeValue());
		}
	};

	public void createMaxBandwidthPathWithHeap(Graph graph, GraphNode source) {

		Queue<GraphNode> fringePriorityQueue = new PriorityQueue<GraphNode>(graph.getSize(), graphFringeComparator);

		// List<GraphNode> fringeList = new ArrayList<GraphNode>();
		status[source.getVal()] = StatusINTREE;
		dadArray[source.getVal()] = -1;// root
		distance[source.getVal()] = 0;

		for (int i = 0; i < source.getNeighbour().size(); i++) {

			GraphNode node = source.getNeighbour().get(i);
			int edgeWeight = source.getWeightList().get(i);
			status[node.getVal()] = StatusFRINGE;
			dadArray[node.getVal()] = source.getVal();
			distance[node.getVal()] = edgeWeight;
			node.setFringeValue(distance[node.getVal()]);
			fringePriorityQueue.add(node);
		}

		while (IsfringeNodePresentInPriorityQueue(fringePriorityQueue)) {
			GraphNode node = fringePriorityQueue.poll();
			GraphNode processingNode = graph.getAdjList().get(node.getVal());
			status[processingNode.getVal()] = StatusINTREE;

			for (int i = 0; i < processingNode.getNeighbour().size(); i++) {

				GraphNode neighbour = processingNode.getNeighbour().get(i);
				int edgeWeight = processingNode.getWeightList().get(i);

				if (status[neighbour.getVal()] == StatusUNSEEN) {
					status[neighbour.getVal()] = StatusFRINGE;
					dadArray[neighbour.getVal()] = processingNode.getVal();
					distance[neighbour.getVal()] = Math.min(distance[processingNode.getVal()], edgeWeight);
					neighbour.setFringeValue(distance[neighbour.getVal()]);
					fringePriorityQueue.add(neighbour);

				} else if (status[neighbour.getVal()] == StatusFRINGE && (distance[neighbour.getVal()] < Math.min(distance[processingNode.getVal()], edgeWeight))) {
					distance[neighbour.getVal()] = Math.min(distance[processingNode.getVal()], edgeWeight);
					dadArray[neighbour.getVal()] = processingNode.getVal();
					fringePriorityQueue.remove(neighbour);
					neighbour.setFringeValue(distance[neighbour.getVal()]);
					fringePriorityQueue.add(neighbour);

				}
			}

		}

	}

	private boolean IsfringeNodePresentInPriorityQueue(Queue<GraphNode> fringePriorityQueue) {
		for (GraphNode graphNode : fringePriorityQueue) {
			if (status[graphNode.getVal()] == StatusFRINGE)
				return true;
		}
		return false;
	}

	public void printPathFromSourceToDest(int source, int destination) {
		int parent = destination;
		Stack<Integer> path = new Stack<Integer>();
		while (parent != source) {
			path.add(parent);
			int nextParent = dadArray[parent];
			parent = nextParent;
		}
		path.add(source);

		while (true) {
			System.out.print(path.pop());
			if (!path.empty())
				System.out.print("--->");
			else
				break;
		}
		System.out.println("\n");
	}

	public int getMaximimCapacity(Graph g, int source, int destination) {
		int rv = Integer.MAX_VALUE;
		int parent = destination;
		while (parent != source) {
			int weight = g.getEdgeWeight(dadArray[parent], parent);
			if (weight < rv) {
				rv = weight;
			}
			int nextParent = dadArray[parent];
			parent = nextParent;
		}
		return rv;

	}
}
