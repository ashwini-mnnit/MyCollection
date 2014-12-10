package com.main;

import java.util.Random;

import com.graph.Graph;
import com.graph.RandomGraphGenerator;
import com.routing.algorithms.DijkstraModified;
import com.routing.algorithms.KrushkalModified;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("#################### Testing  ##################");
		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("\n\n\n");
		System.out.println("Executing for 5 pairs of Graph");
		int graphSize = 5000;
		for (int i = 0; i < 5; i++) {
			System.out.println("*****************************************************");
			System.out.println("             Graph pair Iteration: " + (i + 1));
			System.out.println("*****************************************************");
			System.out.println("Generating Sparse Graph ......");
			Graph sparseGraph = RandomGraphGenerator.getDegree6graph(graphSize);
			System.out.println("Generating Dense Graph ......");
		    Graph denseGraph = RandomGraphGenerator.getDegree1000graph(graphSize);
			
			int []sourceArray= new int [5];
			int []destArray= new int [5];
			Random random = new Random();
			for (int j = 0; j < 5; j++) {
				sourceArray[j]=random.nextInt(5000);
				destArray[j]=random.nextInt(5000);
			}
			
			for (int j = 0; j < 5; j++) {
				System.out.println("Sparse Graph (Degree 6 Graph) !!!");
				System.out.println(" \n\n");
				processGraph(sparseGraph, sourceArray[j], destArray[j]);
				System.out.println("\n\n");
				System.out.println("############################################################");
				System.out.println("Dense Graph (Degree 6 Graph) !!!");
				System.out.println(" \n\n");

				processGraph(denseGraph, sourceArray[j], destArray[j]);
			}

		}
	}

	/**
	 * @param graphSize
	 * @param graph
	 * @param source
	 * @param destination
	 */
	private static void processGraph(Graph graph, int source, int destination) {
		System.out.println("First Source/Destination pair. Source=" + source + " Destination=" + destination);
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("                     MAXIMUM BANDWIDTH PATH( Without Heap):");
		graph.resetFringeValue();
		dijkstraWithNoHeap(graph, source, destination);
		
		System.out.println("\n\n");
		System.out.println("                     MAXIMUM BANDWIDTH PATH( With Heap):");
		dijkstraWithHeap(graph, source, destination);
        graph.resetFringeValue();
		System.out.println("\n\n");
		System.out.println("                     MAXIMUM BANDWIDTH PATH( Krushkal):");
		kruskalPath(graph, source, destination);
	}

	/**
	 * @param graph
	 * @param source
	 * @param destination
	 * @param endTime
	 */
	private static void kruskalPath(Graph graph, int source, int destination) {
		graph.resetFringeValue();
		
		KrushkalModified km = new KrushkalModified(graph.getSize());
		long startTime = System.currentTimeMillis();
		km.createMaxBandwidthPath(graph);
		System.out.print("Path: ");
		km.printPathFromSourceToDest(source, destination);
		System.out.println();
		System.out.print("Maximum Capacity: ");
		System.out.println(km.getMaximimCapacity(source, destination));
		System.out.println();
		long endTime = System.currentTimeMillis();
		System.out.println("Elapsed Time (in Milisec) =" + (endTime - startTime));
		
	}

	
	/**
	 * @param graph
	 * @param source
	 * @param destination
	 */
	private static void dijkstraWithHeap(Graph graph, int source, int destination) {
		DijkstraModified djModifiedwithHeap = new DijkstraModified(graph.getSize());
		long startTime = System.currentTimeMillis();
		djModifiedwithHeap.createMaxBandwidthPathWithHeap2(graph, graph.getAdjList().get(source));
		long endTime = System.currentTimeMillis();
		System.out.print("Path: ");djModifiedwithHeap.printPathFromSourceToDest(source, destination);
		System.out.println();
		System.out.print("Maximum Capacity: ");
		System.out.println(djModifiedwithHeap.getMaximimCapacity(graph,source, destination));
		System.out.println();
		System.out.println("Elapsed Time (in Milisec) =" + (endTime - startTime));
		
	}

	/**
	 * @param graph
	 * @param source
	 * @param destination
	 */
	private static void dijkstraWithNoHeap(Graph graph, int source, int destination) {
		DijkstraModified djModifiedwithoutHeap = new DijkstraModified(graph.getSize());
		long startTime = System.currentTimeMillis();
		djModifiedwithoutHeap.createMaxBandwidthPathWithoutHeap(graph, graph.getAdjList().get(source));
		long endTime = System.currentTimeMillis();
		System.out.print("Path: ");djModifiedwithoutHeap.printPathFromSourceToDest(source, destination);
		System.out.println();
		System.out.print("Maximum Capacity: ");
		System.out.println(djModifiedwithoutHeap.getMaximimCapacity(graph,source, destination));
		System.out.println();
		System.out.println("Elapsed Time (in Milisec) =" + (endTime - startTime));
	}
}