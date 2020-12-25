package com.example.search.di

import com.example.search.data.RetrofitBuilder
import com.example.search.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module() {
    viewModel { MainViewModel(get()) }
}

val repositoryModule = module() {
    single { RetrofitBuilder.buildRetrofit() }
}

val appModules = listOf(viewModelModule, repositoryModule)