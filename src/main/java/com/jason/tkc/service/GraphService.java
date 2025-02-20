package com.jason.tkc.service;

import com.jason.tkc.model.Connection;
import com.jason.tkc.model.Gate;
import com.jason.tkc.repository.GateRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class GraphService {
    @Getter
    private final HashMap<String, List<Connection>> graph = new HashMap<>();

    private final GateRepository gateRepository;

    @Autowired
    public GraphService(GateRepository gateRepository) {
        this.gateRepository = gateRepository;
    }

    @PostConstruct
    public void buildGraph() {
        List<Gate> gates = gateRepository.findAll();

        for (Gate gate : gates) {
            graph.put(gate.getName(), gate.getConnections());
        }
    }
}
