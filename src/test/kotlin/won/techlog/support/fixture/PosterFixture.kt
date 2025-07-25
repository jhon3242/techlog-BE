package won.techlog.support.fixture

import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.Poster
import java.time.OffsetDateTime
import java.util.UUID

object PosterFixture {
    fun create(
        title: String = "test title",
        thumbnail: String = "test thumbnailUrl",
        content: String = "test content",
        url: String = "test url" + UUID.randomUUID(),
        views: Long = 0L,
        recommendations: Long = 0L,
        blogType: BlogType = BlogType.WOOWABRO,
        publishedAt: OffsetDateTime? = OffsetDateTime.now()
    ) = Poster(
        blogMetaData = BlogMetaDataFixture.create(title, thumbnail, content, url, publishedAt = publishedAt.toString()),
        blogType = blogType,
        views = views,
        likeCount = recommendations
    )
}
