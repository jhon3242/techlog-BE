package won.techlog.poster.domain

import org.springframework.data.repository.CrudRepository

interface PosterTagRepository : CrudRepository<PosterTag, Long> {
    fun findByPosterAndIsDeletedFalse(poster: Poster): Set<PosterTag>

    fun findByPoster_IdAndTag_IdAndIsDeletedFalse(
        posterId: Long,
        tagId: Long
    ): PosterTag?

    fun findAllByTag_IdAndIsDeletedFalse(tagId: Long): Set<PosterTag>
}
