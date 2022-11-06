package com.example.es.ui.di

import com.example.es.domain.usecases.FetchUseCase
import com.example.es.domain.Repository
import com.example.es.domain.usecases.PostUseCase
import com.example.es.ui.model.MapDomainToUi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideFetchUseCase(repository: Repository): FetchUseCase =
        FetchUseCase.Base(repository = repository)

    @Provides
    fun providePostUseCase(repository: Repository): PostUseCase =
        PostUseCase.Base(repository = repository)

    @Provides
    fun provideMapDomainToUi(): MapDomainToUi = MapDomainToUi.Base()
}