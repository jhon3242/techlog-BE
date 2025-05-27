package won.techlog.poster.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PosterTagRepository : JpaRepository<PosterTag, Long> {
    fun findByPosterAndIsDeletedFalse(poster: Poster): Set<PosterTag>

    fun findByPoster_IdAndTag_IdAndIsDeletedFalse(
        posterId: Long,
        tagId: Long
    ): PosterTag?
}
