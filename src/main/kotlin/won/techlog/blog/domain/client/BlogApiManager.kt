package won.techlog.blog.domain.client

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.Poster
import won.techlog.poster.domain.PosterDao

@Service
class BlogApiManager(
    private val clients: List<FetchClient>,
    private val posterDao: PosterDao
) {
    private val clientMap: Map<BlogType, FetchClient> = clients.associateBy { it.supportType()}

    @Transactional
    suspend fun fetchBlogs(blogType: BlogType) {
        val fetchClient = getClient(blogType)
        val result = fetchClient.fetchBlogs()
            .map { Poster(blogType = blogType, blogMetaData = it) }
        posterDao.savePosters(result)
    }

    private fun getClient(blogType: BlogType): FetchClient {
        val fetchClient = clientMap[blogType]
            ?: throw IllegalArgumentException("지원하지 않은 블로그입니다.")
        return fetchClient
    }

    fun canHandle(url: String): Boolean {
        val blogType = BlogType.getByUrl(url)
        return clientMap.containsKey(blogType)
    }
}
