package com.mayank.kafka.debezium.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebeziumController {

    @GetMapping("/status")
    public String getStatus() {
        // You could add logic to return the status of the Debezium engine or any other health check.
        return "Debezium Engine is running";
    }
}