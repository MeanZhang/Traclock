package com.mean.traclock.utils

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Message
import co.touchlab.kermit.MessageStringFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.Tag
import co.touchlab.kermit.platformLogWriter
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import java.nio.charset.Charset

@OptIn(FormatStringsInDatetimeFormats::class)
actual fun initLogger() {
    val severity = Severity.Debug
    Logger.setLogWriters(
        platformLogWriter(
            object : MessageStringFormatter {
                override fun formatMessage(
                    severity: Severity?,
                    tag: Tag?,
                    message: Message,
                ): String {
                    return "[${
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            .format(
                                LocalDateTime.Format {
                                    byUnicodePattern("yyyy-MM-dd HH:mm:ss[.SSS]")
                                },
                            )
                    }][${severity?.name}][${tag?.tag}]${message.message}".toGBK()
                }
            },
        ),
    )
    Logger.setMinSeverity(severity)
    Logger.setTag("DIARY")
    Logger.i { "Logger initialized, severity: ${severity.name}." }
}

private fun String.toGBK(): String {
    return this.toByteArray(Charsets.UTF_8).toString(Charset.forName("GBK"))
}
