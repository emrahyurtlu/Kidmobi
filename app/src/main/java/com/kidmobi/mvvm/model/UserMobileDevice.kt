package com.kidmobi.mvvm.model

import java.io.Serializable
import javax.inject.Inject

data class UserMobileDevice @Inject constructor(
    var devices: MutableList<String>
) : Serializable, BaseModel