package won.techlog.poster.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import won.techlog.blog.domain.BlogType
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

        val searchResult =
            posterDao.searchTop21Posters(
                keyword = request.keyword,
                tagNames = request.tags,
                blogType = BlogType.findByName(request.blogType),
                cursor = request.cursor
            )

        println("[DEBUG] searchResult.size = ${searchResult.size}")

        val subListSize = min(maxCount, searchResult.size)
        val beforeMapping = searchResult.subList(0, subListSize)

        println("[DEBUG] subList size = $subListSize")

        val contents =
            beforeMapping.mapIndexed { index, poster ->
                try {
                    val tags = posterTagDao.findTags(poster)
                    println("[DEBUG] Mapping index=$index, posterId=${poster.id}, tags.size=${tags.size}")
                    PosterResponse(poster, tags)
                } catch (e: Exception) {
                    println(
                        "[ERROR] Failed to map PosterResponse at index=$index, " +
                            "posterId=${poster.id}, error=${e.message}"
                    )
                    throw e // 혹은 continue하려면 null 처리 후 filterNotNull 필요
                }
            }

        val nextCursor = searchResult.lastOrNull()?.blogMetaData?.publishedAt.toString()
        val hasNext = searchResult.size > maxCount

        println("[DEBUG] Final contents.size = ${contents.size}, hasNext = $hasNext, nextCursor = $nextCursor")

        return PostersResponse(contents, nextCursor, hasNext, contents.size)
    }

//    @Transactional(readOnly = true)
//    fun searchPosters(request: PosterSearchRequest): PostersResponse {
//        val maxCount = 20
//        val searchResult =
//            posterDao.searchTop21Posters(
//                keyword = request.keyword,
//                tagNames = request.tags,
//                blogType = BlogType.findByName(request.blogType),
//                cursor = request.cursor
//            )
//        val contents =
//            searchResult.subList(0, min(maxCount, searchResult.size))
//                .map { PosterResponse(it, posterTagDao.findTags(it)) }
//        val nextCursor = searchResult.lastOrNull()?.blogMetaData?.publishedAt.toString()
//        val hasNext = searchResult.size > maxCount
//        return PostersResponse(contents, nextCursor, hasNext, contents.size)
//    }

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
