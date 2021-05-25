package com.kidmobi.mvvm.model

import com.google.common.truth.Truth.assertThat
import com.kidmobi.business.utils.extensions.modelExtensions.isInvalid
import com.kidmobi.business.utils.extensions.modelExtensions.isNull
import com.kidmobi.business.utils.extensions.modelExtensions.isValid
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class DeviceSessionNullObjectTest : TestCase() {
    lateinit var session: DeviceSession

    @Before
    public override fun setUp() {
        super.setUp()
        session = DeviceSession()
    }

    @Test
    fun testObjectIsNull() {
        val result = session.isNull()
        assertThat(result).isTrue()
    }

    @Test
    fun testObjectIsValid() {
        val result = session.isValid()
        assertThat(result).isFalse()
    }

    @Test
    fun testObjectIsInvalidWithNullObject() {
        val result = session.isInvalid()
        assertThat(result).isTrue()
    }
}