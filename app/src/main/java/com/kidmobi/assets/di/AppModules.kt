package com.kidmobi.assets.di

import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.MobileDeviceInfo
import com.kidmobi.mvvm.model.UserMobileDevice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    fun provideMobileDevice() = MobileDevice.init()

    @Provides
    fun provideMobileDeviceInfo() = MobileDeviceInfo.init()

    @Provides
    fun provideUserMobileDevice() = UserMobileDevice.init()
}

/*
@Module
@InstallIn(ActivityComponent::class)
object ViewModelModules {
    @Provides
    @ViewModelScoped
    fun provideMobileDeviceViewModel() = MobileDeviceViewModel()
}*/
