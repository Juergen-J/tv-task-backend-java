package com.teclead.ventures.javatask.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI customOpenApi() {
        final var components = new Components();

        List<Server> servers = new LinkedList<>();
        servers.add(new Server().url("http://localhost:" + port));

        return new OpenAPI().components(components)
                .info(new Info()
                        .title("Recruiting Task Backend - Java API")
                        .version("v1")
                        .description("")
                        .contact(new Contact().email("yurk.jur@gmail.com")))
                .servers(servers);
    }
}
