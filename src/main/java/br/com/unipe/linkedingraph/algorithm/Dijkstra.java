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
        Vertex sourceVertex = graph.findVertex(sourceVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + sourceVName + " não encontrado."));
        Vertex targetVertex = graph.findVertex(targetVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + targetVName + " não encontrado."));

        // Distância (custo acumulado) até cada vértice
        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> parentMap = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();

        for (Vertex v : graph.getVertices()) {
            distances.put(v, Integer.MAX_VALUE);
        }
        distances.put(sourceVertex, 0);

        PriorityQueue<Vertex> queue = new PriorityQueue<>(
                Comparator.comparingInt(distances::get)
        );
        queue.add(sourceVertex);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();

            if (visited.contains(current)) {
                continue; // já processado com o menor custo possível
            }
            visited.add(current);

            if (current.equals(targetVertex)) {
                break; // destino alcançado com o menor custo: pode parar
            }

            for (Edge edge : graph.getIncidentEdges(current)) {
                Vertex neighbor = graph.getOppositeVertex(edge, current);

                if (visited.contains(neighbor)) {
                    continue;
                }

                int newDistance = distances.get(current) + edge.getWeight();

                // Relaxação de aresta: atualiza se encontrou caminho mais barato
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (distances.get(targetVertex) == Integer.MAX_VALUE) {
            return PathResult.empty();
        }

        List<String> path = buildPath(targetVertex, parentMap);
        return new PathResult(path, distances.get(targetVertex));
    }

    private List<String> buildPath(Vertex target, Map<Vertex, Vertex> parentMap) {
        LinkedList<String> path = new LinkedList<>();
        Vertex step = target;

        while (step != null) {
            path.addFirst(step.getProfileName());
            step = parentMap.get(step);
        }

        return path;
    }
}