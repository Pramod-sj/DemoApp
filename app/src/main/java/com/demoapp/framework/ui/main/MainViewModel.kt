package com.demoapp.framework.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demoapp.business.interactor.GetRandomShadiUserUseCase
import com.demoapp.business.interactor.UpdateUserAcceptStateUseCase
import com.demoapp.business.model.ACCEPT_STATE_ACCEPT
import com.demoapp.business.model.ACCEPT_STATE_DECLINE
import com.demoapp.business.model.AcceptState
import com.demoapp.business.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomShadiUserUseCase: GetRandomShadiUserUseCase,
    private val updateUserAcceptStateUseCase: UpdateUserAcceptStateUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }


    private val _userIntent = MutableSharedFlow<UserIntent>()

    private val _uiState = MutableStateFlow(
        UiState(
            users = emptyList(),
            showContentLoader = false,
            showSwipeToRefreshLoader = false
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val currentUIState: UiState
        get() = _uiState.value

    private fun setIntent(userIntent: UserIntent) {
        viewModelScope.launch {
            _userIntent.emit(userIntent)
        }
    }

    fun fetchUser(forceFetch: Boolean) {
        if (forceFetch) {
            viewModelScope.launch {
                _uiEvent.emit(
                    UiEvent.SnackBarMessage(
                        message = "Are you sure? You will loose the user data!",
                        action = "Fetch users",
                        actionCallback = {
                            setIntent(UserIntent.RefreshUser)
                        },
                        noActionCallback = {
                            _uiState.value = currentUIState.copy(showSwipeToRefreshLoader = true)
                            _uiState.value = currentUIState.copy(showSwipeToRefreshLoader = false)
                        }
                    )
                )
            }
        } else setIntent(UserIntent.FetchUser)
    }

    fun acceptUser(userId: String) {
        setIntent(UserIntent.UpdateAcceptanceState(userId, ACCEPT_STATE_ACCEPT))
    }

    fun declineUser(userId: String) {
        setIntent(UserIntent.UpdateAcceptanceState(userId, ACCEPT_STATE_DECLINE))
    }

    private fun handleIntent() {
        var shadiUserFetchJob: Job? = null
        viewModelScope.launch {
            _userIntent.collect { intent ->
                when (intent) {
                    UserIntent.FetchUser -> {
                        shadiUserFetchJob?.cancel()
                        shadiUserFetchJob = fetchShadiUsers(false)
                    }

                    UserIntent.RefreshUser -> {
                        shadiUserFetchJob?.cancel()
                        shadiUserFetchJob = fetchShadiUsers(true)
                    }

                    is UserIntent.UpdateAcceptanceState -> {
                        updateUserAcceptStateUseCase(intent.userId, intent.acceptState)
                    }

                }
            }
        }
    }

    private fun fetchShadiUsers(forceFetch: Boolean): Job {
        return viewModelScope.launch {

            _uiState.value =
                if (forceFetch) currentUIState.copy(showSwipeToRefreshLoader = true)
                else currentUIState.copy(showContentLoader = true)

            getRandomShadiUserUseCase(forceFetch).collect {
                Log.i(TAG, it.toString())
                when (it) {
                    is GetRandomShadiUserUseCase.UseCaseResult.GenericFailure -> {
                        _uiState.value =
                            currentUIState.copy(
                                users = it.data,
                                showContentLoader = false,
                                showSwipeToRefreshLoader = false
                            )
                        _uiEvent.emit(UiEvent.ShowToastMessage(it.message))
                    }

                    is GetRandomShadiUserUseCase.UseCaseResult.Success -> {
                        _uiState.value =
                            currentUIState.copy(
                                users = it.data,
                                showContentLoader = false,
                                showSwipeToRefreshLoader = false
                            )
                    }
                }
            }
        }
    }

    init {
        handleIntent()
    }


}

data class UiState(
    val users: List<User>,
    val showContentLoader: Boolean,
    val showSwipeToRefreshLoader: Boolean
)

sealed class UiEvent {

    data class ShowToastMessage(val message: String) : UiEvent()

    data class SnackBarMessage(
        val message: String,
        val action: String? = null,
        val actionCallback: (() -> Unit)? = null,
        val noActionCallback: (() -> Unit)? = null
    ) : UiEvent()
}

sealed class UserIntent {

    object FetchUser : UserIntent() //Fetching data for the 1st time

    object RefreshUser : UserIntent() //For fetching new set of data

    data class UpdateAcceptanceState(
        val userId: String, @AcceptState val acceptState: Int
    ) : UserIntent() //updating accepted or decline state

}