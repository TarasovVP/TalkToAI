package com.vnteam.talktoai.data.network.firestore

import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_CONTEXT_WINDOW
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_DISPLAY_NAME
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_ENABLED
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_ID
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_MAX_OUTPUT_TOKENS
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_SCHEMA_VERSION
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_SUPPORTS_TEMPERATURE
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_SUPPORTS_VISION
import com.vnteam.talktoai.data.network.firestore.FirestoreConstants.FIELD_TIERS
import com.vnteam.talktoai.domain.aimodels.RawAiModel
import com.vnteam.talktoai.domain.aimodels.RawAiModelsConfig

private fun FirestoreValue?.asLong(): Long? =
    this?.integerValue?.toLongOrNull() ?: this?.doubleValue?.toLong()

fun FirestoreDocument.toRawAiModelsConfig(): RawAiModelsConfig {
    val f = fields.orEmpty()
    val models = f[FIELD_TIERS]?.mapValue.orEmpty().flatMap { (tierName, providerMap) ->
        providerMap.mapValue.orEmpty().map { (providerName, value) ->
            val m = value.mapValue.orEmpty()
            RawAiModel(
                id = m[FIELD_ID]?.stringValue.orEmpty(),
                displayName = m[FIELD_DISPLAY_NAME]?.stringValue,
                provider = providerName,
                tier = tierName,
                supportsTemperature = m[FIELD_SUPPORTS_TEMPERATURE]?.booleanValue,
                supportsVision = m[FIELD_SUPPORTS_VISION]?.booleanValue,
                contextWindow = m[FIELD_CONTEXT_WINDOW].asLong(),
                maxOutputTokens = m[FIELD_MAX_OUTPUT_TOKENS].asLong(),
                enabled = m[FIELD_ENABLED]?.booleanValue,
            )
        }
    }
    return RawAiModelsConfig(schemaVersion = f[FIELD_SCHEMA_VERSION].asLong(), models = models)
}
