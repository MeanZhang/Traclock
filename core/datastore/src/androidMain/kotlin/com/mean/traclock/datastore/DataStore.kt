package com.mean.traclock.datastore

import android.content.Context

internal fun getDataStore(context: Context) =
    createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
