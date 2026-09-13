package com.scrumdapp.userservice.groupRequests

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupResponse(
    val id: Long,
)