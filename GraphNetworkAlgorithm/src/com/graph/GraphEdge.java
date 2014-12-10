package com.graph;

public class GraphEdge {
	private int sourceEdge;
	private int destEdge;
	private int weight;
	private boolean isDirected;
	public GraphEdge(int sourceEdge, int destEdge, int weight) {
		super();
		this.sourceEdge = sourceEdge;
		this.destEdge = destEdge;
		this.weight = weight;
		this.isDirected=false;
	}
	public int getSourceEdge() {
		return sourceEdge;
	}
	public void setSourceEdge(int sourceEdge) {
		this.sourceEdge = sourceEdge;
	}
	public int getDestEdge() {
		return destEdge;
	}
	public void setDestEdge(int destEdge) {
		this.destEdge = destEdge;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public boolean isDirected() {
		return isDirected;
	}
	public void setDirected(boolean isDirected) {
		this.isDirected = isDirected;
	}

}
