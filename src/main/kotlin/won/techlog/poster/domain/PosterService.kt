package won.techlog.poster.domain

import org.springframework.stereotype.Service
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogParserManager

@Service
class PosterService(
    private val posterDao: PosterDao
) {
    fun createPoster(blogMetaData: BlogMetaData): Poster
        = posterDao.savePoster(blogMetaData)

    fun getPoster(id: Long): Poster
        = posterDao.getPoster(id)

    fun getPosters(): List<Poster>
        = posterDao.getAllPosters()

    fun deletePoster(id: Long)
        = posterDao.deletePoster(id)
}
