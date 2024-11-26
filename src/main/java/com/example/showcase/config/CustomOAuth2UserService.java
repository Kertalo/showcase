package com.example.showcase.config;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = (String) oAuth2User.getAttributes().get("email");
        if (email == null) {
            // Дополнительный запрос на получение email
            RestTemplate restTemplate = new RestTemplate();
            String token = userRequest.getAccessToken().getTokenValue();
            String emailsUrl = "https://api.github.com/user/emails";

            // Заголовок Authorization для запроса email
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            // Выполнить запрос
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                emailsUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
            );

            List<Map<String, Object>> emails = response.getBody();

            if (emails != null) {
                for (Map<String, Object> emailData : emails) {
                    if ((boolean) emailData.get("primary")) {
                        email = (String) emailData.get("email");
                        break;
                    }
                }
            }
        }

        System.out.println("User email: " + email);

        return oAuth2User;
    }
}
