package com.kidmobi.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.kidmobi.data.model.DeviceSession
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class DeviceSessionRepoTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)


    @Inject
    lateinit var repo: DeviceSessionRepo

    private var calendar: Calendar = Calendar.getInstance()

    private var session = DeviceSession("test", "test", calendar.time, calendar.time)

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun add() {
        testScope.launch {
            repo.add(session)
            Truth.assertThat(repo.getList()).contains(session)
        }
    }

    @Test
    fun getById() {
        testScope.launch {
            val entity = repo.getById("02duq2y8igZ6Kvkaku2u")
            Truth.assertThat(entity).isNotSameInstanceAs(session)
        }
    }

    @Test
    fun getList() {
        testScope.launch {
            val list = repo.getList()
            Truth.assertThat(list).isNotEmpty()
            Truth.assertThat(list).contains(session)
            this.coroutineContext.job.join()
        }
    }

    @Test
    fun update() {
        testScope.launch {
            session.sessionCreatorDeviceId = "testt"
            session.sessionOwnerDeviceId = "testt"
            repo.update("02duq2y8igZ6Kvkaku2u", session)
        }
    }

    @Test
    fun remove() {
    }

    @Test
    fun docExists() {
        testScope.launch {
            val result = repo.docExists("02duq2y8igZ6Kvkaku2u")
            Truth.assertThat(result).isTrue()
        }
    }

    @Test
    fun getByOpenSession() {
    }
}