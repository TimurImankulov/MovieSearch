package com.example.search

import android.app.Application
import com.example.search.di.appModules
import org.koin.android.ext.android.startKoin

class SearchApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }
}