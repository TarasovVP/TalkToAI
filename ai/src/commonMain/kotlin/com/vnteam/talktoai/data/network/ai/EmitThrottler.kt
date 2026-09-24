package com.vnteam.talktoai.data.network.ai

import kotlin.time.TimeSource

internal class EmitThrottler(private val minIntervalMs: Long = 50) {
    private var lastEmitMark = TimeSource.Monotonic.markNow()
    private var everEmitted = false

    fun shouldEmit(): Boolean {
        if (!everEmitted) return true
        return lastEmitMark.elapsedNow().inWholeMilliseconds >= minIntervalMs
    }

    fun markEmitted() {
        everEmitted = true
        lastEmitMark = TimeSource.Monotonic.markNow()
    }
}
