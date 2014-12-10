package com.graph;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Graph {
	private int size;
	private List<GraphNode> adjList;
	private List<GraphEdge> edgelist;

	public Graph() {
		super();
		this.size = 0;
		this.adjList = new ArrayList<GraphNode>();
		this.edgelist = new ArrayList<GraphEdge>();
	}

	public void addNode(GraphNode node) {
		size++;
		adjList.add(node);
	}

	public Graph(int size) {
		super();
		this.size = size;
		this.adjList = new ArrayList<GraphNode>();
		this.edgelist = new ArrayList<GraphEdge>();
	}

	public void createGraph() {
		for (int i = 0; i < this.size; i++) {
			GraphNode node = new GraphNode(i);
			this.adjList.add(node);
		}
	}

	public boolean containsNode(int val) {
		for (GraphNode node : this.adjList) {
			if (node.getVal() == val) {
				return true;
			}
		}
		return false;
	}

	// return false if the edge exist or size=0 (no node)
	public void addEdge(int src, int dest, int weight) {
		this.adjList.get(src).getNeighbour().add(this.adjList.get(dest));
		this.adjList.get(src).getWeightList().add(weight);
		this.adjList.get(dest).getNeighbour().add(this.adjList.get(src));
		this.adjList.get(dest).getWeightList().add(weight);

		GraphEdge edge = new GraphEdge(src, dest, weight);
		this.edgelist.add(edge);
	}

	public void resetFringeValue() {
		for (GraphNode node : this.adjList) {
			node.setFringeValue(0);
			for (GraphNode neighbour : node.getNeighbour()) {
				neighbour.setFringeValue(0);
			}
		}
	}

	public List<GraphEdge> getEdgelist() {
		return edgelist;
	}

	public void setEdgelist(List<GraphEdge> edgelist) {
		this.edgelist = edgelist;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<GraphNode> getAdjList() {
		return adjList;
	}

	public void setAdjList(List<GraphNode> adjList) {
		this.adjList = adjList;
	}

	public void printGraph() {
		System.out.println("*************Graph ( size=  " + size + " )***************");
		for (int i = 0; i < size; i++) {
			adjList.get(i).printNode();
		}
	}

	public int getEdgeWeight(int source, int destination) {
		this.adjList.get(source).getNeighbour();
		for (int i = 0; i < this.adjList.get(source).getNeighbour().size(); i++) {
			GraphNode dest = this.adjList.get(source).getNeighbour().get(i);
			if (dest.getVal() == destination) {
				return this.adjList.get(source).getWeightList().get(i);
			}
		}
		return -1;
	}

}
