package com.vnteam.talktoai.data.network.firestore

import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_CONTEXT_WINDOW
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_DISPLAY_NAME
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_ENABLED
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_MAX_OUTPUT_TOKENS
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_MODELS
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_PROVIDER
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_SCHEMA_VERSION
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_SUPPORTS_TEMPERATURE
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_SUPPORTS_VISION
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_TIER
import com.vnteam.talktoai.domain.aimodels.RawAiModel
import com.vnteam.talktoai.domain.aimodels.RawAiModelsConfig

private fun FirestoreValue?.asLong(): Long? =
    this?.integerValue?.toLongOrNull() ?: this?.doubleValue?.toLong()

fun FirestoreDocument.toRawAiModelsConfig(): RawAiModelsConfig {
    val f = fields.orEmpty()
    val models = f[FIELD_MODELS]?.mapValue.orEmpty().map { (id, value) ->
        val m = value.mapValue.orEmpty()
        RawAiModel(
            id = id,
            displayName = m[FIELD_DISPLAY_NAME]?.stringValue,
            provider = m[FIELD_PROVIDER]?.stringValue,
            tier = m[FIELD_TIER]?.stringValue,
            supportsTemperature = m[FIELD_SUPPORTS_TEMPERATURE]?.booleanValue,
            supportsVision = m[FIELD_SUPPORTS_VISION]?.booleanValue,
            contextWindow = m[FIELD_CONTEXT_WINDOW].asLong(),
            maxOutputTokens = m[FIELD_MAX_OUTPUT_TOKENS].asLong(),
            enabled = m[FIELD_ENABLED]?.booleanValue,
        )
    }
    return RawAiModelsConfig(schemaVersion = f[FIELD_SCHEMA_VERSION].asLong(), models = models)
}
