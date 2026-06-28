package br.com.unipe.linkedingraph.algorithm;

import java.util.Collections;
import java.util.List;

public record PathResult (
        List<String> path,
        int totalCost
){
    public boolean hasPath(){
        return path != null && !path.isEmpty();
    }

    public static PathResult empty(){
        return new PathResult(Collections.emptyList(), -1);
    }
}