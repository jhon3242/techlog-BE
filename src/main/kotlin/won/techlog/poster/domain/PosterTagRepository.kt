package won.techlog.poster.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import won.techlog.blog.domain.BlogType
import java.time.OffsetDateTime

interface PosterTagRepository : JpaRepository<PosterTag, Long> {
    fun findByPoster(poster: Poster): Set<PosterTag>

//    fun findAllByTag_NameIn(name: List<String>): List<PosterTag>

    @Query(
        """
        SELECT pt FROM PosterTag pt
        JOIN pt.poster p
        JOIN pt.tag t
        WHERE (:cursor IS NULL OR p.blogMetaData.publishedAt < :cursor)
            AND(:blogType IS NULL OR p.blogType = :blogType)
            AND(:tagNames IS NULL OR t.name IN :tagNames)
            AND (:keyword IS NULL
                OR p.blogMetaData.title LIKE %:keyword%
                OR p.blogMetaData.content LIKE %:keyword%
                OR t.name LIKE %:keyword%
            )
            ORDER BY p.blogMetaData.publishedAt DESC
    """
    )
    fun searchTop21(
        keyword: String? = null,
        blogType: BlogType? = null,
        tagNames: List<String>? = null,
        cursor: OffsetDateTime? = null
    ): List<PosterTag>
}
