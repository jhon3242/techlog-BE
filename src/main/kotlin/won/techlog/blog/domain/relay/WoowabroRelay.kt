package won.techlog.blog.domain.relay

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.infrastructure.woowabro.WoowabroWebClient

@Component
class WoowabroRelay(
    private val woowabroWebClient: WoowabroWebClient
) : Relayable {
    override fun getBlogs(): List<BlogMetaData> {
        return runBlocking {
            woowabroWebClient.fetchBlogs()
        }
    }

    override fun getBlog(url: String): BlogMetaData {
        return runBlocking {
            woowabroWebClient.fetchBlog(url)
        }
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return blogType == BlogType.WOOWABRO
    }
}
