package won.techlog.poster.domain

import org.springframework.stereotype.Service
import won.techlog.poster.api.request.PosterSearchRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.api.response.PostersResponse
import won.techlog.tag.domain.Tag
import won.techlog.tag.domain.TagDao

@Service
class PosterService(
    private val posterDao: PosterDao,
    private val tagDao: TagDao,
    private val posterTagDao: PosterTagDao
) {
    fun createPoster(
        poster: Poster,
        names: List<String>
    ): PosterResponse {
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
    ): PostersResponse {
        val posters = posterDao.getPosters(page, size)
        return posters.map { PosterResponse(it, posterTagDao.findTags(it)) }
            .let { PostersResponse(it) }
    }

    fun searchPosters(request: PosterSearchRequest): List<PosterResponse> {
        val posters =
            posterDao.searchPosters(
                keyword = request.keyword,
                tagNames = request.tags,
                blogType = request.blogType
            )
        return posters.map { PosterResponse(it, posterTagDao.findTags(it)) }
    }

    fun deletePoster(id: Long) = posterDao.deletePoster(id)

    fun recommend(id: Long) {
        posterDao.recommend(id)
    }

    fun cancelRecommend(id: Long) {
        posterDao.cancelRecommend(id)
    }

    fun increaseView(id: Long) {
        posterDao.increaseView(id)
    }
}
