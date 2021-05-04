package com.kidmobi.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TestFirebaseModules {

    @Provides
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()
}