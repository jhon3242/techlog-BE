package won.techlog.poster.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PosterRepository: JpaRepository<Poster, Long> {

    fun findByBlogMetaData_Url(url: String): Poster?
}
