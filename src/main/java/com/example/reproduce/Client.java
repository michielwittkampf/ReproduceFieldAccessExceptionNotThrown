package com.example.reproduce;

import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

@Component
public class Client {

    GraphQlClient graphQlClient = HttpGraphQlClient.builder().url("http://localhost:8080/graphql").build();

    Root getRoot() {
        return graphQlClient.documentName("query")
                .retrieve("root")
                .toEntity(Root.class)
                .block();
    }
}
