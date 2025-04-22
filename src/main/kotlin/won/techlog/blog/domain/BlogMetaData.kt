package won.techlog.blog.domain

import jakarta.persistence.Embeddable

@Embeddable
data class BlogMetaData(
    val title: String,
    val thumbnailUrl: String?,
    val content: String,
    val url: String
)
