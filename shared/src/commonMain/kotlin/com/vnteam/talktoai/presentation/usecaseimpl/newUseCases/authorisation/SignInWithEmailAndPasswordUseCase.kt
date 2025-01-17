package com.vnteam.talktoai.presentation.usecaseimpl.newUseCases.authorisation

import com.vnteam.talktoai.Constants
import com.vnteam.talktoai.data.network.Result
import com.vnteam.talktoai.domain.repositories.AuthRepository
import com.vnteam.talktoai.domain.usecase.UseCase
import com.vnteam.talktoai.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SignInWithEmailAndPasswordUseCase(
    private val repository: AuthRepository,
    private val networkState: NetworkState
) : UseCase<Pair<String, String>, Flow<Result<String>>> {

    override suspend fun execute(params: Pair<String, String>): Flow<Result<String>> {
        return when {
            networkState.isNetworkAvailable() -> repository.signInWithEmailAndPassword(
                params.first,
                params.second
            ).map {
                Result.Success(it)
            }.catch {
                Result.Failure(it.message)
            }

            else -> flowOf(Result.Failure(Constants.APP_NETWORK_UNAVAILABLE_REPEAT))
        }
    }
}