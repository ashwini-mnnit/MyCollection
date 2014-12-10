package com.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {
	private int val;
	private List<GraphNode> neighbour;
	private List<Integer> weightList;
	private int fringeValue;
	
	public List<GraphNode> getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(List<GraphNode> neighbour) {
		this.neighbour = neighbour;
	}

	public GraphNode(int val) {
		super();
		this.val = val;
		this.neighbour = new ArrayList<GraphNode>();
		this.weightList = new ArrayList<Integer>();
	}

	public int getFringeValue() {
		return fringeValue;
	}

	public void setFringeValue(int fringeValue) {
		this.fringeValue = fringeValue;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;

	}

	public boolean isAdjListcontainsNode(int val) {
            for (GraphNode neighbour1 : neighbour) {
                if (neighbour1.getVal() == val) {
				return true;
			}
            }
		
		return false;
	}

	public void printNode(){
		System.out.print("GraphNode["+val+"]"+ "neighbour =[");
		for (GraphNode neighbour : this.neighbour) {
			System.out.print(neighbour.getVal()+",");
		}
		System.out.print("]  weightList=[");
		for (Integer wt : this.weightList) {
			System.out.print(wt+",");
		}
		System.out.println("]");
	}

	public List<Integer> getWeightList() {
		return weightList;
	}

	public void setWeightList(List<Integer> weightList) {
		this.weightList = weightList;
	}

}
