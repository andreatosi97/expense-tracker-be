package com.imatcoding.expensetracker.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// When spring-boot-starter-security is on the classpath, autoconfigured default
// security setup is given => this annotation tells SpringBoot to use our security
// configuration if we define a SecurityFilterChain Bean
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/error"
    };

    // SecurityFilterChain: interface with 1 getFilters returning an ordered list
    //      of security Filter 2 RequestMatcher (functional interface
    //      boolean matches(HttpServletRequest req)) deciding which requests it
    //      applies to => can have different chains (set by http.securityMatche(...))
    // HttpSecurity: Builder object configured to produce SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) {
        http
                // configures SessionManagementFilter and session-creation behavior
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // disable Cross-site Request Forgery which exploits auto-attached
                // cookies, not needed if session stateless
                .csrf(AbstractHttpConfigurer::disable)
                // configures AuthorizationFilter with an ordered list of path
                // <-> access rule. AuthorizationFilter get Authentication from
                // SecurityContext set by previous authentication filter
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
