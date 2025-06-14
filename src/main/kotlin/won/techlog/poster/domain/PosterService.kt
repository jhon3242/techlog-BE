package won.techlog.poster.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import won.techlog.blog.domain.BlogType
import won.techlog.common.TimeProvider
import won.techlog.poster.api.request.PosterSearchRequest
import won.techlog.poster.api.request.PosterUpdateRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.api.response.PostersResponse
import won.techlog.tag.domain.Tag
import won.techlog.tag.domain.TagDao
import kotlin.math.min

@Service
@Transactional
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
        val savePoster = posterDao.save(poster)
        posterTagDao.save(savePoster, tags)
        return PosterResponse(savePoster, tags)
    }

    @Transactional(readOnly = true)
    fun getPoster(id: Long): PosterResponse {
        val poster = posterDao.getPoster(id)
        val tags = posterTagDao.findTags(poster)
        return PosterResponse(poster, tags)
    }

    @Transactional(readOnly = true)
    fun searchPosters(request: PosterSearchRequest): PostersResponse {
        val maxCount = 20
        val searchResult = mutableListOf<Poster>()
        val findBlogType = BlogType.findByName(request.blogType)
        searchResult.addAll(
            posterDao.searchTop21Posters(
                keyword = request.keyword,
                tagNames = request.tags,
                blogType = findBlogType,
                cursor = request.cursor?.let { TimeProvider.parseByString(it) }
            )
        )
        searchResult.addAll(getTagSearchResult(request, findBlogType))
        val contents =
            searchResult.subList(0, min(maxCount, searchResult.size))
                .map { PosterResponse(it, posterTagDao.findTags(it)) }
        val nextCursor = searchResult.lastOrNull()?.blogMetaData?.publishedAt.toString()
        val hasNext = searchResult.size > maxCount
        return PostersResponse(contents, nextCursor, hasNext, contents.size)
    }

    private fun getTagSearchResult(
        request: PosterSearchRequest,
        findBlogType: BlogType?
    ): List<Poster> {
        if (request.keyword != null) {
            val findTag = tagDao.findByName(request.keyword)
            if (findTag != null) {
                return posterTagDao.findPosters(findTag)
                    .filter { findBlogType == null || it.blogType == findBlogType }
            }
        }
        return emptyList()
    }

    fun updatePoster(
        id: Long,
        request: PosterUpdateRequest
    ): PosterResponse {
        val poster = posterDao.getPoster(id)
        poster.update(
            title = request.title,
            thumbnailUrl = request.thumbnail,
            content = request.content,
            url = request.url,
            blogType =
                BlogType.findByName(
                    request.blogType
                )
        )
        val tags = tagDao.findAllByNames(request.tags)
        posterTagDao.save(poster, tags)
        return PosterResponse(poster, tags)
    }

    fun deletePoster(id: Long) = posterDao.deletePoster(id)

    fun like(id: Long) {
        posterDao.like(id)
    }

    fun cancelLike(id: Long) {
        posterDao.cancelLike(id)
    }

    fun increaseView(id: Long) {
        posterDao.increaseView(id)
    }
}
