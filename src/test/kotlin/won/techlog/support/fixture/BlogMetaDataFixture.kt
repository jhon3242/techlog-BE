package won.techlog.support.fixture

import won.techlog.blog.domain.BlogMetaData
import won.techlog.common.TimeProvider

object BlogMetaDataFixture {
    fun create(
        title: String = "test title",
        thumbnailUrl: String = "test thumbnailUrl",
        content: String = "test content",
        url: String = "test url",
        publishedAt: String = TimeProvider.now().toString()
    ) = BlogMetaData(
        title = title,
        thumbnailUrl = thumbnailUrl,
        content = content,
        url = url,
        publishedAt = TimeProvider.parseByString(publishedAt)
    )
}
