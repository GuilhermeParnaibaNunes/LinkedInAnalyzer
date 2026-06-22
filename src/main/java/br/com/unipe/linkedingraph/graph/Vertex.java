package br.com.unipe.linkedingraph.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vertex {
    private final String profileName;
    private final List<Vertex> adjacency;

    public Vertex (String profileName) {
        if(profileName == null || profileName.isBlank()) {
            throw new IllegalArgumentException("Nome do perfil inválido.");
        }

        this.profileName = profileName.trim();
        this.adjacency = new ArrayList<>();
    }

    //package-private
    void addAdjacency(Vertex vertex) {
        adjacency.add(vertex);
    }

    @Override
    public String toString() {
        return profileName;
    }

    /**GETTERS & SETTERS:*/

    //GETTERS
    public String getProfileName() {
        return profileName;
    }
    public int getDegree() {
        return adjacency.size();
    }
    public List<Vertex> getAdjacency() {
        return Collections.unmodifiableList(adjacency);
    }
    public String getDegreeInfo() {
        return "\n%s: grau %d".formatted(profileName, getDegree());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Vertex vertex = (Vertex) obj;

        return profileName.equalsIgnoreCase(vertex.profileName);
    }

    @Override
    public int hashCode() {
        return profileName.toLowerCase().hashCode();
    }
}
