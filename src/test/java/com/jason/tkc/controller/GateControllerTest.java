package com.jason.tkc.controller;

import com.jason.tkc.model.Gate;
import com.jason.tkc.repository.GateRepository;
import com.jason.tkc.service.DijkstraPathfinder;
import com.jason.tkc.service.GraphService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GraphService graphService;

    @Mock
    private GateRepository gateRepository;

    @InjectMocks
    private GateController gateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gateController).build();
    }

    @Test
    void testGetAllGates() throws Exception {
        Gate testGate1 = new Gate();
        testGate1.setId("SOL");
        testGate1.setName("Sol");
        testGate1.setConnections(new ArrayList<>());

        Gate testGate2 = new Gate();
        testGate2.setId("PRX");
        testGate2.setName("Proxima");
        testGate2.setConnections(new ArrayList<>());

        List<Gate> gates = Arrays.asList(testGate1, testGate2);

        when(gateRepository.findAll()).thenReturn(gates);

        mockMvc.perform(get("/gates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("SOL"))
                .andExpect(jsonPath("$[0].name").value("Sol"))
                .andExpect(jsonPath("$[0].connections").isArray())
                .andExpect(jsonPath("$[1].id").value("PRX"))
                .andExpect(jsonPath("$[1].name").value("Proxima"))
                .andExpect(jsonPath("$[1].connections").isArray());
    }

    @Test
    public void testGetGate_Exists() throws Exception {
        Gate testGate = new Gate();
        testGate.setId("SOL");
        testGate.setName("Sol");
        testGate.setConnections(Collections.emptyList());

        when(gateRepository.findById("SOL")).thenReturn(Optional.of(testGate));

        mockMvc.perform(get("/gates/SOL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("SOL"))
                .andExpect(jsonPath("$.name").value("Sol"))
                .andExpect(jsonPath("$.connections").isArray());
    }

    @Test
    void testGetShortestRoute_success() throws Exception {
        List<String> mockPath = Arrays.asList("Gate1", "Gate2", "Gate3");
        try (MockedStatic<DijkstraPathfinder> mockedStatic = Mockito.mockStatic(DijkstraPathfinder.class)) {

            mockedStatic.when(() -> DijkstraPathfinder.findShortestRoute(any(), eq("Gate1"), eq("Gate3")))
                    .thenReturn(mockPath);

            mockMvc.perform(get("/gates/{gateCode}/to/{targetGateCode}", "Gate1", "Gate3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0]").value("Gate1"))
                    .andExpect(jsonPath("$[1]").value("Gate2"))
                    .andExpect(jsonPath("$[2]").value("Gate3"));

            mockedStatic.verify(() -> DijkstraPathfinder.findShortestRoute(any(), eq("Gate1"), eq("Gate3")), times(1));
        }
    }

    @Test
    void testGetShortestRouteNotFound() throws Exception {
        try (MockedStatic<DijkstraPathfinder> mockedStatic = Mockito.mockStatic(DijkstraPathfinder.class)) {

            mockedStatic.when(() -> DijkstraPathfinder.findShortestRoute(any(), eq("Gate1"), eq("Gate3")))
                    .thenReturn(null);

            mockMvc.perform(get("/gates/{fromGate}/to/{toGate}", "Gate1", "Gate3"))
                    .andExpect(status().isNotFound());

            mockedStatic.verify(() -> DijkstraPathfinder.findShortestRoute(any(), eq("Gate1"), eq("Gate3")), times(1));
        }
    }
}
