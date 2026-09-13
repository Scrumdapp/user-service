package com.scrumdapp.userservice.groupRequests

interface GroupRequestService {
    fun getUserGroups(userId: Long): List<Long>
}