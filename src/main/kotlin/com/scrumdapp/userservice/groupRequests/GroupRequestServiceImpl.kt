package com.scrumdapp.userservice.groupRequests

import com.scrumdapp.userservice.exceptions.ServiceUnavailableException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient.builder
import org.springframework.web.client.toEntity
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@Service
class GroupRequestServiceImpl(
    @Value($$"${GROUP_SERVICE_URL}") private val baseUrl: String,
    @Value($$"${GROUP_FETCH_ENDPOINT}") private val fetchEndpoint: String = "/groups/user",
    @Value($$"${spring.application.name}") private val appName: String
): GroupRequestService {
    private val reqBuilder = builder().baseUrl(baseUrl).build()
    private val mapper = ObjectMapper()

    override fun getUserGroups(userId: Long): List<Long> {
        val uri = "$fetchEndpoint/$userId"
        val res = performGroupRequest<List<GroupResponse>>(uri).map { it.id }
        return res
    }

    private fun <T> performGroupRequest(uri: String): T {

        // Authentication principal does exist by this point
        val jwt = SecurityContextHolder.getContext().authentication?.principal as Jwt
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
                return mapper.readValue(body, object : TypeReference<T>() {})
            }
        } catch (e: Exception) {
            throw ServiceUnavailableException(message = "Downstream service is unreachable")
        }
    }
}