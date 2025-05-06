package won.techlog.blog.domain

import org.springframework.stereotype.Service

@Service
class BlogService(
    private val blogParserManager: BlogParserManager,
    private val blogRecommendationDao: BlogRecommendationDao
) {
    fun parseBlog(url: String): Blog = blogParserManager.parseBlog(url)

    fun parseBlogs(url: String): List<Blog> = blogParserManager.parseBlogs(url)

    fun saveBlogRecommendation(url: String) = blogRecommendationDao.save(url)

    fun findAllBlogRecommendations(): List<BlogRecommendation> = blogRecommendationDao.findAll()

    fun deleteBlogRecommendation(id: Long) = blogRecommendationDao.delete(id)
}
