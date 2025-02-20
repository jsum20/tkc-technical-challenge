package com.jason.tkc.controller;

import com.jason.tkc.model.Gate;
import com.jason.tkc.repository.GateRepository;
import com.jason.tkc.service.DijkstraPathfinder;
import com.jason.tkc.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gates")
public class GateController {
    private final GateRepository gateRepository;
    private final GraphService graphService;

    @Autowired
    public GateController(GateRepository gateRepository, GraphService graphService) {
        this.gateRepository = gateRepository;
        this.graphService = graphService;
    }

    @GetMapping
    public List<Gate> getAllGates() {
        return gateRepository.findAll();
    }

    @GetMapping("/{gateCode}")
    public ResponseEntity<Gate> getGate(@PathVariable String gateCode) {
        Optional<Gate> gate = gateRepository.findById(gateCode);
        return gate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{gateCode}/to/{targetGateCode}")
    public ResponseEntity<?> getRoute(@PathVariable String gateCode, @PathVariable String targetGateCode) {
        List<String> result = DijkstraPathfinder.findShortestRoute(graphService.getGraph(), gateCode, targetGateCode);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }
}
