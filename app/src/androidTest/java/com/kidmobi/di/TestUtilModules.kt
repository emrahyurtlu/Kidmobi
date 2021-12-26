package com.kidmobi.di

import android.content.Context
import com.kidmobi.business.utils.misc.SettingsUtil
import com.kidmobi.business.utils.misc.SharedPrefsUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestUtilModules {

    @Provides
    @Named("test_u_su")
    fun provideSettingsUtil(@ApplicationContext context: Context) = SettingsUtil(context)

    @Provides
    @Named("test_u_spu")
    fun provideSharedPrefsUtil(@ApplicationContext context: Context) = SharedPrefsUtil(context)
}