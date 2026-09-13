package com.scrumdapp.userservice.groupRequests

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
@ConditionalOnBooleanProperty(name=["USE_MOCK_SERVICE"], havingValue = true)
class MockGroupRequestService: GroupRequestService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        logger.info("Using mock group service")
    }

    override fun getUserGroups(userId: Long): List<Long> {
        logger.info("fetched user groups using mock group service")
        return listOf(1, 2, 3)
    }
}