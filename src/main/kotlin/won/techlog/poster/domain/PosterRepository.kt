package won.techlog.poster.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import won.techlog.blog.domain.BlogType

interface PosterRepository : JpaRepository<Poster, Long> {
    fun findByIdAndIsDeletedFalse(id: Long): Poster?

    fun findByBlogMetaData_Url(url: String): Poster?

    fun findAllByIsDeletedFalse(): List<Poster>

    @Query(
        """
    SELECT p FROM Poster p
    LEFT JOIN PosterTag pt ON p.id = pt.poster.id
    LEFT JOIN Tag t ON pt.tag.id = t.id
    WHERE (:cursor IS NULL OR p.id < :cursor)
      AND (:blogType IS NULL OR p.blogType = :blogType)
      AND (:keyword IS NULL
        OR t.name = :keyword
        OR p.blogMetaData.content LIKE %:keyword%
        OR p.blogMetaData.title LIKE %:keyword%
     )
    ORDER BY p.id DESC
    LIMIT 20
"""
    )
    fun findTop20ByCursor(
        keyword: String?,
        blogType: BlogType?,
        cursor: Long?
    ): List<Poster>
}
