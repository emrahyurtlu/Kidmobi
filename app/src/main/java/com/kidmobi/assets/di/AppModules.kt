package com.kidmobi.assets.di

import android.content.Context
import com.kidmobi.assets.utils.SettingsUtil
import com.kidmobi.mvvm.model.MobileDevice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @ActivityScoped
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

    @Singleton
    @Provides
    fun provideSettingUtil(context: Context) = SettingsUtil(
        context = context
    )
}