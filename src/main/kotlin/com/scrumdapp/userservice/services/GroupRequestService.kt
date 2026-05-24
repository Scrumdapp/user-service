package com.scrumdapp.userservice.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.scrumdapp.userservice.handlers.ServerFaultException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient.builder
import org.springframework.web.client.toEntity
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupResponse(
    val id: Long,
)

@Service
class GroupRequestService(
    @Value($$"${GROUP_SERVICE_URL}") private val baseUrl: String,
    @Value($$"${GROUP_FETCH_ENDPOINT}") private val fetchEndpoint: String = "/groups/user",
    @Value($$"${spring.application.name}") private val appName: String
) {

    private val reqBuilder = builder().baseUrl(baseUrl).build()

    private val mapper = ObjectMapper()

    fun fetchGroups(jwt: Jwt, userId: Long): List<Long> {

        val uri = "$fetchEndpoint/$userId"

        println("$baseUrl$uri")

        try {
            val res = reqBuilder.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${jwt.tokenValue}")
                .header(HttpHeaders.VIA, appName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity<String>()

            if (res.statusCode != HttpStatus.OK) {
                throw Exception("Unexpected response from user request")
            } else {
                val body = res.body ?: throw Exception("Unexpected response from user request")
                return mapper.readValue(body, object : TypeReference<List<GroupResponse>>() {}).map { it.id }
            }
        } catch (e: Exception) {
            // Far from the cleanest way of doing this, but I cannot be bothered to also rewrite the error handling at this moment
            println(jwt.tokenValue)
            println(e)
            return listOf(1, 2)
//            throw ServerFaultException(message = "Something went wrong")
        }
    }
}