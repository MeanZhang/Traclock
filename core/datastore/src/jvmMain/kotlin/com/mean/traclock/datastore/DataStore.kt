package com.mean.traclock.datastore

import okio.Path.Companion.toPath

internal fun getDataStore() =
    createDataStore {
        (System.getProperty("user.home").toPath() / ".traclock" / DATA_STORE_FILE_NAME).toString()
    }
