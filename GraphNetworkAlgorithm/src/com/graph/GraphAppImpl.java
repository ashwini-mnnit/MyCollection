package com.graph;

public class GraphAppImpl {

	public static void main(String[] args) {
		//sparseGraphTest();
		//denseGraphTest();
		simpleEdgetest();

	}

	private static void simpleEdgetest() {
		Graph g = new Graph(5);
		g.createGraph();
		//Random randomGenerator = new Random();
		g.addEdge(0, 1, 4);
		g.addEdge(0, 2, 3);
		g.addEdge(0, 3, 7);
		//g.addEdge(2, 0, 1);
		g.addEdge(2, 4, 5);
		g.addEdge(3, 4, 5);
	}
	private static void denseGraphTest() {
		System.out.println("Dense graph");
		for (int i = 0; i < 1; i++) {
			Graph dg = RandomGraphGenerator.getDegree1000graph(5000);
			int count = 0;
			for (GraphNode node : dg.getAdjList()) {
				if (node.getNeighbour().size() != 1000)
					count++;
			}
			System.out.println("Count=" + count);
		}
	}

	private static void sparseGraphTest() {
		System.out.println("Sparse graph");
		for (int i = 0; i < 10; i++) {
			Graph g = RandomGraphGenerator.getDegree6graph(5000);
			int count = 0;
			for (GraphNode node : g.getAdjList()) {
				if (node.getNeighbour().size() != 6)
					count++;
			}
			g.printGraph();
			System.out.println("Count=" + count);

		}
	}

}
