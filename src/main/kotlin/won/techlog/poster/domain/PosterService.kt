package won.techlog.poster.domain

import org.springframework.stereotype.Service
import won.techlog.poster.api.response.PosterResponse
import won.techlog.tag.domain.Tag
import won.techlog.tag.domain.TagDao

@Service
class PosterService(
    private val posterDao: PosterDao,
    private val tagDao: TagDao,
    private val posterTagDao: PosterTagDao
) {
    fun createPoster(poster: Poster, names: List<String>): PosterResponse {
        val tags: List<Tag> = tagDao.findAllByNames(names)
        val savePoster = posterDao.savePoster(poster)
        posterTagDao.save(savePoster, tags)
        return PosterResponse(savePoster, tags)
    }

    fun getPoster(id: Long): PosterResponse {
        val poster = posterDao.getPoster(id)
        val tags = posterTagDao.findTags(poster)
        return PosterResponse(poster, tags)
    }

    fun getPosters(
        page: Int,
        size: Int
    ): List<PosterResponse> {
        val posters = posterDao.getPosters(page, size)
        return posters.map { PosterResponse(it, posterTagDao.findTags(it)) }
    }

    fun deletePoster(id: Long) = posterDao.deletePoster(id)

    fun recommend(id: Long) {
        posterDao.recommend(id)
    }

    fun increaseView(id: Long) {
        posterDao.increaseView(id)
    }
}
