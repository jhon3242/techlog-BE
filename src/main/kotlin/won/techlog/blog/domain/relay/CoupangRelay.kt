package won.techlog.blog.domain.relay

import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.crawler.MediumBlogCrawler

@Component
class CoupangRelay : Relayable {
    companion object {
        private const val SLUG = "coupang-engineering"
    }

    override fun getBlogs(): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        (2018..2025).forEach { year ->
            result.addAll(MediumBlogCrawler.fetchBlog(SLUG, year))
        }
        return result
    }

    override fun getBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return blogType == BlogType.COUPANG
    }
}
