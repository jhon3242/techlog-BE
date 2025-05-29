package won.techlog.blog.domain.relay

import org.springframework.stereotype.Service
import won.techlog.blog.domain.BlogType
import java.lang.IllegalArgumentException

@Service
class RelayService(
    private val remoteServerManager: RemoteServerManager,
    private val relayList: List<Relayable>
) {
    fun relayByBlogName(name: String) {
        val findBlogType = BlogType.getByName(name)
        val relay = getRelay(findBlogType)
        val blogs = relay.getBlogs()
        blogs.chunked(10).forEach { chunk ->
            remoteServerManager.saveBlogs(chunk, findBlogType)
        }
    }

    fun relayByUrl(url: String) {
        val findBlogType = BlogType.getByUrl(url)
        val relay = getRelay(findBlogType)
        relay.getBlog(url)
            .let { remoteServerManager.saveBlog(it, findBlogType) }
    }

    private fun getRelay(findBlogType: BlogType): Relayable =
        (
            relayList.find { it.isSupportType(findBlogType) }
                ?: throw IllegalArgumentException("지원하지 않는 블로그입니다.")
        )
}
