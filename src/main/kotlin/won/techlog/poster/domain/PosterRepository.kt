package won.techlog.poster.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PosterRepository : JpaRepository<Poster, Long>, PosterRepositoryCustom {
    fun findByIdAndIsDeletedFalse(id: Long): Poster?

    fun findByBlogMetaData_Url(url: String): Poster?

    fun findAllByIsDeletedFalse(): List<Poster>
}
