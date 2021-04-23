package com.kidmobi.assets.di

import com.kidmobi.assets.utils.extensions.modelExtensions.init
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import com.kidmobi.mvvm.model.UserMobileDevice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ModelModules {
    @Provides
    fun provideMobileDevice() = MobileDevice().init()

    @Provides
    fun provideMobileDeviceInfo() = MobileDeviceInfo().init()

    @Provides
    fun provideUserMobileDevice() = UserMobileDevice().init()
}