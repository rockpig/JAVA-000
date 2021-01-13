package com.example.javacourse.activemq;

import org.apache.activemq.broker.BrokerService;

public class ActiveMQServer {
    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();

        broker.addConnector("tcp://127.0.0.1:61616");

        broker.start();
    }
}
