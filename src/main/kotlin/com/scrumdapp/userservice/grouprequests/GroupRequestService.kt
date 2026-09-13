package com.scrumdapp.userservice.grouprequests

interface GroupRequestService {
    fun getUserGroups(userId: Long): List<Long>
}