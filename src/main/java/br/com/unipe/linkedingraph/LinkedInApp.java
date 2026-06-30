package br.com.unipe.linkedingraph;

import br.com.unipe.linkedingraph.algorithm.BFS;
import br.com.unipe.linkedingraph.algorithm.DFS;
import br.com.unipe.linkedingraph.algorithm.PathResult;
import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.analyzer.LinkedInAnalyzer;

public class LinkedInApp {
    public static void main(String[] args) {
        Graph linkedinGraph = new Graph();

        // ====== VÉRTICES ======
        linkedinGraph.addVertices(
                "Ana", "Bruno", "Carlos", "Daniela", "Eduardo",
                "Fernanda", "Gabriel", "Hugo", "Igor", "Juliana", "Kevin"
        );

        // ====== CONEXÕES PRINCIPAIS ======
        linkedinGraph.addEdge("Ana", "Bruno", 1);
        linkedinGraph.addEdge("Ana", "Carlos", 2);
        linkedinGraph.addEdge("Ana", "Daniela", 8);

        linkedinGraph.addEdge("Bruno", "Eduardo", 1);
        linkedinGraph.addEdge("Carlos", "Eduardo", 1);

        linkedinGraph.addEdge("Daniela", "Fernanda", 5);
        linkedinGraph.addEdge("Eduardo", "Fernanda", 1);

        linkedinGraph.addEdge("Fernanda", "Kevin", 4);

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
        // ====== GRAU DE SEPARAÇÃO ======
        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(linkedinGraph);
        System.out.println("\n=== TESTES: Grau de Separação (LinkedInAnalyzer) ===");

        // TESTE 1: Contatos diretos (grau 1)
        System.out.println("\n[Grau de Separação] Ana -> Bruno");
        int grau1 = analyzer.degreeOfSeparation("Ana", "Bruno");
        System.out.println("Resultado: " + (grau1 == -1 ? "Sem conexão (-1)" : grau1 + " salto(s)"));

        // TESTE 2: Amigo de amigo (grau 2)
        System.out.println("\n[Grau de Separação] Ana -> Eduardo");
        int grau2 = analyzer.degreeOfSeparation("Ana", "Eduardo");
        System.out.println("Resultado: " + (grau2 == -1 ? "Sem conexão (-1)" : grau2 + " salto(s)"));

        // TESTE 3: Caminho mais longo na rede principal (grau 3)
        System.out.println("\n[Grau de Separação] Ana -> Kevin");
        int grau3 = analyzer.degreeOfSeparation("Ana", "Kevin");
        System.out.println("Resultado: " + (grau3 == -1 ? "Sem conexão (-1)" : grau3 + " salto(s)"));

        // TESTE 4: Perfis em componentes totalmente isolados (deve retornar -1)
        System.out.println("\n[Grau de Separação] Ana -> Gabriel (componentes isolados)");
        int grau4 = analyzer.degreeOfSeparation("Ana", "Gabriel");
        System.out.println("Resultado: " + (grau4 == -1 ? "Sem conexão (-1)" : grau4 + " salto(s)"));

        // TESTE 5: Mesmo perfil (grau 0)
        System.out.println("\n[Grau de Separação] Ana -> Ana (mesmo perfil)");
        int grau5 = analyzer.degreeOfSeparation("Ana", "Ana");
        System.out.println("Resultado: " + (grau5 == -1 ? "Sem conexão (-1)" : grau5 + " salto(s)"));

        // ====== SUGESTÕES DE CONEXÃO ======
        br.com.unipe.linkedingraph.analyzer.ConnectionSuggestion suggester =
                new br.com.unipe.linkedingraph.analyzer.ConnectionSuggestion(linkedinGraph);

        System.out.println("\n=== TESTES: Sugestões de Conexão (ConnectionSuggestion) ===");

        // TESTE 1: Sugestões para Ana (Múltiplas sugestões com pesos diferentes)
        System.out.println("\n[Sugestões] Perfil: Ana");
        var sugestoesAna = suggester.suggestConnections("Ana");
        if (sugestoesAna.isEmpty()) {
            System.out.println("Nenhuma sugestão encontrada.");
        } else {
            sugestoesAna.forEach(s ->
                    System.out.println(" -> " + s.profileName() + " (" + s.mutualFriends() + " amigo(s) em comum)")
            );
        }

        // TESTE 2: Sugestões para Eduardo (Simetria da rede)
        System.out.println("\n[Sugestões] Perfil: Eduardo");
        var sugestoesEdu = suggester.suggestConnections("Eduardo");
        if (sugestoesEdu.isEmpty()) {
            System.out.println("Nenhuma sugestão encontrada.");
        } else {
            sugestoesEdu.forEach(s ->
                    System.out.println(" -> " + s.profileName() + " (" + s.mutualFriends() + " amigo(s) em comum)")
            );
        }

        // TESTE 3: Perfil isolado sem rede de segundo grau
        System.out.println("\n[Sugestões] Perfil: Gabriel (Grupo Isolado)");
        var sugestoesGab = suggester.suggestConnections("Gabriel");
        if (sugestoesGab.isEmpty()) {
            System.out.println("Nenhuma sugestão encontrada. O algoritmo filtrou corretamente!");
        } else {
            sugestoesGab.forEach(s ->
                    System.out.println(" -> " + s.profileName() + " (" + s.mutualFriends() + " amigo(s) em comum)")
            );
        }
        
        // ====== ROTA DE MAIOR AFINIDADE (Dijkstra) ======
        System.out.println("\n=== TESTES: Rota de Maior Afinidade (LinkedInAnalyzer) ===");

        // TESTE 1: Rota direta vs rota alternativa mais cara
        System.out.println("\n[Rota de Maior Afinidade] Ana -> Fernanda");
        PathResult bestRouteAF = analyzer.bestAffinityRoute("Ana", "Fernanda");
        if (bestRouteAF.hasPath()) {
            System.out.println("Rota: " + bestRouteAF.path());
            System.out.println("Custo total: " + bestRouteAF.totalCost());
        }

        // TESTE 2: Conexão direta (grau 1)
        System.out.println("\n[Rota de Maior Afinidade] Ana -> Bruno");
        PathResult bestRouteAB = analyzer.bestAffinityRoute("Ana", "Bruno");
        if (bestRouteAB.hasPath()) {
            System.out.println("Rota: " + bestRouteAB.path());
            System.out.println("Custo total: " + bestRouteAB.totalCost());
        }

        // TESTE 3: Perfis em componentes isolados (sem rota possível)
        System.out.println("\n[Rota de Maior Afinidade] Ana -> Gabriel (componentes isolados)");
        PathResult bestRouteAG = analyzer.bestAffinityRoute("Ana", "Gabriel");
        if (!bestRouteAG.hasPath()) {
            System.out.println("Resultado: Sem rota possível (Perfis em redes isoladas).");
        }
    }
}