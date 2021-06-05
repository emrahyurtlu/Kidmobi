package com.kidmobi.data.model

import java.io.Serializable

data class ManagedDevice constructor(
    var devices: MutableList<String> = mutableListOf()
) : BaseModel, Serializable