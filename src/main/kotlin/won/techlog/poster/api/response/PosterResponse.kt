package won.techlog.poster.api.response

import won.techlog.poster.domain.Poster

class PosterResponse(
    val title: String,
    val thumbnail: String?,
    val content: String,
    val url: String,
    val blogType: String
) {
    constructor(poster: Poster): this(
        title = poster.blogMetaData.title,
        thumbnail = poster.blogMetaData.thumbnailUrl,
        content = poster.blogMetaData.content,
        url = poster.blogMetaData.url,
        blogType = poster.blogType.name
        )
}
