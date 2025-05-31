package won.techlog.blog.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.OffsetDateTime

@Embeddable
data class BlogMetaData(
    var title: String,
    var thumbnailUrl: String?,
    @Column(columnDefinition = "TEXT")
    var content: String,
    @Column(length = 1024)
    var url: String,
    val publishedAt: OffsetDateTime
) {
    fun update(
        title: String? = null,
        thumbnailUrl: String? = null,
        content: String? = null,
        url: String? = null
    ) {
        title?.let { this.title = it }
        thumbnailUrl?.let { this.thumbnailUrl = it }
        content?.let { this.content = it }
        url?.let { this.url = it }
    }
}
