package br.com.unipe.linkedingraph.algorithm;

import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.*;

public class BFS implements GraphAlgorithm {
    private final Graph graph;

    public BFS(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String getName() {
        return "Breadth-First Search";
    }

    @Override
    public PathResult execute(String sourceVName, String targetVName) {
        Vertex sourceVertex = graph.findVertex(sourceVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + sourceVName + " não encontrado."));

        Vertex targetVertex = targetVName == null ? null : graph.findVertex(targetVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + targetVName + " não encontrado."));

        Queue<Vertex> queue = new ArrayDeque<>();
        Set<Vertex> visited = new LinkedHashSet<>();

        Map<Vertex, Vertex> parentMap = new HashMap<>();

        visited.add(sourceVertex);
        queue.add(sourceVertex);
        parentMap.put(sourceVertex, null);

        while(!queue.isEmpty()) {
            Vertex current = queue.poll();

            if(current.equals(targetVertex)) {
                List<String> path = buildPath(targetVertex, parentMap);
                return new PathResult(path, path.size() - 1);
            }

            List<Vertex> adjacency = current.getAdjacency();
            List<Vertex> orderedAdjacency = adjacency.stream()
                    .sorted(Comparator.comparing(Vertex::getProfileName))
                    .toList();

            for(Vertex next : orderedAdjacency) {
                if(!visited.contains(next)) {
                    visited.add(next);
                    parentMap.put(next, current);
                    queue.add(next);
                }
            }
        }

        if(targetVertex == null) {
            List<String> allVisited = visited.stream().map(Vertex::getProfileName).toList();
            return new PathResult(allVisited, allVisited.size() - 1);
        }

        return PathResult.empty();
    }

    private List<String> buildPath(Vertex target, Map<Vertex, Vertex> parentMap) {
        LinkedList<String> path = new LinkedList<>();
        Vertex step = target;

        while(step != null) {
            path.addFirst(step.getProfileName());
            step = parentMap.get(step);
        }

        return path;
    }
}