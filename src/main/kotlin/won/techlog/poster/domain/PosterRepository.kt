package won.techlog.poster.domain

import org.springframework.data.jpa.repository.JpaRepository

interface PosterRepository: JpaRepository<Poster, Long> {
}
