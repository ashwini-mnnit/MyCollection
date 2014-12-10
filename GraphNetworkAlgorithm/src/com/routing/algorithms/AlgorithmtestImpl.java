package com.routing.algorithms;

import com.graph.Graph;
import com.graph.RandomGraphGenerator;

public class AlgorithmtestImpl {

	public static void main(String[] args) {
		AlgorithmtestImpl.simpleGraphtest();
		AlgorithmtestImpl.simpleGraphtest2();
		//AlgorithmtestImpl.degree6GraphTest();
	}

	public static void degree6GraphTest() {
		int graphsize = 1000;
		Graph g = RandomGraphGenerator.getDegree6graph(graphsize);
		// g.printGraph();
		DijkstraModified dj = new DijkstraModified(graphsize);
		dj.createMaxBandwidthPathWithoutHeap(g, g.getAdjList().get(2));
		System.out.println(dj.toString());
	}
	public static void simpleGraphtest2() {
		Graph g = new Graph(5);
		g.createGraph();
		//Random randomGenerator = new Random();
		g.addEdge(0, 1, 4);
		g.addEdge(0, 2, 3);
		g.addEdge(0, 3, 7);
		g.addEdge(2, 0, 1);
		g.addEdge(2, 4, 5);
		g.addEdge(3, 4, 5);

		DijkstraModified dj = new DijkstraModified(5);
		dj.createMaxBandwidthPathWithHeap(g, g.getAdjList().get(0));
		dj.printPathFromSourceToDest(0, 4);
		System.out.print("Maximum Capacity: ");
		System.out.println(dj.getMaximimCapacity(g,0, 4));
	}
	public static void simpleGraphtest() {
		Graph g = new Graph(5);
		g.createGraph();
		//Random randomGenerator = new Random();
		g.addEdge(0, 1, 4);
		g.addEdge(0, 2, 3);
		g.addEdge(0, 3, 7);
		g.addEdge(2, 0, 1);
		g.addEdge(2, 4, 5);
		g.addEdge(3, 4, 5);

		DijkstraModified dj = new DijkstraModified(5);
		dj.createMaxBandwidthPathWithoutHeap(g, g.getAdjList().get(0));
		dj.printPathFromSourceToDest(0, 4);
		System.out.print("Maximum Capacity: ");
		System.out.println(dj.getMaximimCapacity(g,0, 4));
	}
}
