package com.routing.algorithms;

import java.util.Stack;

import com.graph.Graph;
import com.graph.GraphNode;

public class DFS {

	int graphsize;
	private int[] dadArray;
	private int[] color;

	private static final int ColorWHITE = 1;
	private static final int ColorGRAY = 2;
	private static final int ColorBLACK = 3;

	public DFS(int graphsize) {
		super();
		this.graphsize = graphsize;
		this.dadArray = new int[graphsize];
		this.color = new int[graphsize];

		for (int i = 0; i < graphsize; i++) {
			dadArray[i] = -1;
			color[i] = ColorWHITE;
		}
	}

	public void createDFS(Graph graph, GraphNode graphNode) {
		for (GraphNode node : graph.getAdjList()) {
			color[node.getVal()] = ColorWHITE;
			dadArray[node.getVal()] = -1;
		}
		for (GraphNode node : graph.getAdjList()) {
			if (color[node.getVal()] == ColorWHITE)
				createDFSVisit(graph, node);
		}
	}

	public void createDFSVisit(Graph graph, GraphNode sourceNode) {
		color[sourceNode.getVal()] = ColorGRAY;
		for (GraphNode adjNode : sourceNode.getNeighbour()) {
			if (color[adjNode.getVal()] == ColorWHITE) {
				dadArray[adjNode.getVal()] = sourceNode.getVal();
				createDFSVisit(graph, adjNode);
			}
		}
		color[sourceNode.getVal()] = ColorBLACK;
	}

	public int printPathFromSourceToDest(Graph graph, int source, int destination) {
		int parent = destination;
		int rvWeight = Integer.MAX_VALUE;
		Stack<Integer> path = new Stack<Integer>();
		while (parent != source) {
			path.add(parent);
			int nextParent = dadArray[parent];
			int weight = graph.getEdgeWeight(nextParent, parent);
			if (weight < rvWeight) {
				rvWeight = weight;
			}
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
		
		return rvWeight;
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
