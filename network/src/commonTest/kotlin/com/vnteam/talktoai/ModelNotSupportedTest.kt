package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.isModelNotSupportedError
import com.vnteam.talktoai.data.network.parseErrorMessage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModelNotSupportedTest {

    private val notFoundBody = """{"error":{"type":"not_found_error","message":"The model `gpt-old` does not exist or you do not have access to it."}}"""
    private val invalidRequestModelBody = """{"error":{"type":"invalid_request_error","message":"model `xyz` is not supported"}}"""
    private val invalidRequestOtherBody = """{"error":{"type":"invalid_request_error","message":"max_tokens must be positive"}}"""
    private val authBody = """{"error":{"type":"authentication_error","message":"Invalid API key"}}"""

    @Test
    fun notFoundErrorWithModelInMessageReturnsTrue() {
        assertTrue(isModelNotSupportedError(400, notFoundBody))
    }

    @Test
    fun invalidRequestErrorWithModelInMessageReturnsTrue() {
        assertTrue(isModelNotSupportedError(400, invalidRequestModelBody))
    }

    @Test
    fun invalidRequestErrorWithoutModelInMessageReturnsFalse() {
        assertFalse(isModelNotSupportedError(400, invalidRequestOtherBody))
    }

    @Test
    fun authenticationErrorReturnsFalse() {
        assertFalse(isModelNotSupportedError(400, authBody))
    }

    @Test
    fun non400StatusReturnsFalse() {
        assertFalse(isModelNotSupportedError(404, notFoundBody))
        assertFalse(isModelNotSupportedError(500, notFoundBody))
        assertFalse(isModelNotSupportedError(200, notFoundBody))
    }

    @Test
    fun malformedJsonReturnsFalse() {
        assertFalse(isModelNotSupportedError(400, "not json at all"))
        assertFalse(isModelNotSupportedError(400, ""))
    }

    @Test
    fun parseErrorMessageExtractsMessageFromValidBody() {
        val msg = parseErrorMessage(notFoundBody)
        assertTrue(msg.contains("gpt-old"))
    }

    @Test
    fun parseErrorMessageReturnsRawBodyForMalformedJson() {
        val raw = "not json"
        assertEquals(raw, parseErrorMessage(raw))
    }
}
