package com.kidmobi.business.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.kidmobi.mvvm.model.DeviceSession
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class DeviceSessionRepoTest {

    @Inject
    @Named("test_r_ds")
    lateinit var repo: DeviceSessionRepo

    var calendar: Calendar = Calendar.getInstance()

    var session = DeviceSession("test", "test", calendar.time, calendar.time)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test() {
        val r = 5
        Truth.assertThat(r).isEqualTo(5)
    }

    @Test
    fun add() = runBlockingTest {
        repo.add(session)
        Truth.assertThat(repo.getList()).contains(session)
    }

    @Test
    fun getById() {

    }

    @Test
    fun getList() = runBlockingTest {
        val list = repo.getList()
        Truth.assertThat(list).isNotEmpty()
        Truth.assertThat(list).contains(session)
    }

    @Test
    fun update() {
    }

    @Test
    fun remove() {
    }

    @Test
    fun docExists() {
    }

    @Test
    fun getByOpenSession() {
    }
}