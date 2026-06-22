package br.com.unipe.linkedingraph.graph;

import javax.swing.*;
import java.util.*;

public class Graph {
    private final List<Edge> edges;
    private final List<Vertex> vertices;
    private int order;
    private int size;

    public Graph() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
    }

    public void addVertices(String... names) {
        for(String profileName : names) {
            vertices.add(new Vertex(profileName));
            order++;
        }
    }

    public void addEdge(String v1, String v2, int weight) {
        edges.add(createEdge(v1, v2, weight));
    }

    private Edge createEdge(String vertex1Name, String vertex2Name, Integer weight) {
        Vertex v1 = findVertex(vertex1Name).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + vertex1Name + " não encontrado."));
        Vertex v2 = findVertex(vertex2Name).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + vertex2Name + " não encontrado."));
        updateAdjacencies(v1, v2);
        size++;
        return new Edge(v1, v2, weight);
    }

    private void updateAdjacencies(Vertex v1, Vertex v2) {
        v1.addAdjacency(v2);
        v2.addAdjacency(v1);
    }

    public Optional<Vertex> findVertex(String name) {
        for(Vertex vertex : vertices) {
            if(vertex.getProfileName().equalsIgnoreCase(name)) {
                return Optional.of(vertex);
            }
        }
        return Optional.empty();
    }

    public String formatVerticesDegreeInfo() {
        StringBuilder degrees = new StringBuilder();
        for(Vertex vertex : vertices) {
            degrees.append(vertex.getDegreeInfo());
        }
        return degrees.toString();
    }

    public String formatAdjacencyInfo() {
        StringBuilder adjacencyInfo = new StringBuilder();
        for(Vertex vertex : vertices) {
            adjacencyInfo.append("\n").append(vertex.getProfileName()).append(": ").append(vertex.getAdjacency());
        }
        return adjacencyInfo.toString();
    }

    public void displayAdjacencyMatrix() {
        List<Vertex> orderedVertices = vertices.stream().sorted(Comparator.comparing(Vertex::getProfileName)).toList();

        StringBuilder matrix = new StringBuilder("\nMatriz de Adjacência\n");
        matrix.append("\t");
        orderedVertices.forEach(v -> matrix.append(v.getProfileName()).append("\t"));
        matrix.append("\n");

        for(Vertex vertex : orderedVertices) { // read-only
            matrix.append(vertex.getProfileName()).append("\t");
            List<Vertex> adjacency = vertex.getAdjacency();
            for(Vertex anotherVertex : orderedVertices) {
                matrix.append(adjacency.contains(anotherVertex) ? "1" : "0").append("\t");
            }
            matrix.append("\n");
        }

        System.out.println(matrix);
    }

    public void displayIncidenceMatrix() {
        List<Vertex> orderedVertices = vertices.stream().sorted(Comparator.comparing(Vertex::getProfileName)).toList();
        StringBuilder matrix = new StringBuilder("\nMatriz de Incidência\n\t");
        edges.forEach(
                e -> matrix.append(
                    "%s-%s".formatted(e.getSourceVertex(), e.getTargetVertex())
                ).append("\t")
        );
        matrix.append("\n");
        for(Vertex vertex : orderedVertices) {
            matrix.append(vertex.getProfileName()).append("\t");
            for(Edge edge : edges) {
                Vertex source = edge.getSourceVertex();
                Vertex target = edge.getTargetVertex();
                String value;
                if(source.equals(vertex) && target.equals(vertex)) {
                    value = " 2";
                } else if(source.equals(vertex)) {
                    value = "1";
                } else if(target.equals(vertex)) {
                    value = " 1";
                } else {
                    value = " 0";
                }
                matrix.append(value).append("\t");
            }
            matrix.append("\n");
        }
        System.out.println(matrix);
    }

    public int getPathSize(String... path) {
        int length = 0;
        List<Edge> visitedEdges = new ArrayList<>();

        for(int i = 0; i < path.length - 1; i++) {
            int currentIndex = i;
            Vertex source = findVertex(path[currentIndex]).orElseThrow(
                    () -> new IllegalArgumentException("Perfil " + path[currentIndex] + " não encontrado."));
            Vertex target = findVertex(path[currentIndex + 1]).orElseThrow(
                    () -> new IllegalArgumentException("Perfil " + path[currentIndex + 1] + " não encontrado."));
            Optional<Edge> edge = edges.stream()
                    .filter(e -> e.getSourceVertex().equals(source) && e.getTargetVertex().equals(target))
                    .findFirst();
            if(edge.isPresent()) {
                if(visitedEdges.contains(edge.get())) {
                    throw new IllegalArgumentException("Edge repetida!");
                }
                visitedEdges.add(edge.get());
                length += edge.get().getWeight();
            }
        }
        return length;
    }

    private List<Edge> getEdgesToNeighbor(Vertex current, Vertex neighbor) {
        return edges.stream()
                .filter(e -> (
                        e.getSourceVertex().equals(current) && e.getTargetVertex().equals(neighbor)) ||
                        e.getTargetVertex().equals(current) && e.getSourceVertex().equals(neighbor))
                .toList();
    }

    public List<Vertex> getNeighbors(Vertex vertex) {
        return vertex.getAdjacency();
    }

    public Vertex getOppositeVertex(Edge edge, Vertex vertex) {
        return edge.getSourceVertex().equals(vertex) ? edge.getTargetVertex() : edge.getSourceVertex();
    }

    public List<Edge> getOutgoingEdges(Vertex vertex){
        return edges.stream()
                .filter(edge -> edge.getSourceVertex().equals(vertex))
                .toList();
    }

    public List<Edge> getIncidentEdges(Vertex vertex) {
        return edges.stream()
                .filter(edge -> edge.getSourceVertex().equals(vertex)
                        || edge.getTargetVertex().equals(vertex))
                .toList();
    }

    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    @Override
    public String toString() {
        return """
                {
                ordem = %d,
                tamanho = %d,
                vertices = %s,
                arestas = %s,
                graus = %s,
                adjacências = %s
                }
                """.formatted(order, size, vertices, edges, formatVerticesDegreeInfo(),
                formatAdjacencyInfo());
    }
}
