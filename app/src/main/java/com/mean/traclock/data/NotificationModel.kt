package com.mean.traclock.data

/** 通知相关数据的模型 */
internal class NotificationModel {

    /**
     * @return 计时器通知的 ID
     */
    val timerNotificationId: Int
        get() = Int.MAX_VALUE - 1
}
