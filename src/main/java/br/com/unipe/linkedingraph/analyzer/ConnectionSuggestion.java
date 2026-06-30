package br.com.unipe.linkedingraph.analyzer;

import br.com.unipe.linkedingraph.graph.Graph;
import br.com.unipe.linkedingraph.graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsável por sugerir novas conexões para um perfil,
 * utilizando o conceito de BFS (amigos de segundo grau).
 */
public class ConnectionSuggestion {

    private final Graph graph;

    public ConnectionSuggestion(Graph graph) {
        this.graph = graph;
    }

    /**
     * Retorna uma lista de sugestões de conexão para um usuário.
     *
     * Regras:
     * - Não sugere o próprio usuário;
     * - Não sugere amigos diretos;
     * - Ordena pela quantidade de amigos em comum (decrescente).
     *
     * @param profileName Nome do perfil que receberá as sugestões.
     * @return Lista de perfis sugeridos com a quantidade de amigos em comum.
     */
    public List<SuggestionResult> suggestConnections(String profileName) {

        Vertex sourceVertex = graph.findVertex(profileName)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Perfil " + profileName + " não encontrado."
                        )
                );

        // Amigos diretos do usuário
        List<Vertex> directFriends = sourceVertex.getAdjacency();

        // Armazena a quantidade de amigos em comum
        Map<Vertex, Integer> mutualFriendsMap = new HashMap<>();

        /*
         * Percorre cada amigo direto do usuário
         * (primeiro nível da BFS)
         */
        for (Vertex friend : directFriends) {

            List<Vertex> friendsOfFriend = friend.getAdjacency()
                    .stream()
                    .sorted(Comparator.comparing(Vertex::getProfileName))
                    .toList();

            /*
             * Percorre os amigos do amigo
             * (segundo nível da BFS)
             */
            for (Vertex candidate : friendsOfFriend) {

                // Não pode sugerir o próprio usuário
                if (candidate.equals(sourceVertex)) {
                    continue;
                }

                // Não pode sugerir quem já é amigo direto
                if (directFriends.contains(candidate)) {
                    continue;
                }

                /*
                 * Se a pessoa já apareceu antes,
                 * incrementa a quantidade de amigos em comum.
                 */
                mutualFriendsMap.put(
                        candidate,
                        mutualFriendsMap.getOrDefault(candidate, 0) + 1
                );
            }
        }

        List<SuggestionResult> suggestions = new ArrayList<>();

        // Converte o Map para uma lista de objetos de retorno
        for (Map.Entry<Vertex, Integer> entry : mutualFriendsMap.entrySet()) {

            suggestions.add(
                    new SuggestionResult(
                            entry.getKey().getProfileName(),
                            entry.getValue()
                    )
            );
        }

        // Ordena do maior número de amigos em comum para o menor
        suggestions.sort(
                Comparator.comparingInt(
                        SuggestionResult::mutualFriends
                ).reversed()
        );

        return suggestions;
    }
}
