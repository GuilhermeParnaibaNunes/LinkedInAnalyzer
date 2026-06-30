package br.com.unipe.linkedingraph;

import br.com.unipe.linkedingraph.algorithm.PathResult;
import br.com.unipe.linkedingraph.analyzer.ConnectionSuggestion;
import br.com.unipe.linkedingraph.analyzer.LinkedInAnalyzer;
import br.com.unipe.linkedingraph.graph.Graph;

import java.util.List;

public class LinkedInApp {
    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("*** INICIANDO O MOTOR DE ANÁLISE: LINKEDIN ANALYZER ***");
        System.out.println("=====================================================\n");

        // ---------------------------------------------------------
        // FASE 1: CONSTRUÇÃO DA REDE
        // ---------------------------------------------------------
        Graph linkedinGraph = new Graph();

        linkedinGraph.addVertices(
                "Ana", "Bruno", "Carlos", "Daniela", "Eduardo",
                "Fernanda", "Kevin", // Rede Principal estendida
                "Gabriel", "Hugo",   // Grupo Isolado 1
                "Igor", "Juliana"    // Grupo Isolado 2
        );

        // Conexões da Rede Principal
        linkedinGraph.addEdge("Ana", "Bruno", 1);
        linkedinGraph.addEdge("Ana", "Carlos", 2);
        linkedinGraph.addEdge("Ana", "Daniela", 8);
        linkedinGraph.addEdge("Bruno", "Eduardo", 1);
        linkedinGraph.addEdge("Carlos", "Eduardo", 1);
        linkedinGraph.addEdge("Daniela", "Fernanda", 5);
        linkedinGraph.addEdge("Eduardo", "Fernanda", 1);
        linkedinGraph.addEdge("Fernanda", "Kevin", 1); // Extensão de 3º grau

        // Conexões dos Grupos Isolados
        linkedinGraph.addEdge("Gabriel", "Hugo", 1);
        linkedinGraph.addEdge("Igor", "Juliana", 1);

        System.out.println("[FASE 1] Rede construída com sucesso!");
        System.out.println("Total de perfis cadastrados: " + linkedinGraph.getVertices().size());

        // Inicializa o Cérebro das Análises
        LinkedInAnalyzer analyzer = new LinkedInAnalyzer(linkedinGraph);

        // ---------------------------------------------------------
        // FASE 2: MAPEAMENTO DE SUB-REDES (Busca Exaustiva / DFS)
        // ---------------------------------------------------------
        System.out.println("\n-----------------------------------------------------");
        System.out.println("[FASE 2] MAPEAMENTO DE COMPONENTES CONEXOS (DFS)");
        System.out.println("Objetivo: Encontrar grupos totalmente isolados na rede");

        List<List<String>> isolatedGroups = analyzer.isolatedGroups();
        System.out.println("-> Total de bolhas (sub-redes) detectadas: " + isolatedGroups.size());
        for (int i = 0; i < isolatedGroups.size(); i++) {
            System.out.println("   Grupo " + (i + 1) + ": " + isolatedGroups.get(i));
        }

        // ---------------------------------------------------------
        // FASE 3: GRAU DE SEPARAÇÃO (Busca em Largura / BFS)
        // ---------------------------------------------------------
        System.out.println("\n-----------------------------------------------------");
        System.out.println("[FASE 3] GRAU DE SEPARAÇÃO (BFS)");
        System.out.println("Objetivo: Encontrar a distância mais curta em número de saltos");

        System.out.println("\n-> Teste: Ana até Kevin (Forçando 3 graus de separação)");
        int saltos = analyzer.degreeOfSeparation("Ana", "Kevin");
        System.out.println("   Resultado: " + (saltos == -1 ? "Inalcançável" : saltos + " salto(s) de distância."));

        // ---------------------------------------------------------
        // FASE 4: ROTA DE MAIOR AFINIDADE (Algoritmo de Dijkstra)
        // ---------------------------------------------------------
        System.out.println("\n-----------------------------------------------------");
        System.out.println("[FASE 4] ROTA DE MAIOR AFINIDADE (DIJKSTRA)");
        System.out.println("Objetivo: O caminho de menor custo acumulado (ignorando nº de saltos)");

        System.out.println("\n-> Teste: Ana até Fernanda");
        PathResult afinidade = analyzer.bestAffinityRoute("Ana", "Fernanda");

        if (afinidade.hasPath()) {
            System.out.println("   Caminho mais curto em passos seria via Daniela (Custo 13).");
            System.out.println("   Mas o Dijkstra escolheu: " + afinidade.path());
            System.out.println("   Custo otimizado encontrado: " + afinidade.totalCost());
        } else {
            System.out.println("   Resultado: Sem rota possível.");
        }

        // ---------------------------------------------------------
        // FASE 5: SUGESTÕES DE CONEXÃO (Amigos em Comum)
        // ---------------------------------------------------------
        System.out.println("\n-----------------------------------------------------");
        System.out.println("[FASE 5] MOTOR DE SUGESTÕES DE CONEXÃO");
        System.out.println("Objetivo: Recomendar amigos de 2º grau ordenados por relevância");

        ConnectionSuggestion suggester = new ConnectionSuggestion(linkedinGraph);

        System.out.println("\n-> Sugestões para Ana:");
        var sugestoesAna = suggester.suggestConnections("Ana");
        if (sugestoesAna.isEmpty()) System.out.println("   Nenhuma sugestão no momento.");
        else sugestoesAna.forEach(s -> System.out.println("   * Sugerindo " + s.profileName() + " (" + s.mutualFriends() + " amigo(s) em comum)"));

        System.out.println("\n-> Sugestões para Gabriel (Perfil em bolha pequena):");
        var sugestoesGab = suggester.suggestConnections("Gabriel");
        if (sugestoesGab.isEmpty()) System.out.println("   Nenhuma sugestão no momento. (Filtro funcionou!)");
        else sugestoesGab.forEach(s -> System.out.println("   * Sugerindo " + s.profileName() + " (" + s.mutualFriends() + " amigo(s) em comum)"));

        System.out.println("\n=====================================================");
        System.out.println("*** ANÁLISE CONCLUÍDA COM SUCESSO! ***");
        System.out.println("=====================================================");
    }
}