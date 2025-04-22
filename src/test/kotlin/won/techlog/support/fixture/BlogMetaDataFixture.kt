package won.techlog.support.fixture

import won.techlog.blog.domain.BlogMetaData

object BlogMetaDataFixture {

    fun create(
        title: String = "test title",
        thumbnailUrl: String = "test thumbnailUrl",
        content: String = "test content",
        url: String = "test url",
    ) = BlogMetaData(
        title = title,
        thumbnailUrl = thumbnailUrl,
        content = content,
        url = url,
    )
}
