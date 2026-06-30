package br.com.unipe.linkedingraph.analyzer;

import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;
import br.com.unipe.linkedingraph.algorithm.BFS;
import br.com.unipe.linkedingraph.algorithm.DFS;
import br.com.unipe.linkedingraph.algorithm.Dijkstra;
import br.com.unipe.linkedingraph.algorithm.PathResult;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

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

    /**
     * Mapeia todos os grupos isolados (componentes conexos) na rede.
     * Utiliza a Busca em Profundidade (DFS) sem alvo específico para
     * explorar exaustivamente cada sub-rede.
     *
     * @return Uma lista onde cada elemento é uma lista de nomes de perfis
     * que formam um grupo isolado.
     */
    public List<List<String>> isolatedGroups() {
        List<List<String>> connectedComponents = new ArrayList<>();
        Set<String> visitedProfiles = new HashSet<>();

        DFS dfs = new DFS(graph);

        for(Vertex vertex : graph.getVertices()) {
            String profileName = vertex.getProfileName();

            if (!visitedProfiles.contains(profileName)) {
                PathResult result = dfs.execute(profileName, null);
                List<String> group = result.path();

                connectedComponents.add(group);

                visitedProfiles.addAll(group);
            }
        }

        return connectedComponents;
    }
}