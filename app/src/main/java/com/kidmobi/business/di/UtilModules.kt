package com.kidmobi.business.di

import android.content.Context
import com.kidmobi.business.utils.misc.SettingsUtil
import com.kidmobi.business.utils.misc.SharedPrefsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UtilModules {

    @Provides
    fun provideSettingsUtil(@ApplicationContext context: Context) = SettingsUtil(context)

    @Provides
    fun provideSharedPrefsUtil(@ApplicationContext context: Context) = SharedPrefsUtil(context)
}