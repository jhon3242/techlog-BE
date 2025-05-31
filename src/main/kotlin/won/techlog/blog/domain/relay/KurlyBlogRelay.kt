package won.techlog.blog.domain.relay

import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.crawler.company.KurlyBlogCrawler

@Component
class KurlyBlogRelay(
    private val kurlyBlogCrawler: KurlyBlogCrawler
) : Relayable {
    override fun getBlogs(): List<BlogMetaData> {
        return kurlyBlogCrawler.crawlBlogs()
    }

    override fun getBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return BlogType.KURLY == blogType
    }
}
