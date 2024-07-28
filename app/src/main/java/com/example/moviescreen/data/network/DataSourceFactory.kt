package com.example.moviescreen.data.network

import androidx.paging.DataSource

class DataSourceFactory<LocalType, RemoteType>(private val pageList: PaginationList<LocalType, RemoteType>) :
    DataSource.Factory<Int, LocalType>() {
    override fun create(): DataSource<Int, LocalType> = pageList
}

