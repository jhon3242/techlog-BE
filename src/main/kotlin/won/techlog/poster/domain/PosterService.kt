package won.techlog.poster.domain

import org.springframework.stereotype.Service
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogParserManager

@Service
class PosterService(
    private val posterDao: PosterDao
) {
    fun createPoster(poster: Poster): Poster
        = posterDao.savePoster(poster)

    fun getPoster(id: Long): Poster
        = posterDao.getPoster(id)

    fun getPosters(): List<Poster>
        = posterDao.getAllPosters()

    fun deletePoster(id: Long)
        = posterDao.deletePoster(id)
}
