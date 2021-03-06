package com.kidmobi.di

import com.kidmobi.data.model.ManagedDevice
import com.kidmobi.data.model.MobileDevice
import com.kidmobi.data.model.MobileDeviceInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestModelModules {
    @Provides
    @Named("test_e_md")
    fun provideMobileDevice() = MobileDevice()

    @Provides
    @Named("test_e_mdi")
    fun provideMobileDeviceInfo() = MobileDeviceInfo()

    @Provides
    @Named("test_e_mand")
    fun provideUserMobileDevice() = ManagedDevice()
}