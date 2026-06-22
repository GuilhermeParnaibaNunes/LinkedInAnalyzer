package br.com.unipe.linkedingraph;

import br.com.unipe.linkedingraph.graph.Graph;

public class LinkedInApp {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // ====== VÉRTICES ======
        graph.addVertices(
                "Ana",
                "Bruno",
                "Carlos",
                "Daniela",
                "Eduardo",
                "Fernanda",
                "Gabriel",
                "Hugo",
                "Igor",
                "Juliana"
        );

        // ====== CONEXÕES PRINCIPAIS ======
        graph.addEdge("Ana", "Bruno", 1);
        graph.addEdge("Ana", "Carlos", 2);
        graph.addEdge("Ana", "Daniela", 8);

        graph.addEdge("Bruno", "Eduardo", 1);
        graph.addEdge("Carlos", "Eduardo", 1);

        graph.addEdge("Daniela", "Fernanda", 5);
        graph.addEdge("Eduardo", "Fernanda", 1);

        // ====== GRUPO ISOLADO 1 ======
        graph.addEdge("Gabriel", "Hugo", 1);

        // ====== GRUPO ISOLADO 2 ======
        graph.addEdge("Igor", "Juliana", 1);

        // ====== TESTES ======

        System.out.println(graph);

        graph.displayAdjacencyMatrix();

        graph.displayIncidenceMatrix();

        System.out.println("\nCaminho Ana -> Fernanda: " +
                graph.getPathSize("Ana", "Bruno", "Eduardo", "Fernanda"));

        System.out.println("Caminho Ana -> Fernanda (alternativo): " +
                graph.getPathSize("Ana", "Daniela", "Fernanda"));
    }
}