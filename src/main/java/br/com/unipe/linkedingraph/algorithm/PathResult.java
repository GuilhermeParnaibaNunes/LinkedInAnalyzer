package br.com.unipe.linkedingraph.algorithm;

import java.util.Collections;
import java.util.List;

public record PathResult (
        List<String> path, //Path can be a list of reachable vertices from source
        int totalCost //Cost in BFS and DFS is distance between vertices
){
    public boolean hasPath(){
        return path != null && !path.isEmpty();
    }

    public static PathResult empty(){
        return new PathResult(Collections.emptyList(), -1);
    }
}