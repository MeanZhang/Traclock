package com.mean.traclock.common

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

fun Context.getString(stringRes: StringResource): String {
    return StringDesc.Resource(stringRes).toString(this)
}
