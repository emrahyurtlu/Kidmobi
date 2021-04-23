package com.kidmobi.assets.di

import com.kidmobi.mvvm.model.ManagedDevice
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ModelModules {
    @Provides
    fun provideMobileDevice() = MobileDevice()

    @Provides
    fun provideMobileDeviceInfo() = MobileDeviceInfo()

    @Provides
    fun provideUserMobileDevice() = ManagedDevice()
}