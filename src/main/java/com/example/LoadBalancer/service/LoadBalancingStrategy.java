package com.example.LoadBalancer.service;

import com.example.LoadBalancer.Server;
import java.util.List;

public interface LoadBalancingStrategy {
    Server selectServer(List<Server> servers, String clientIp);
}
