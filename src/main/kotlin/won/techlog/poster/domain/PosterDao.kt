package won.techlog.poster.domain

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import won.techlog.blog.domain.BlogType
import won.techlog.poster.exception.NotFoundException

@Component
class PosterDao(
    private val posterRepository: PosterRepository
) {
    @Transactional
    fun savePoster(poster: Poster): Poster {
        val alreadySavedBlog = posterRepository.findByBlogMetaData_Url(poster.blogMetaData.url)
        if (alreadySavedBlog != null) {
            alreadySavedBlog.isDeleted = false
            return alreadySavedBlog
        }
        return posterRepository.save(poster)
    }

    @Transactional
    fun savePosters(posters: List<Poster>): List<Poster> = posters.map { savePoster(it) }

    @Transactional(readOnly = true)
    fun getPoster(id: Long): Poster =
        posterRepository.findByIdAndIsDeletedFalse(id)
            ?: throw NotFoundException()

    @Transactional(readOnly = true)
    fun getAllPosters(): List<Poster> = posterRepository.findAllByIsDeletedFalse()

    @Transactional(readOnly = true)
    fun getAllPostersByCursor(cursor: Long?): List<Poster> = posterRepository.findTop21ByCursor(cursor = cursor)

    @Transactional(readOnly = true)
    fun getPosters(
        page: Int,
        size: Int
    ): List<Poster> {
        val pageable = PageRequest.of(page, size)
        return posterRepository.findAll(pageable).content
    }

    @Transactional(readOnly = true)
    fun searchTop21Posters(
        keyword: String? = null,
        blogType: BlogType? = null,
        cursor: Long? = null
    ): List<Poster> {
        return posterRepository.findTop21ByCursor(keyword, blogType, cursor)
    }

    @Transactional
    fun deletePoster(id: Long) = posterRepository.deleteById(id)

    @Transactional
    fun recommend(id: Long) {
        val poster = getPoster(id)
        poster.recommendations++
    }

    @Transactional
    fun cancelRecommend(id: Long) {
        val poster = getPoster(id)
        poster.recommendations--
    }

    @Transactional
    fun increaseView(id: Long) {
        val poster = getPoster(id)
        poster.views++
    }
}
