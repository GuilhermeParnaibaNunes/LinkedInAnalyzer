package br.com.unipe.linkedingraph.graph;

import java.util.Objects;

public class Edge {
    private final Vertex sourceVertex;
    private final Vertex targetVertex;
    private final int weight;

    public Edge(Vertex sourceVertex, Vertex targetVertex, int weight) {
        if(weight <= 0) {
            throw new IllegalArgumentException(
                    "O peso deve ser maior do que zero."
            );
        }

        this.sourceVertex = Objects.requireNonNull(sourceVertex, "Vértice não pode ser nulo");
        this.targetVertex = Objects.requireNonNull(targetVertex, "Vértice não pode ser nulo");
        this.weight = weight;
    }

    /**GETTERS:*/
    public Vertex getSourceVertex() { return sourceVertex; }
    public Vertex getTargetVertex() { return targetVertex; }
    public int getWeight() { return weight; }

    @Override
    public String toString() {
        return "%s -> %s (%d)"
                .formatted(
                        sourceVertex.getProfileName(),
                        targetVertex.getProfileName(),
                        weight
                );
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null || getClass() != obj.getClass()) return false;

        Edge edge = (Edge) obj;

        return sourceVertex.equals(edge.getSourceVertex()) &&
                targetVertex.equals(edge.getTargetVertex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceVertex, targetVertex);
    }
}
