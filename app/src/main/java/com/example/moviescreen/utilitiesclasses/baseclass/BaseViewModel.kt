package com.example.moviescreen.utilitiesclasses.baseclass

import androidx.lifecycle.ViewModel
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    /**
     * Container for RxJava subscriptions.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected val subscriptions = CompositeDisposable()

    /**
     * Subscribes to a [Maybe], and optionally disposes the request when the ViewModel is being destroyed
     */
    protected fun <T> subscribe(
        stream: Maybe<T>,
        unsubscribeOnClear: Boolean = false,
        onSuccess: ((T) -> Unit)? = null
    ) {
        val subscription = stream.subscribe(onSuccess ?: {}) { }
        if (unsubscribeOnClear) subscriptions.add(subscription)
    }


    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }


}