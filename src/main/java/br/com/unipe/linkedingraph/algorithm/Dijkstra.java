package br.com.unipe.linkedingraph.algorithm;

import br.com.unipe.linkedingraph.graph.Edge;
import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.*;

public class Dijkstra implements GraphAlgorithm {
    private final Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String getName() {
        return "Dijkstra";
    }

    @Override
    public PathResult execute(String sourceVName, String targetVName) {
        if (targetVName == null) {
            throw new IllegalArgumentException("O algoritmo de Dijkstra exige um perfil de destino válido.");
        }

        Vertex sourceVertex = graph.findVertex(sourceVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + sourceVName + " não encontrado."));

        Vertex targetVertex = targetVName == null ? null : graph.findVertex(targetVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + targetVName + " não encontrado."));

        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> previousVertices = new HashMap<>();
        Set<Vertex> visitedVertices = new HashSet<>();

        PriorityQueue<DijkstraNode> queue =
                new PriorityQueue<>(
                        Comparator.comparingInt(DijkstraNode::distance)
                );

        for (Vertex vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }

        distances.put(sourceVertex, 0);
        queue.add(new DijkstraNode(sourceVertex, 0));

        while (!queue.isEmpty()) {
            DijkstraNode currentData = queue.poll();
            Vertex current = currentData.vertex();

            if (visitedVertices.contains(current)) {
                continue;
            }

            visitedVertices.add(current);

            if (current.equals(targetVertex)) {
                break;
            }

            for (Edge edge : graph.getIncidentEdges(current)) {
                Vertex neighbor =
                        graph.getOppositeVertex(
                                edge,
                                current
                        );

                if (visitedVertices.contains(neighbor)) {
                    continue;
                }

                int newDistance =
                        distances.get(current)
                                + edge.getWeight();

                if (newDistance < distances.get(neighbor)) {
                    distances.put(
                            neighbor,
                            newDistance
                    );

                    previousVertices.put(
                            neighbor,
                            current
                    );

                    queue.add(
                            new DijkstraNode(
                                    neighbor,
                                    newDistance
                            )
                    );
                }
            }
        }

        if (distances.get(targetVertex) == Integer.MAX_VALUE) {
            return PathResult.empty();
        }

        LinkedList<String> path = new LinkedList<>();
        Vertex current = targetVertex;

        while (current != null) {
            path.addFirst(current.getProfileName());
            current = previousVertices.get(current);
        }

        return new PathResult(path, distances.get(targetVertex));
    }

    private record DijkstraNode(Vertex vertex, int distance) {}
}