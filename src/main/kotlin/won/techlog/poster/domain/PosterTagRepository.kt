package won.techlog.poster.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PosterTagRepository: JpaRepository<PosterTag, Long> {

    fun findByPoster(poster: Poster): Set<PosterTag>
}
