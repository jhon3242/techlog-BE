package won.techlog.blog.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class BlogMetaData(
    val title: String,
    val thumbnailUrl: String?,
    @Column(columnDefinition = "TEXT")
    val content: String,
    val url: String
)
