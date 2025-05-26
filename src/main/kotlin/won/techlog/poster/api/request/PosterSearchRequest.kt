package won.techlog.poster.api.request

import java.time.OffsetDateTime

data class PosterSearchRequest(
    val keyword: String? = null,
    val tags: List<String>? = null,
    val blogType: String? = null,
    val cursor: OffsetDateTime? = null
)
