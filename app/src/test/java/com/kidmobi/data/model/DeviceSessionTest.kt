package com.kidmobi.data.model

import com.google.common.truth.Truth.assertThat
import com.kidmobi.business.utils.extensions.modelExtensions.isInvalid
import com.kidmobi.business.utils.extensions.modelExtensions.isNull
import com.kidmobi.business.utils.extensions.modelExtensions.isValid
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import java.util.*

class DeviceSessionTest : TestCase() {
    lateinit var session: DeviceSession

    @Before
    public override fun setUp() {
        super.setUp()
        val calendar = Calendar.getInstance()
        session = DeviceSession("q", "o", calendar.time)
        calendar.add(Calendar.MINUTE, 60)
        session.sessionEnd = calendar.time
    }

    @Test
    fun testObjectIsNull() {
        val result = session.isNull()
        assertThat(result).isFalse()
    }

    @Test
    fun testObjectIsValid() {
        val result = session.isValid()
        assertThat(result).isTrue()
    }

    @Test
    fun testObjectIsInvalid() {
        val result = session.isInvalid()
        assertThat(result).isFalse()
    }
}