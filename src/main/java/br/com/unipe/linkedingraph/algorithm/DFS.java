package br.com.unipe.linkedingraph.algorithm;

import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.*;

public class DFS implements GraphAlgorithm{
    private final Graph graph;

    public DFS(Graph graph) {
        this.graph = graph;
    }

    @Override
    public String getName() {
        return "Depth-First Search";
    }

    @Override
    public PathResult execute(String sourceVName, String targetVName) { //iterativeDFS
        Vertex sourceVertex = graph.findVertex(sourceVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + sourceVName + " não encontrado."));
        Vertex targetVertex = targetVName == null ? null : graph.findVertex(targetVName).orElseThrow(
                () -> new IllegalArgumentException("Perfil " + targetVName + " não encontrado."));

        Stack<Vertex> stack = new Stack<>();
        Set<Vertex> visited = new LinkedHashSet<>();

        visited.add(sourceVertex);
        stack.push(sourceVertex);

        while(!stack.isEmpty()) {
            Vertex current = stack.peek();

            if(current.equals(targetVertex)) {
                List<String> path = stack.stream().map(Vertex::getProfileName).toList();
                return new PathResult(path, path.size() - 1);
            }

            List<Vertex> adjacency = current.getAdjacency();
            List<Vertex> orderedAdjacency = adjacency.stream()
                    .sorted(Comparator.comparing(Vertex::getProfileName))
                    .toList();

            // Pegue a primeira adjacência não visitada
            Optional<Vertex> nextVertex = orderedAdjacency.stream()
                    .filter(a -> !visited.contains(a))
                    .findFirst();

            if(nextVertex.isPresent()) {
                Vertex next = nextVertex.get();
                visited.add(next);
                stack.push(next); // avança para o primeiro vizinho não visitado
            } else {
                stack.pop(); // vértice esgotado: remove da stack
            }
        }

        if(targetVertex == null) {
            List<String> path = visited.stream().map(Vertex::getProfileName).toList();
            return new  PathResult(path, path.size() - 1);
        }
        return PathResult.empty();
    }
}