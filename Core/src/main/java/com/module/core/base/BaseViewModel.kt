package com.module.core.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class BaseViewModel : ViewModel(), DefaultLifecycleObserver {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope: CoroutineScope =
        CoroutineScope(viewModelJob + Dispatchers.Main.immediate)

    /**observer to show or hide loading*/
    val isLoading by lazy { MutableLiveData<Boolean>() }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
        viewModelScope.cancel()
    }

    private fun handlerError(
        throwable: Throwable? = null,
        errors: ((err: String?) -> Unit)? = null
    ) {
        throwable?.let {
            println("ddd - ${it.cause}")
            when (it.cause) {
                is UnknownHostException -> {
                    errors?.invoke(it.message ?: "UnknownHostException")
                }

                is java.net.ConnectException -> {
                    errors?.invoke(it.message ?: "java.net.ConnectException")
                }

                else -> {
                    errors?.invoke(null)
                }
            }
        } ?: kotlin.run {
            errors?.invoke(null)
        }
    }

    fun <T> flowOnIO(value: T) = flow {
        emit(value)
    }.flowOn(Dispatchers.IO)

    fun <T> Flow<T>.execute(
        errors: ((err: String?) -> Unit)? = null,
        success: (T) -> Unit
    ) {
        onStart {
            isLoading.value = true
        }.onEach {
            it?.let { data ->
                withContext(Dispatchers.Main) {
                    success.invoke(data)
                    isLoading.value = false
                }
            } ?: kotlin.run {
                isLoading.value = false
            }
        }.catch {
            isLoading.value = false
            Throwable(it).also { throwable ->
                handlerError(throwable, errors)
                throwable.printStackTrace()
            }
        }.launchIn(viewModelScope)
    }

}