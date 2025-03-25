package com.mean.traclock.backup.model

internal enum class RestoreError {
    COLUMN_ERROR,
    RECORD_ID_ERROR,
    PROJECT_ID_ERROR,
    START_TIME_ERROR,
    END_TIME_ERROR,
    COLOR_ERROR,
    INSERT_ERROR,
    NO_ERROR,
    PROJECT_NAME_EMPTY,
}
