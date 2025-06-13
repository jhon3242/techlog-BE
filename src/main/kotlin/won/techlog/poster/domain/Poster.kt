package won.techlog.poster.domain

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.common.BaseTimeEntity

@Entity
class Poster(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Embedded
    val blogMetaData: BlogMetaData,
    @Enumerated(EnumType.STRING)
    @Column(name = "blog_type", length = 20)
    var blogType: BlogType,
    var likeCount: Long = 0L,
    var views: Long = 0L,
    var isDeleted: Boolean = false
) : BaseTimeEntity() {
    fun update(
        title: String? = null,
        url: String? = null,
        content: String? = null,
        thumbnailUrl: String? = null,
        blogType: BlogType? = null
    ) {
        blogMetaData.update(
            title = title,
            url = url,
            content = content,
            thumbnailUrl = thumbnailUrl
        )
        this.blogType = blogType ?: this.blogType
    }
}
