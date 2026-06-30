package br.com.unipe.linkedingraph;

import br.com.unipe.linkedingraph.algorithm.BFS;
import br.com.unipe.linkedingraph.algorithm.DFS;
import br.com.unipe.linkedingraph.algorithm.Dijkstra;
import br.com.unipe.linkedingraph.algorithm.PathResult;
import br.com.unipe.linkedingraph.graph.Graph;

public class LinkedInApp {
    public static void main(String[] args) {

        Graph linkedinGraph = new Graph();

        // ====== VÉRTICES ======
        linkedinGraph.addVertices(
                "Ana", "Bruno", "Carlos", "Daniela", "Eduardo",
                "Fernanda", "Gabriel", "Hugo", "Igor", "Juliana"
        );

        // ====== CONEXÕES PRINCIPAIS ======
        linkedinGraph.addEdge("Ana", "Bruno", 1);
        linkedinGraph.addEdge("Ana", "Carlos", 2);
        linkedinGraph.addEdge("Ana", "Daniela", 8);

        linkedinGraph.addEdge("Bruno", "Eduardo", 1);
        linkedinGraph.addEdge("Carlos", "Eduardo", 1);

        linkedinGraph.addEdge("Daniela", "Fernanda", 5);
        linkedinGraph.addEdge("Eduardo", "Fernanda", 1);

        // ====== GRUPO ISOLADO 1 ======
        linkedinGraph.addEdge("Gabriel", "Hugo", 1);

        // ====== GRUPO ISOLADO 2 ======
        linkedinGraph.addEdge("Igor", "Juliana", 1);

        // ====== ESTADO DO GRAFO ======
        System.out.println("=== ESTRUTURA DO GRAFO ===");
        System.out.println(linkedinGraph);
        linkedinGraph.displayAdjacencyMatrix();

        // Teste manual antigo (preservado)
        System.out.println("\n=== TESTES MANUAIS ANTIGOS ===");
        System.out.println("Custo do caminho Ana -> Bruno -> Eduardo -> Fernanda: " +
                linkedinGraph.getPathSize("Ana", "Bruno", "Eduardo", "Fernanda"));

        // ====== TESTES COM DFS ======
        DFS dfs = new DFS(linkedinGraph);
        System.out.println("\n=== TESTES DE ALGORITMO: " + dfs.getName() + " ===");

        // TESTE 1: Caminho possível
        System.out.println("\n[Teste 1] Buscando conexão: Ana -> Fernanda");
        PathResult pathAF = dfs.execute("Ana", "Fernanda");
        if (pathAF.hasPath()) {
            System.out.println("Caminho percorrido pelo DFS: " + pathAF.path());
            System.out.println("Grau de separação (saltos): " + pathAF.totalCost());
        }

        // TESTE 2: Caminho impossível (ilhas diferentes)
        System.out.println("\n[Teste 2] Buscando conexão em ilha isolada: Ana -> Gabriel");
        PathResult pathAG = dfs.execute("Ana", "Gabriel");
        if (!pathAG.hasPath()) {
            System.out.println("Resultado: Perfil inalcançável (Sem conexão na rede). PathResult.empty() funcionou!");
        }

        // TESTE 3: Travessia completa do componente conectado
        System.out.println("\n[Teste 3] Mapeando toda a rede alcançável a partir de: Gabriel");
        PathResult fullGabriel = dfs.execute("Gabriel", null);
        System.out.println("Perfis alcançáveis por Gabriel: " + fullGabriel.path());

        // ====== COMPARANDO DFS COM BFS ======
        BFS bfs = new BFS(linkedinGraph);
        System.out.println("\n=== TESTES DE ALGORITMO: " + bfs.getName() + " ===");

        // Teste de Caminho Ana -> Fernanda
        PathResult bfsPath = bfs.execute("Ana", "Fernanda");
        if (bfsPath.hasPath()) {
            System.out.println("Caminho percorrido pelo BFS: " + bfsPath.path());
            System.out.println("Grau de separação (saltos): " + bfsPath.totalCost());
        }

        // ====== TESTES COM DIJKSTRA ======
        Dijkstra dijkstra = new Dijkstra(linkedinGraph);
        System.out.println("\n=== TESTES DE ALGORITMO: " + dijkstra.getName() + " ===");

        // Teste 1: O Caminho de Menor Custo vs Menos Saltos
        System.out.println("\n[Teste 1] Buscando rota otimizada por peso: Ana -> Fernanda");
        PathResult dijkstraPath = dijkstra.execute("Ana", "Fernanda");
        if (dijkstraPath.hasPath()) {
            System.out.println("Caminho escolhido pelo Dijkstra: " + dijkstraPath.path());
            System.out.println("Custo total (soma dos pesos): " + dijkstraPath.totalCost());
        }

        // Teste 2: Caminho impossível (Ilha isolada)
        System.out.println("\n[Teste 2] Buscando rota para ilha isolada: Ana -> Gabriel");
        PathResult dijkstraIsolado = dijkstra.execute("Ana", "Gabriel");
        if (!dijkstraIsolado.hasPath()) {
            System.out.println("Resultado: Perfil inalcançável. PathResult.empty() funcionou perfeitamente.");
        }

        // Teste 3: Proteção arquitetural contra travessia cega
        System.out.println("\n[Teste 3] Tentativa de travessia cega (Alvo nulo)");
        try {
            dijkstra.execute("Ana", null);
        } catch (IllegalArgumentException e) {
            System.out.println("Exceção capturada com sucesso: " + e.getMessage());
        }
    }
}