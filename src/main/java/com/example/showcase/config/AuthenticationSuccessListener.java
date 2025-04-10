package com.example.showcase.config;

import com.example.showcase.entity.Role;
import com.example.showcase.entity.User;
import com.example.showcase.repository.UserRepository;
import com.example.showcase.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        String email = "";
        String name = "";
        if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) event.getAuthentication().getPrincipal();
            email = (String) oidcUser.getAttributes().get("email");
            name =  (String) oidcUser.getAttributes().get("name");
        }
        else if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) event.getAuthentication().getPrincipal();
            email = (String) oauth2User.getAttributes().get("email");
            name =  (String) oauth2User.getAttributes().get("login");
        }

        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setFullName(name);
            Role role = roleService.getRoleById(1);
            user.setRole(role);
            userRepository.save(user);
        } else {
            System.out.println("This email " + email + " already has");
        }
    }
}