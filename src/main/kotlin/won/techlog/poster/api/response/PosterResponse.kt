package won.techlog.poster.api.response

import won.techlog.poster.domain.Poster
import won.techlog.tag.domain.Tag

class PosterResponse(
    val id: Long,
    val title: String,
    val thumbnail: String?,
    val content: String,
    val url: String,
    val blogType: String,
    val likeCount: Long,
    val views: Long,
    val tags: List<String>,
    val publishedAt: String
) {
    constructor(poster: Poster, tags: List<Tag>) : this(
        id = poster.id,
        title = poster.blogMetaData.title,
        thumbnail = poster.blogMetaData.thumbnailUrl,
        content = poster.blogMetaData.content,
        url = poster.blogMetaData.url,
        blogType = poster.blogType.name,
        likeCount = poster.likeCount,
        views = poster.views,
        tags = tags.map { it.name },
        publishedAt = poster.blogMetaData.publishedAt.toString()
    )
}
