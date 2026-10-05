package com.mean.traclock.resources

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

fun getString(stringRes: StringResource): String {
    return StringDesc.Resource(stringRes).localized()
}
