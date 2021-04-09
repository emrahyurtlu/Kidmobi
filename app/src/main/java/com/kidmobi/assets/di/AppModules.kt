package com.kidmobi.assets.di

import com.kidmobi.mvvm.model.MobileDevice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    fun provideMobileDevice() = MobileDevice(
        deviceId = "",
        deviceImageUrl = 0,
        deviceOwnerName = null,
        deviceOwnerImageUrl = null,
        deviceOwnerUid = null,
        deviceOwnerEmail = null,
        info = null,
        settings = null,
        createdAt = null,
        updatedAt = null,
        userType = null
    )

    /*@ActivityScoped
    @Provides
    fun provideSettingUtil(context: Context) = SettingsUtil(
        context = context
    )*/
}