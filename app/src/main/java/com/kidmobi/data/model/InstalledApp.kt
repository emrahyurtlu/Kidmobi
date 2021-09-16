package com.kidmobi.data.model

import java.io.Serializable

data class InstalledApp(
    var appName: String = "",
    var packageName: String = "",
) : BaseModel, Serializable
