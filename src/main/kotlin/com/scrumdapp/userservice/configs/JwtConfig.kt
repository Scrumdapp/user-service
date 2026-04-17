package com.scrumdapp.userservice.configs

import org.springframework.context.annotation.Bean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import java.util.stream.Collectors

@Bean
public fun customJwtDecoder(
    discoveryClient: DiscoveryClient,
): JwtDecoder {
    //TODO(Change uri to random gateway instance)

    val gatewayInstances = discoveryClient.getInstances("GATEWAY")[0]
    val decoder = NimbusJwtDecoder.withJwkSetUri("${gatewayInstances.uri}/.well-known/jwks.json").build()

    decoder.setJwtValidator(JwtValidators.createDefault())
    return decoder
}

@Bean
fun jwtAuthConverter(): JwtAuthenticationConverter {
    val converter = JwtAuthenticationConverter()
    converter.setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
        val rolesClaim = jwt.getClaim<Any>("roles")

        val roles: List<String>
        when (rolesClaim) {
            is List<*> -> {
                roles = rolesClaim.stream().map { obj: Any? -> obj.toString() }.toList()
            }
            is String -> {
                roles = listOf(rolesClaim)
            }
            else -> {
                roles = listOf()
            }
        }
        roles.stream()
            .map { role: String -> SimpleGrantedAuthority("ROLE_$role") }
            .collect(Collectors.toList())
    }
    return converter
}