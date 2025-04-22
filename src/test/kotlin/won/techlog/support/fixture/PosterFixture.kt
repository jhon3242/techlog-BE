package won.techlog.support.fixture

import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.Poster

object PosterFixture {

    fun create(
        title: String = "test title",
        thumbnailUrl: String = "test thumbnailUrl",
        content: String = "test content",
        url: String = "test url",
        views: Long = 0L,
        recommendations: Long = 0L,
    ) = Poster(
        blogMetaData = BlogMetaDataFixture.create(title, thumbnailUrl, content, url),
        blogType = BlogType.WOOWABRO,
        views = views,
        recommendations = recommendations
    )
}
