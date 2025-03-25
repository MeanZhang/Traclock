package com.mean.traclock.backup.model

enum class RestoreState {
    /** 恢复中 */
    RESTORING,

    /** 恢复成功 */
    SUCCESS,

    /** 恢复失败 */
    FAILED,

    /** 无数据 */
    NO_DATA,
}
