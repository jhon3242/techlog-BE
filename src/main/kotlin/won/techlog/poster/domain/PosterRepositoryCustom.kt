package won.techlog.poster.domain

import java.time.LocalDateTime

interface PosterRepositoryCustom {
    fun searchPosters(
        keyword: String? = null,
        tagNames: List<String>? = null,
        blogType: String? = null,
        cursorCreatedAt: LocalDateTime?,
        cursorId: Long?,
        size: Int
    ): List<Poster>
}
