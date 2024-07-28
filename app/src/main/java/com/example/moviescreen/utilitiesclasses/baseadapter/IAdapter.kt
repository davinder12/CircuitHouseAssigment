package com.example.moviescreen.utilitiesclasses.baseadapter

import io.reactivex.Observable


interface IAdapter<T> {
    val clicks: Observable<T>

}