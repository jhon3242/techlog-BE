package won.techlog.poster.domain

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import won.techlog.poster.exception.NotFoundException
import kotlin.jvm.optionals.getOrElse

@Component
class PosterDao(
    private val posterRepository: PosterRepository,
) {
    @Transactional
    fun savePoster(poster: Poster): Poster {
        return posterRepository.findByBlogMetaData_Url(poster.blogMetaData.url)
            ?: posterRepository.save(poster)
    }

    @Transactional(readOnly = true)
    fun getPoster(id: Long): Poster
    = posterRepository.findById(id)
        .getOrElse { throw NotFoundException() }

    @Transactional(readOnly = true)
    fun getAllPosters(): List<Poster>
    = posterRepository.findAll()

    @Transactional(readOnly = true)
    fun getPosters(page: Int, size: Int): List<Poster> {
        val pageable = PageRequest.of(page, size)
        return posterRepository.findAll(pageable).content
    }

    @Transactional
    fun deletePoster(id: Long) = posterRepository.deleteById(id)

    @Transactional
    fun recommend(id: Long) {
        val poster = getPoster(id)
        poster.recommendations++
    }

    @Transactional
    fun increaseView(id: Long) {
        val poster = getPoster(id)
        poster.views++
    }
}
