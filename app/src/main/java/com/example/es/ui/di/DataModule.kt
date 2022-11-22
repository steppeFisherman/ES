package com.example.es.ui.di

import android.content.Context
import com.example.es.data.model.MapCacheToDomain
import com.example.es.data.model.MapCloudToCache
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.model.MapDomainToCloud
import com.example.es.data.repository.*
import com.example.es.data.room.AppRoomDao
import com.example.es.data.room.AppRoomDatabase
import com.example.es.domain.Repository
import com.example.es.utils.SnackBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

//    @Provides
//    @Singleton
//    fun provideConnectionLiveData(@ApplicationContext context: Context): ConnectionLiveData =
//        ConnectionLiveData(context = context)

    @Provides
    @Singleton
    fun provideSnackBuilder(): SnackBuilder = SnackBuilder.Base()

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context): AppRoomDao =
        AppRoomDatabase.getInstance(context = context).getAppRoomDao()

    @Provides
    @Singleton
    fun provideMapCacheToDomain(): MapCacheToDomain =
        MapCacheToDomain.Base()

    @Provides
    @Singleton
    fun provideMapCloudToCache(): MapCloudToCache =
        MapCloudToCache.Base()

    @Provides
    @Singleton
    fun provideMapCloudToDomain(): MapCloudToDomain =
        MapCloudToDomain.Base()

    @Provides
    @Singleton
    fun provideMapDomainToCloud(): MapDomainToCloud =
        MapDomainToCloud.Base()

    @Provides
    fun provideDispatchers(): ToDispatch = ToDispatch.Base()

    @Provides
    fun provideExceptionHandle(): ExceptionHandle =
        ExceptionHandle.Base()

    @Provides
    fun provideCloudSource(
        appDao: AppRoomDao,
        mapCacheToDomain: MapCacheToDomain,
        mapCloudToCache: MapCloudToCache,
        mapCloudToDomain: MapCloudToDomain,
        mapDomainToCloud: MapDomainToCloud,
        dispatchers: ToDispatch,
        exceptionHandle: ExceptionHandle,
    ): CloudSource = CloudSource.Base(
        appDao = appDao,
        mapperCacheToDomain = mapCacheToDomain,
        mapperCloudToCache = mapCloudToCache,
        mapperCloudToDomain = mapCloudToDomain,
        mapperDomainToCloud = mapDomainToCloud,
        dispatchers = dispatchers,
        exceptionHandle = exceptionHandle
    )

    @Provides
    fun provideCacheSource(
        appDao: AppRoomDao,
        mapCacheToDomain: MapCacheToDomain,
        mapCloudToCache: MapCloudToCache,
        mapCloudToDomain: MapCloudToDomain,
        dispatchers: ToDispatch,
        exceptionHandle: ExceptionHandle,
    ): CacheSource = CacheSource.Base(
        appDao = appDao,
        mapperCacheToDomain = mapCacheToDomain,
        mapperCloudToCache = mapCloudToCache,
        mapperCloudToDomain = mapCloudToDomain,
        dispatchers = dispatchers,
        exceptionHandle = exceptionHandle
    )

    @Provides
    @Singleton
    fun provideRepository(
        cloudSource: CloudSource,
        cacheSource: CacheSource,
        exceptionHandle: ExceptionHandle,
        dispatchers: ToDispatch,
    ): Repository = RepositoryImpl(
        cloudSource = cloudSource,
        cacheSource = cacheSource,
        exceptionHandle = exceptionHandle,
        dispatchers = dispatchers
    )
}