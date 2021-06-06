package com.kidmobi.business.di

import com.kidmobi.data.model.ManagedDevice
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.data.model.MobileDeviceInfo
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