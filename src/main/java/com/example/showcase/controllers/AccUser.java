package com.example.showcase.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccUser {
    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @GetMapping("/user")
    public Map<String, Object> getUserInfo(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("accessToken", accessToken);
        userInfo.put("attributes", authentication.getPrincipal().getAttributes());
        return userInfo;
    }
}