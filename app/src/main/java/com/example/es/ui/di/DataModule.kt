package com.example.es.ui.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import com.example.es.data.model.MapCacheToDomain
import com.example.es.data.model.MapCloudToCache
import com.example.es.data.model.MapCloudToDomain
import com.example.es.data.model.MapDomainToCloud
import com.example.es.data.repository.CacheSource
import com.example.es.data.repository.CloudSource
import com.example.es.data.repository.ExceptionHandle
import com.example.es.data.repository.RepositoryImpl
import com.example.es.data.repository.ToDispatch
import com.example.es.data.room.AppRoomDao
import com.example.es.data.room.AppRoomDatabase
import com.example.es.domain.Repository
import com.example.es.utils.ApiCheck
import com.example.es.utils.DateTimeFormat
import com.example.es.utils.ResourceProvider
import com.example.es.utils.connectivity.NetworkProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider.Base =
        ResourceProvider.Base(context)

    @Provides
    @Singleton
    fun provideDateTimeFormat(): DateTimeFormat.Base = DateTimeFormat.Base()

    @Provides
    @Singleton
    fun provideGeoCoder(@ApplicationContext context: Context): Geocoder =
        Geocoder(context, Locale.getDefault())

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    @Singleton
    fun provideGoogleLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideGoogleSettingsLocationClient(@ApplicationContext context: Context): SettingsClient =
        LocationServices.getSettingsClient(context)

    @Provides
    @Singleton
    fun provideHuaweiLocationClient(@ApplicationContext context: Context): com.huawei.hms.location.FusedLocationProviderClient =
        com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideHuaweiSettingsClient(@ApplicationContext context: Context): com.huawei.hms.location.SettingsClient =
        com.huawei.hms.location.LocationServices.getSettingsClient(context)

    @Provides
    @Singleton
    fun provideApiCheck(@ApplicationContext context: Context): ApiCheck.Base =
        ApiCheck.Base(context)

    @Provides
    @Singleton
    fun provideNetworkProvider(@ApplicationContext context: Context) =
        NetworkProvider(context)

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context): AppRoomDao =
        AppRoomDatabase.getInstance(context).getAppRoomDao()

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
        dispatchers: ToDispatch,
        exceptionHandle: ExceptionHandle,
    ): CloudSource = CloudSource.Base(
        appDao = appDao,
        mapperCacheToDomain = mapCacheToDomain,
        mapperCloudToCache = mapCloudToCache,
        mapperCloudToDomain = mapCloudToDomain,
        dispatchers = dispatchers,
        exceptionHandle = exceptionHandle
    )

    @Provides
    fun provideCacheSource(
        appDao: AppRoomDao,
        mapCacheToDomain: MapCacheToDomain,
        exceptionHandle: ExceptionHandle,
    ): CacheSource = CacheSource.Base(
        appDao = appDao,
        mapperCacheToDomain = mapCacheToDomain,
        exceptionHandle = exceptionHandle
    )

    @Provides
    @Singleton
    fun provideRepository(
        cloudSource: CloudSource,
        cacheSource: CacheSource,
        exceptionHandle: ExceptionHandle,
    ): Repository = RepositoryImpl(
        cloudSource = cloudSource,
        cacheSource = cacheSource,
        exceptionHandle = exceptionHandle,
    )
}