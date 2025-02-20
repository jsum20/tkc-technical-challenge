package com.jason.tkc.service;

import com.jason.tkc.model.Connection;
import com.jason.tkc.model.Gate;
import com.jason.tkc.repository.GateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GraphServiceTest {

    @Mock
    private GateRepository gateRepository;

    @InjectMocks
    private GraphService graphService;

    @BeforeEach
    void setUp() {
        // Mock data
        List<Connection> connections = new ArrayList<>();
        Connection ran = new Connection();
        ran.setId("RAN");
        ran.setHu(100);

        Connection prox = new Connection();
        prox.setId("PRX");
        prox.setHu(90);

        connections.add(ran);
        connections.add(prox);

        Gate gateA = new Gate();
        gateA.setId("SOL");
        gateA.setName("sol");
        gateA.setConnections(connections);

        when(gateRepository.findAll()).thenReturn(Collections.singletonList(gateA));

        graphService.buildGraph();
    }

    @Test
    void testBuildGraph() {
        HashMap<String, List<Connection>> graph = graphService.getGraph();

        assertNotNull(graph);
        assertEquals(1, graph.size());
        assertTrue(graph.containsKey("sol"));

        List<Connection> solConnections = graph.get("sol");
        assertEquals(2, solConnections.size());

        assertEquals("RAN", solConnections.get(0).getId());
        assertEquals(100, solConnections.get(0).getHu());

        assertEquals("PRX", solConnections.get(1).getId());
        assertEquals(90, solConnections.get(1).getHu());
    }
}
