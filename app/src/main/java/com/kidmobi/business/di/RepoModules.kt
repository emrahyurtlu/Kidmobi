package com.kidmobi.business.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.business.repositories.ManagedDeviceRepo
import com.kidmobi.business.repositories.MobileDeviceRepo
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
    fun provideUserMobileDeviceRepo(db: FirebaseFirestore, auth: FirebaseAuth, mobileDeviceRepo: MobileDeviceRepo) = ManagedDeviceRepo(db, auth, mobileDeviceRepo)
}