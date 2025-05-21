package won.techlog.common

import java.time.OffsetDateTime
import java.time.ZoneOffset

object TimeProvider {
    fun now(): OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
}
