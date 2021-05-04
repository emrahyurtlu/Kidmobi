package com.kidmobi.di

import android.content.Context
import com.kidmobi.business.utils.SettingsUtil
import com.kidmobi.business.utils.SharedPrefsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TestUtilModules {

    @Provides
    fun provideSettingsUtil(@ApplicationContext context: Context) = SettingsUtil(context)

    @Provides
    fun provideSharedPrefsUtil(@ApplicationContext context: Context) = SharedPrefsUtil(context)
}