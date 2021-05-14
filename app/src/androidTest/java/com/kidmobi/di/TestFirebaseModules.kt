package com.kidmobi.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestFirebaseModules {
    @Provides
    @Named("test_fs")
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Named("test_fa")
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()
}