package com.kidmobi.business.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.kidmobi.mvvm.model.DeviceSession
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
@ExperimentalCoroutinesApi
class DeviceSessionRepoTest {

    @Inject
    lateinit var repo: DeviceSessionRepo

    @Mock
    lateinit var session: DeviceSession

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var tastExecuter = InstantTaskExecutorRule()

    var calendar = Calendar.getInstance()

    @Before
    fun setUp() {
        hiltRule.inject()
        session = mock(DeviceSession::class.java)
    }

    @Test
    fun getById() {

    }

    @Test
    fun getList() {
    }

    @Test
    suspend fun add() {
        repo.add(session)
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