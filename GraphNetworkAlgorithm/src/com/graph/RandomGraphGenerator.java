package com.graph;

import java.util.Random;

public class RandomGraphGenerator {

	public static int getRandomWeight() {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(1000);
	}

	public static int getRandomDestination(Graph g, int src, int degree) {
		int randomattempt = 400;
		while (true) {
			if (randomattempt-- < 0)
				break;
			Random rendGen = new Random();
			int dest = rendGen.nextInt(g.getSize());
			if (dest != src && (!g.getAdjList().get(dest).isAdjListcontainsNode(src)) && (g.getAdjList().get(dest).getNeighbour().size() < degree))
				return dest;
		}
		// get first free node.

		for (int i = 0; i < g.getSize(); i++) {
			if (i != src && (!g.getAdjList().get(src).isAdjListcontainsNode(i)) && (g.getAdjList().get(i).getNeighbour().size() < degree))
				return i;
		}
		return -1;
	}

	public static Graph getDegree6graph(int size) {
		Graph graph = new Graph(size);
		graph.createGraph();
		for (int i = 0; i < graph.getSize(); i++) {
			GraphNode node = graph.getAdjList().get(i);
			int src = node.getVal();
			// System.out.println("Src=" + src);
			int childs = node.getNeighbour().size();
			int remainingChild = 6 - childs;
			while (true) {
				if (remainingChild <= 0)
					break;

				int dest = getRandomDestination(graph, src, 6);
				if (dest < 0) {
					break;
				}
				graph.addEdge(src, dest, getRandomWeight());
				remainingChild--;
			}

		}
		return graph;
	}

	public static Graph getDegree1000graph(int size) {
		Graph graph = new Graph(size);
		graph.createGraph();
		for (GraphNode node  : graph.getAdjList()) {
			int src = node.getVal();
			int childs = node.getNeighbour().size();
			int remainingChild = 1000 - childs;
			while (remainingChild>0) {
				int dest = getRandomDestination(graph, src, 1000);
				if (dest < 0) {
					break;
				}				
				graph.addEdge(src, dest, getRandomWeight());
				remainingChild--;

			}

		}
		return graph;
	}

	public RandomGraphGenerator() {
		super();
	}
}
