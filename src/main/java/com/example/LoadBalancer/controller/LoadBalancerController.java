package com.example.LoadBalancer.controller;

import com.example.LoadBalancer.Server;
import com.example.LoadBalancer.service.LoadBalancingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.Objects; // For null-safe handling

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class LoadBalancerController {

    @Autowired
    private Map<String, LoadBalancingStrategy> strategies;

    @Value("${loadBalancer.strategy}")
    private String strategyName;

    private final WebClient webClient = WebClient.builder().build();

    private final List<Server> servers = Arrays.asList(
            new Server("http://localhost:8081", 5),
            new Server("http://localhost:8082", 3),
            new Server("http://localhost:8083", 2)
    );

    @GetMapping("/**")
    public Mono<ResponseEntity<String>> proxy(
            ServerHttpRequest request,
            @RequestHeader HttpHeaders headers) {

        // Get client IP from HttpServletRequest
        String clientIp = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();

        LoadBalancingStrategy strategy = strategies.get(strategyName);
        Server selectedServer = strategy.selectServer(servers, clientIp);

        selectedServer.getActiveConnections().incrementAndGet();
        log.info("Routing to {} using {} strategy", selectedServer.getUrl(), strategyName);

        // Get path from HttpServletRequest
        String path = request.getURI().getPath();
        String queryString = request.getURI().getQuery();
        String targetUrl = selectedServer.getUrl() + path +
                (queryString != null ? "?" + queryString : "");

        return webClient
                .method(request.getMethod())
                .uri(targetUrl)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(String.class)
                .doFinally(signal -> {
                    selectedServer.getActiveConnections().decrementAndGet();
                    log.info("Request completed for {}", selectedServer.getUrl());
                })
                .onErrorResume(error -> {
                    log.error("Error proxying request: ", error);
                    return Mono.just(ResponseEntity.status(502).body("Bad Gateway"));
                });
    }
}