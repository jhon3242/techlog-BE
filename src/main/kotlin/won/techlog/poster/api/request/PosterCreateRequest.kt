package won.techlog.poster.api.request

import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.Poster

data class PosterCreateRequest(
    val title: String,
    val thumbnail: String?,
    val content: String,
    val url: String,
    val blogType: String,
    val tags: List<String>
) {
    fun toPoster(): Poster =
        Poster(
            blogMetaData =
                BlogMetaData(
                    title = title,
                    thumbnailUrl = thumbnail,
                    content = content,
                    url = url
                ),
            blogType =
                BlogType.valueOf(
                    blogType
                )
        )
}
