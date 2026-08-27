package com.vnteam.talktoai

import com.vnteam.talktoai.data.network.isModelNotSupportedError
import com.vnteam.talktoai.data.network.parseErrorMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelNotSupportedTest {

    private val notFoundBody = """
        {"error":{"type":"not_found_error","message":"The model `gpt-old` does not exist or you do not have access to it."}}
    """.trimIndent()

    private val invalidRequestModelBody = """
        {"error":{"type":"invalid_request_error","message":"model `xyz` is not supported"}}
    """.trimIndent()

    private val invalidRequestOtherBody = """
        {"error":{"type":"invalid_request_error","message":"max_tokens must be positive"}}
    """.trimIndent()

    private val authBody = """
        {"error":{"type":"authentication_error","message":"Invalid API key"}}
    """.trimIndent()

    @Test
    fun `not_found_error with model in message returns true`() {
        assertTrue(isModelNotSupportedError(400, notFoundBody))
    }

    @Test
    fun `invalid_request_error with model in message returns true`() {
        assertTrue(isModelNotSupportedError(400, invalidRequestModelBody))
    }

    @Test
    fun `invalid_request_error without model in message returns false`() {
        assertFalse(isModelNotSupportedError(400, invalidRequestOtherBody))
    }

    @Test
    fun `authentication_error returns false`() {
        assertFalse(isModelNotSupportedError(400, authBody))
    }

    @Test
    fun `non-400 status code returns false even with model-error body`() {
        assertFalse(isModelNotSupportedError(404, notFoundBody))
        assertFalse(isModelNotSupportedError(500, notFoundBody))
        assertFalse(isModelNotSupportedError(200, notFoundBody))
    }

    @Test
    fun `malformed JSON returns false`() {
        assertFalse(isModelNotSupportedError(400, "not json at all"))
        assertFalse(isModelNotSupportedError(400, ""))
    }

    @Test
    fun `parseErrorMessage extracts message from valid body`() {
        val msg = parseErrorMessage(notFoundBody)
        assertTrue(msg.contains("gpt-old"))
    }

    @Test
    fun `parseErrorMessage returns raw body for malformed JSON`() {
        val raw = "not json"
        assertEquals(raw, parseErrorMessage(raw))
    }
}
