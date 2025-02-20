package com.jason.tkc.service;

import com.jason.tkc.model.Connection;

import java.util.*;

public class DijkstraPathfinder {

    public static List<String> findShortestRoute(
            Map<String, List<Connection>> graph,
            String startGate,
            String endGate
    ) {
        if (!graph.containsKey(startGate) || !graph.containsKey(endGate)) {
            return null;
        }

        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(Map.Entry.comparingByValue());

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        distances.put(startGate, 0);

        minHeap.add(new AbstractMap.SimpleEntry<>(startGate, 0));

        while (!minHeap.isEmpty()) {
            Map.Entry<String, Integer> current = minHeap.poll();
            String currentGate = current.getKey();
            int currentDistance = current.getValue();

            if (currentGate.equals(endGate)) {
                break;
            }

            for (Connection c : graph.getOrDefault(currentGate, Collections.emptyList())) {
                String neighbour = c.getId();
                int newDistance = currentDistance + c.getHu();

                if (newDistance < distances.getOrDefault(neighbour, Integer.MAX_VALUE)) {
                    distances.put(neighbour, newDistance);
                    previous.put(neighbour, currentGate);
                    minHeap.add(new AbstractMap.SimpleEntry<>(neighbour, newDistance));
                }
            }
        }

        if (!previous.containsKey(endGate)) {
            return null;
        }

        List<String> route = new ArrayList<>();
        String current = endGate;
        while (current != null) {
            route.add(current);
            current = previous.get(current);
        }

        Collections.reverse(route);

        return route;
    }
}
