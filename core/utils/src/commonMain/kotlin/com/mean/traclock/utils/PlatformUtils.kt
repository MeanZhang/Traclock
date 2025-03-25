package com.mean.traclock.utils

import dev.icerock.moko.resources.StringResource

expect fun toast(message: String)

expect fun openUrl(url: String)

expect fun getString(stringRes: StringResource): String
