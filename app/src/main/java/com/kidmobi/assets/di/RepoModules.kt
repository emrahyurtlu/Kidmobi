package com.kidmobi.assets.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideMobileDeviceRepo(db: FirebaseFirestore) = MobileDeviceRepo(db)

    @Provides
    fun provideUserMobileDeviceRepo(db: FirebaseFirestore, auth: FirebaseAuth) = UserMobileDeviceRepo(db, auth)
}