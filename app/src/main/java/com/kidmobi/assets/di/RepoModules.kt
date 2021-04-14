package com.kidmobi.assets.di

import com.kidmobi.assets.repositories.MobileDeviceRepo
import com.kidmobi.assets.repositories.UserMobileDeviceRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
object RepoModules {

    @Provides
    fun provideMobileDeviceRepo() = MobileDeviceRepo()

    @Provides
    fun provideUserMobileDeviceRepo() = UserMobileDeviceRepo()
}