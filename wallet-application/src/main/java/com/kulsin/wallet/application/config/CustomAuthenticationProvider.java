package com.kulsin.wallet.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${wallet.username}")
    private String actualUsername;
    @Value("${wallet.password}")
    private String actualPassword;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        boolean isValidCredentials = validateCredentials(username, password);

        if (isValidCredentials) {
            return new UsernamePasswordAuthenticationToken(new UserPrincipal("userId"), null, List.of());
        } else {
            throw new UsernameNotFoundException("Authentication failed for user: " + username);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean validateCredentials(String username, String password) {

        return actualUsername.equals(username) && actualPassword.equals(password);

    }

}
