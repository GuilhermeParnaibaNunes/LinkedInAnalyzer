package br.com.unipe.linkedingraph.algorithm;

public interface GraphAlgorithm {
    String getName();
    PathResult execute(String sourceProfile, String targetProfile);
}
