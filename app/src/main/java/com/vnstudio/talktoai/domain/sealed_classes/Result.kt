<<<<<<<< HEAD:app/src/main/java/com/vnstudio/talktoai/domain/sealed_classes/Result.kt
package com.vnstudio.talktoai.domain.sealed_classes
========
package com.vnstudio.talktoai.data.network
>>>>>>>> 467a644 (Add firebase to project):app/src/main/java/com/vnstudio/talktoai/data/network/Result.kt

sealed class Result<out T> {
    data class Success<T>(val data: T? = null) : Result<T>()
    data class Failure(val errorMessage: String? = null) : Result<Nothing>()
}
