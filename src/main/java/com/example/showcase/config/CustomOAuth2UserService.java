package com.example.showcase.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.example.showcase.entity.User;
import com.example.showcase.entity.Role;
import com.example.showcase.service.RoleService;
import com.example.showcase.repository.UserRepository;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    @Autowired
    private RoleService roleService;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println("CustomOAuth2UserService initialized!");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = (String) oAuth2User.getAttributes().get("upn"); //email

        Map<String, Object> attributes = oAuth2User.getAttributes();
    
        System.out.println("OAuth2 Provider: " + userRequest.getClientRegistration().getRegistrationId());
        System.out.println("Attributes: " + attributes);  // Логируем все атрибуты

        //String email = getEmailFromAttributes(userRequest.getClientRegistration().getRegistrationId(), attributes);

        System.out.println(email + " wow");
        if (email == null) {
            throw new IllegalArgumentException("Email is null. User cannot be registered.");
        }

        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            Role role = roleService.getRoleById(1);
            user.setRole(role);
            userRepository.save(user);
        } else {
            System.out.println("This email " + email + " already has");
        }

        return oAuth2User;
    }

    private String getEmailFromAttributes(String registrationId, Map<String, Object> attributes) {
        // Для GitHub
        if ("github".equalsIgnoreCase(registrationId)) {
            return (String) attributes.get("email");
        }
        // Для Azure AD
        else if ("azure".equalsIgnoreCase(registrationId)) {
            // В Azure AD email может быть в поле "upn" или "email"
            String email = (String) attributes.get("upn");
            if (email == null) {
                email = (String) attributes.get("email");
            }
            return email;
        }
        // Для других провайдеров
        else {
            return (String) attributes.get("email");
        }
    }
}
