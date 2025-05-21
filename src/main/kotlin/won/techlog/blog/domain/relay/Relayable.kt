package won.techlog.blog.domain.relay

import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType

interface Relayable {
    fun getBlogs(): List<BlogMetaData>

    fun getBlog(url: String): BlogMetaData

    fun isSupportType(blogType: BlogType): Boolean
}
