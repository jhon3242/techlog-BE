package won.techlog.poster.domain

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import won.techlog.blog.domain.BlogType
import java.time.OffsetDateTime

interface PosterRepository : CrudRepository<Poster, Long> {
    fun findByIdAndIsDeletedFalse(id: Long): Poster?

    fun findByBlogMetaData_Url(url: String): Poster?

    fun findAllByIsDeletedFalse(): List<Poster>

    @Query(
        """
    SELECT p FROM Poster p
    LEFT JOIN PosterTag pt ON p.id = pt.poster.id
    LEFT JOIN Tag t ON pt.tag.id = t.id
    WHERE (:blogType IS NULL OR p.blogType = :blogType)
        AND (:keyword IS NULL
            OR p.blogMetaData.content LIKE %:keyword%
            OR p.blogMetaData.title LIKE %:keyword%
        )
    AND (:tagNames IS NULL OR t.name IN :tagNames)
    AND (:cursor IS NULL OR p.blogMetaData.publishedAt < :cursor)
    AND (p.isDeleted = false)
    GROUP BY p
    ORDER BY p.blogMetaData.publishedAt DESC
    LIMIT 21
    """
    )
    fun findTop21ByPublishedAtCursor(
        keyword: String? = null,
        tagNames: List<String>? = null,
        blogType: BlogType? = null,
        cursor: OffsetDateTime? = null
    ): List<Poster>
}
