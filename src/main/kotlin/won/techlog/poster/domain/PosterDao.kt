package won.techlog.poster.domain

import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.poster.exception.NotFoundException
import kotlin.jvm.optionals.getOrElse

@Component
class PosterDao(
    private val posterRepository: PosterRepository,
) {
    fun savePoster(poster: Poster): Poster {
        return posterRepository.save(poster)
    }

    fun getPoster(id: Long): Poster
    = posterRepository.findById(id)
        .getOrElse { throw NotFoundException() }

    fun getAllPosters(): List<Poster>
    = posterRepository.findAll()

    fun deletePoster(id: Long) = posterRepository.deleteById(id)
}
