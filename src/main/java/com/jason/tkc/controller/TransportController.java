package com.jason.tkc.controller;

import com.jason.tkc.dto.TransportResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transport")
public class TransportController {

    @GetMapping("/{distance}")
    public TransportResponseDTO getCheapestTransport(
            @PathVariable double distance,
            @RequestParam int passengers,
            @RequestParam(defaultValue = "0") int parking
    ) {
        if (passengers < 1 || parking < 0) {
            throw new IllegalArgumentException("Invalid parameters: passengers must be >= 1, parking must be >= 0");
        }

        int numberOfPersonalVehicles = (int) Math.ceil((double) passengers / 4);
        double personalTransportCost = numberOfPersonalVehicles * (0.30 * distance + 5 * parking);

        int numberOfHstcVehicles = (int) Math.ceil((double) passengers / 5);
        double hstcTransportCost = numberOfHstcVehicles * 0.45 * distance;

        if (personalTransportCost <= hstcTransportCost) {
            return new TransportResponseDTO("Personal Transport", personalTransportCost);
        } else {
            return new TransportResponseDTO("HSTC Transport", hstcTransportCost);
        }
    }
}
