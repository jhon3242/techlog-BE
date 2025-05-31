package won.techlog.blog.domain.relay

import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.crawler.MediumBlogCrawler

@Component
class YeogiRelay : Relayable {
    companion object {
        private const val URL = "https://techblog.gccompany.co.kr"
        private val yearList = listOf(2022, 2023, 2024, 2025)
    }

    override fun getBlogs(): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        yearList.forEach { year ->
            result.addAll(MediumBlogCrawler.fetchBlog(null, year, URL))
        }
        return result
    }

    override fun getBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return BlogType.YEOGI == blogType
    }
}
