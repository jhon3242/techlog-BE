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
    val recommendations: Long,
    val views: Long,
    val tags: List<String>
) {
    constructor(poster: Poster, tags: List<Tag>) : this(
        id = poster.id,
        title = poster.blogMetaData.title,
        thumbnail = poster.blogMetaData.thumbnailUrl,
        content = poster.blogMetaData.content,
        url = poster.blogMetaData.url,
        blogType = poster.blogType.name,
        recommendations = poster.recommendations,
        views = poster.views,
        tags = tags.map { it.name }
    )
}
