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
        type: BlogType = BlogType.WOOWABRO
    ): OffsetDateTime {
        if (type == BlogType.KAKAO) {
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
            return OffsetDateTime.of(
                java.time.LocalDateTime.parse(dateTimeStr, formatter),
                ZoneOffset.UTC
            )
        }
        if (type == BlogType.KAKAO_PAY) {
            val formatter = DateTimeFormatter.ofPattern("yyyy. M. d")
            val localDate = LocalDate.parse(dateTimeStr, formatter)
            return OffsetDateTime.of(localDate.atStartOfDay(), ZoneOffset.UTC)
        }
        return OffsetDateTime.parse(dateTimeStr)
    }

    fun parseByLong(dateTimeLon: Long): OffsetDateTime = Instant.ofEpochMilli(dateTimeLon).atOffset(ZoneOffset.UTC)
}
