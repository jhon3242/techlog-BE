package won.techlog.blog.domain.relay

import org.springframework.stereotype.Service
import won.techlog.blog.domain.BlogType
import java.lang.IllegalArgumentException

@Service
class RelayService(
    private val remoteServerManager: RemoteServerManager,
    private val relayList: List<Relayable>
) {
    fun relay(name: String) {
        val findBlogType = BlogType.getByName(name)
        val relay =
            relayList.find { it.isSupportType(findBlogType) }
                ?: throw IllegalArgumentException("지원하지 않는 블로그입니다.")
        val blogs = relay.getBlogs()
        blogs.chunked(10).forEach { chunk ->
            remoteServerManager.saveBlogs(chunk, findBlogType)
        }
    }
}
