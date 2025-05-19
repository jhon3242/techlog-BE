package won.techlog.blog.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class BlogMetaData(
    var title: String,
    var thumbnailUrl: String?,
    @Column(columnDefinition = "TEXT")
    var content: String,
    var url: String
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
