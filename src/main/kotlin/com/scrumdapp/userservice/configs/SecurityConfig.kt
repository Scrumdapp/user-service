package com.scrumdapp.userservice.configs

import com.scrumdapp.passportplugin.filters.PassportAuthFilter
import com.scrumdapp.passportplugin.filters.usePassport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val passportAuthFilter: PassportAuthFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .usePassport(passportAuthFilter)
            .authorizeHttpRequests {
                it.requestMatchers("/users/@me").hasAnyRole("STUDENT", "COACH")
                it.requestMatchers(HttpMethod.GET, "/users/{userId}").hasAnyAuthority("STUDENT", "COACH", "GATEWAY")
                it.requestMatchers("/users/gateway").hasAuthority("GATEWAY")
                it.requestMatchers("/users/{userId}/role").hasAuthority("GATEWAY")
            }

        return http.build()
    }
}