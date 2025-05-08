package won.techlog.blog.domain

import org.springframework.stereotype.Service
import won.techlog.blog.domain.parser.BlogParserManager
import won.techlog.blog.domain.recommendation.BlogRecommendation
import won.techlog.blog.domain.recommendation.BlogRecommendationDao

@Service
class BlogService(
    private val blogParserManager: BlogParserManager,
    private val blogRecommendationDao: BlogRecommendationDao
) {
    fun parseBlog(url: String): Blog = blogParserManager.parseBlog(url)

    fun parseBlogs(url: String): List<Blog> = blogParserManager.parseBlogs(url)

    suspend fun parseBlogsAsync(url: String): List<Blog> = blogParserManager.parseBlogsAsync(url)

    fun saveBlogRecommendation(url: String) = blogRecommendationDao.save(url)

    fun findAllBlogRecommendations(): List<BlogRecommendation> = blogRecommendationDao.findAll()

    fun deleteBlogRecommendation(id: Long) = blogRecommendationDao.delete(id)
}
