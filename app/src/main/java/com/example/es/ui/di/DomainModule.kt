package com.example.es.ui.di

import com.example.es.domain.FetchDataUseCase
import com.example.es.domain.Repository
import com.example.es.ui.model.MapDomainToUi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideFetchDataUseCase(repository: Repository): FetchDataUseCase =
        FetchDataUseCase.Base(repository = repository)

    @Provides
    fun provideMapDomainToUi(): MapDomainToUi = MapDomainToUi.Base()
}