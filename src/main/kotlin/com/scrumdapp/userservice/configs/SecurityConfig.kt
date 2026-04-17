package com.scrumdapp.userservice.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
class SecurityConfig(

): WebMvcConfigurer {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { it ->
                it.requestMatchers("/users/@me").hasAnyRole("STUDENT", "COACH")
                it.requestMatchers("/users/{userId}").hasAnyRole("STUDENT", "COACH", "GATEWAY")
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.decoder(customJwtDecoder())
                    jwt.jwtAuthenticationConverter(jwtAuthConverter())
                }
            }

        return http.build()
    }
}