package won.techlog.common

import won.techlog.blog.domain.BlogType
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeProvider {
    fun now(): OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)

    fun parseByString(
        dateTimeStr: String,
    ): OffsetDateTime {
        if (dateTimeStr.matches(Regex("""\d{4}\.\d{2}\.\d{2} \d{2}:\d{2}:\d{2}"""))) {
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
            return OffsetDateTime.of(
                java.time.LocalDateTime.parse(dateTimeStr, formatter),
                ZoneOffset.UTC
            )
        }
        if (dateTimeStr.matches(Regex("""\d{4}\.\s?\d{1,2}\.\s?\d{1,2}"""))) {
            val formatter = DateTimeFormatter.ofPattern("yyyy. M. d")
            val localDate = LocalDate.parse(dateTimeStr, formatter)
            return OffsetDateTime.of(localDate.atStartOfDay(), ZoneOffset.UTC)
        }
        if (dateTimeStr.matches(Regex("""\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}[+-]\d{4}"""))) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            return OffsetDateTime.parse(dateTimeStr, formatter)
        }

        return OffsetDateTime.parse(dateTimeStr)
    }

    fun parseByLong(dateTimeLon: Long): OffsetDateTime = Instant.ofEpochMilli(dateTimeLon).atOffset(ZoneOffset.UTC)
}
