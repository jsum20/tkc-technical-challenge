package com.jason.tkc.service;

import com.jason.tkc.model.Connection;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DijkstraPathfinderTest {

    private HashMap<String, List<Connection>> createGraph() {
        HashMap<String, List<Connection>> graph = new HashMap<>();

        Connection solToPrx = new Connection();
        solToPrx.setId("PRX");
        solToPrx.setHu(90);

        Connection solToRan = new Connection();
        solToRan.setId("RAN");
        solToRan.setHu(100);

        Connection prxToSir = new Connection();
        prxToSir.setId("SIR");
        prxToSir.setHu(100);

        Connection sirToCas = new Connection();
        sirToCas.setId("CAS");
        sirToCas.setHu(200);

        graph.put("SOL", Arrays.asList(solToPrx, solToRan));
        graph.put("PRX", List.of(prxToSir));
        graph.put("SIR", List.of(sirToCas));
        graph.put("CAS", new ArrayList<>());
        graph.put("RAN", new ArrayList<>());

        return graph;
    }

    @Test
    void testFindShortestRoute_SOL_to_SIR() {
        Map<String, List<Connection>> graph = createGraph();

        List<String> shortestRoute = DijkstraPathfinder.findShortestRoute(graph, "SOL", "SIR");

        List<String> expectedRoute = Arrays.asList("SOL", "PRX", "SIR");

        assertEquals(expectedRoute, shortestRoute);
    }

    @Test
    void testFindShortestRoute_NoPath() {
        Map<String, List<Connection>> graph = createGraph();

        List<String> shortestRoute = DijkstraPathfinder.findShortestRoute(graph, "SOL", "ABC");

        assertNull(shortestRoute);
    }
}
