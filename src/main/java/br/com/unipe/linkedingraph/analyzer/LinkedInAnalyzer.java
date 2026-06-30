package br.com.unipe.linkedingraph.analyzer;

import br.com.unipe.linkedingraph.algorithm.BFS;
import br.com.unipe.linkedingraph.algorithm.Dijkstra;
import br.com.unipe.linkedingraph.algorithm.PathResult;
import br.com.unipe.linkedingraph.graph.Graph;

public class LinkedInAnalyzer {

    private final Graph graph;

    public LinkedInAnalyzer(Graph graph) {
        this.graph = graph;
    }

    /**
     * Calcula o grau de separação entre dois perfis da rede.
     *
     * Usa BFS para encontrar o caminho com o menor número de
     * conexões intermediárias (menor número de saltos/arestas).
     *
     * @param originName  Nome do perfil de origem.
     * @param targetName  Nome do perfil de destino.
     * @return O número de saltos (inteiro >= 0) entre os dois perfis,
     *         ou -1 se não houver nenhum caminho entre eles.
     */
    public int degreeOfSeparation(String originName, String targetName) {
        BFS bfs = new BFS(graph);
        PathResult result = bfs.execute(originName, targetName);

        // PathResult.empty() retorna totalCost = -1 quando não há caminho
        return result.totalCost();
    }

    /**
     * Calcula a rota de maior afinidade entre dois perfis, usando Dijkstra.
     *
     * O peso da aresta representa o "custo de distância" entre dois perfis
     * (quanto menor o peso, maior a proximidade/afinidade). O algoritmo
     * encontra o caminho de menor custo acumulado, que corresponde
     * à rota de maior afinidade entre origem e destino.
     *
     * @param originName Nome do perfil de origem.
     * @param targetName Nome do perfil de destino.
     * @return PathResult contendo o caminho percorrido e o custo total,
     *         ou PathResult.empty() se não houver rota possível.
     */
    public PathResult bestAffinityRoute(String originName, String targetName) {
        Dijkstra dijkstra = new Dijkstra(graph);
        return dijkstra.execute(originName, targetName);
    }
}