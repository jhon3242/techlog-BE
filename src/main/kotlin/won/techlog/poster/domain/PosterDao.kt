package won.techlog.poster.domain

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import won.techlog.blog.domain.BlogType
import won.techlog.poster.exception.NotFoundException
import java.time.OffsetDateTime

@Component
class PosterDao(
    private val posterRepository: PosterRepository
) {
    @Transactional
    fun save(poster: Poster): Poster {
        val alreadySavedBlog = posterRepository.findByBlogMetaData_Url(poster.blogMetaData.url)
        if (alreadySavedBlog != null) {
            alreadySavedBlog.isDeleted = false
            return alreadySavedBlog
        }
        return posterRepository.save(poster)
    }

    @Transactional
    fun savePosters(posters: List<Poster>): List<Poster> = posters.map { save(it) }

    @Transactional(readOnly = true)
    fun getPoster(id: Long): Poster =
        posterRepository.findByIdAndIsDeletedFalse(id)
            ?: throw NotFoundException()

    @Transactional(readOnly = true)
    fun getAllPosters(): List<Poster> = posterRepository.findAllByIsDeletedFalse()

    @Transactional(readOnly = true)
    fun searchTop21Posters(
        keyword: String? = null,
        tagNames: List<String>? = null,
        blogType: BlogType? = null,
        cursor: OffsetDateTime? = null
    ): List<Poster> {
        return posterRepository.findTop21ByPublishedAtCursor(keyword, tagNames, blogType, cursor)
    }

    @Transactional
    fun deletePoster(id: Long) = posterRepository.deleteById(id)

    @Transactional
    fun like(id: Long) {
        val poster = getPoster(id)
        poster.likeCount++
    }

    @Transactional
    fun cancelLike(id: Long) {
        val poster = getPoster(id)
        poster.likeCount--
    }

    @Transactional
    fun increaseView(id: Long) {
        val poster = getPoster(id)
        poster.views++
    }
}
