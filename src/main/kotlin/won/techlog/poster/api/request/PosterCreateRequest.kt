package won.techlog.poster.api.request

import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.Poster
import java.time.OffsetDateTime

data class PosterCreateRequest(
    val title: String,
    val thumbnail: String?,
    val content: String,
    val url: String,
    val blogType: String,
    val tags: List<String> = mutableListOf(),
    val publishedAt: OffsetDateTime
) {
    constructor(blogMetaData: BlogMetaData, blogType: BlogType) : this(
        title = blogMetaData.title,
        thumbnail = blogMetaData.thumbnailUrl,
        content = blogMetaData.content,
        url = blogMetaData.url,
        blogType = blogType.name,
        publishedAt = blogMetaData.publishedAt
    )

    constructor(poster: Poster, tags: List<String>) : this(
        title = poster.blogMetaData.title,
        thumbnail = poster.blogMetaData.thumbnailUrl,
        content = poster.blogMetaData.content,
        url = poster.blogMetaData.url,
        blogType = poster.blogType.name,
        publishedAt = poster.blogMetaData.publishedAt,
        tags = tags
    )

    fun toPoster(): Poster =
        Poster(
            blogMetaData =
                BlogMetaData(
                    title = title,
                    thumbnailUrl = thumbnail,
                    content = content,
                    url = url,
                    publishedAt = publishedAt
                ),
            blogType =
                BlogType.valueOf(
                    blogType
                )
        )
}
