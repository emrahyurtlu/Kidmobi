package com.kidmobi.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.business.repositories.DeviceSessionRepo
import com.kidmobi.business.repositories.ManagedDeviceRepo
import com.kidmobi.business.repositories.MobileDeviceRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
object TestRepoModules {
    @Provides
    @Named("test_r_mod")
    fun provideMobileDeviceRepo(db: FirebaseFirestore) = MobileDeviceRepo(db)

    @Provides
    @Named("test_r_md")
    fun provideManagedDeviceRepo(db: FirebaseFirestore, auth: FirebaseAuth, mobileDeviceRepo: MobileDeviceRepo) = ManagedDeviceRepo(db, auth, mobileDeviceRepo)

    @Provides
    @Named("test_r_ds")
    fun provideDeviceSessionRepo(db: FirebaseFirestore) = DeviceSessionRepo(db)
}