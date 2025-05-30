package won.techlog.blog.domain.client

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.Poster
import won.techlog.poster.domain.PosterDao

@Service
class BlogApiManager(
    private val clients: List<FetchClient>,
    private val posterDao: PosterDao
) {
    private val clientMap: Map<BlogType, FetchClient> = clients.associateBy { it.supportType() }
    private val logger = KotlinLogging.logger {}

    @Transactional
    suspend fun fetchBlogs(blogType: BlogType) {
        val fetchClient = getClient(blogType)
        val result =
            fetchClient.fetchBlogs()
                .map { createPoster(blogType, it) }
        posterDao.savePosters(result)
        logger.info { "⭐️블로그 ${result.size}개 저장" }
    }

    suspend fun fetchBlog(url: String) {
        val blogType = BlogType.getByUrl(url)
        val fetchClient = getClient(blogType)
        val result =
            fetchClient.fetchBlog(url)
                .let { createPoster(blogType, it) }
        posterDao.save(result)
    }

    private fun createPoster(
        blogType: BlogType,
        it: BlogMetaData
    ): Poster {
        if (blogType == BlogType.LINE_OLD) {
            return Poster(blogType = BlogType.LINE, blogMetaData = it)
        }
        return Poster(blogType = blogType, blogMetaData = it)
    }

    private fun getClient(blogType: BlogType): FetchClient {
        val fetchClient =
            clientMap[blogType]
                ?: throw IllegalArgumentException("지원하지 않은 블로그입니다.")
        return fetchClient
    }

    fun canHandle(blogType: BlogType): Boolean {
        return clientMap.containsKey(blogType)
    }
}
