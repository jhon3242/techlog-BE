package won.techlog.poster.domain

import org.springframework.stereotype.Service
import won.techlog.blog.domain.BlogType
import won.techlog.poster.api.request.PosterSearchRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.api.response.PostersResponse
import won.techlog.tag.domain.Tag
import won.techlog.tag.domain.TagDao
import kotlin.math.min

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

    fun searchPosters(request: PosterSearchRequest): PostersResponse {
        val maxCount = 20
        val searchResult =
            posterDao.searchTop21Posters(
                keyword = request.keyword,
                blogType = BlogType.findByName(request.blogType),
                cursor = request.cursor
            )
        val contents =
            searchResult.subList(0, min(maxCount, searchResult.size))
                .map { PosterResponse(it, posterTagDao.findTags(it)) }
        val nextCursor = searchResult.lastOrNull()?.id
        val hasNext = searchResult.size > maxCount
        return PostersResponse(contents, nextCursor, hasNext)
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
