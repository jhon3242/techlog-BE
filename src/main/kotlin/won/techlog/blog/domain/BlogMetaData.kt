package won.techlog.blog.domain

import jakarta.persistence.Embeddable
import jakarta.persistence.Lob

@Embeddable
data class BlogMetaData(
    val title: String,
    val thumbnailUrl: String?,
    @Lob
    val content: String,
    val url: String
)
