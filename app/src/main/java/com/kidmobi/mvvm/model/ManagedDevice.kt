package com.kidmobi.mvvm.model

import java.io.Serializable

data class ManagedDevice constructor(
    var devices: MutableList<String> = mutableListOf()
) : Serializable, BaseModel