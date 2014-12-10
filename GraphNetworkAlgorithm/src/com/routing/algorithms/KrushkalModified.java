package com.routing.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.graph.Graph;
import com.graph.GraphEdge;

public class KrushkalModified {
	private int graphSize;
	private int[] dadArray;
	private int[] rankArray;
	private Graph  graphDFS;

	private int maximumCapicity;
	private List<GraphEdge> maxCapacityGraph;

	public KrushkalModified(int graphSize) {
		super();
		this.maximumCapicity =-1;
		this.graphSize = graphSize;
		this.dadArray = new int[graphSize];
		this.rankArray = new int[graphSize];
		for (int i = 0; i < graphSize; i++) {
			dadArray[i] = -1;
			rankArray[i]=0;
		}

		this.maxCapacityGraph = new ArrayList<GraphEdge>();

	}

	// Comparator for GraphNode class implementation
	public static Comparator<GraphEdge> graphEdgeComparator = new Comparator<GraphEdge>() {
		@Override
		public int compare(GraphEdge n1, GraphEdge n2) {
			return (int) (n2.getWeight() - n1.getWeight());
		}
	};

	public void createMaxBandwidthPath(Graph graph) {
		Queue<GraphEdge> edgePriorityQueue = new PriorityQueue<GraphEdge>(graph.getEdgelist().size(), graphEdgeComparator);

		// add all edge in the graph.
		for (GraphEdge graphEdge : graph.getEdgelist()) {
			edgePriorityQueue.add(graphEdge);
		}

		// create tree.
		while (edgePriorityQueue.size() != 0) {
			GraphEdge edge = edgePriorityQueue.poll();
			
			int soureceSet = findRank(edge.getSourceEdge());
			int destSet = findRank(edge.getDestEdge());
			
			if (soureceSet != destSet) {
				this.maxCapacityGraph.add(edge);
				if (rankArray[soureceSet] > rankArray[destSet]) {
					dadArray[destSet] = soureceSet;
				} else if (rankArray[soureceSet] < rankArray[destSet]) {
					dadArray[soureceSet] = destSet;
				} else {
					dadArray[destSet] = soureceSet;
					rankArray[soureceSet]++;
				}
			}

		}
	}

	private int findRank(int sourceEdge) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		while (dadArray[sourceEdge] != -1) {
			path.add(sourceEdge);
			sourceEdge = dadArray[sourceEdge];
		}

		while (!path.isEmpty()) {
			int node = path.remove(0);
			dadArray[node] = sourceEdge;
		}
		return sourceEdge;
	}

	public void printPathFromSourceToDest(int source, int destination) {
		this.graphDFS = new Graph(graphSize);
		this.graphDFS.createGraph();
		for (GraphEdge edge : this.maxCapacityGraph) {
			this.graphDFS.addEdge(edge.getSourceEdge(),edge.getDestEdge(), edge.getWeight());
		}

		DFS dfsGraph = new DFS(this.graphDFS.getSize());
		dfsGraph.createDFSVisit(this.graphDFS, this.graphDFS.getAdjList().get(source));
		this.maximumCapicity= dfsGraph.printPathFromSourceToDest(this.graphDFS,source, destination);
	}

	public int getMaximimCapacity(int source, int destination) {
		return this.maximumCapicity;
		
	}
}
