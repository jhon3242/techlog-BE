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

@Entity
class Poster(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Embedded
    val blogMetaData: BlogMetaData,
    @Enumerated(EnumType.STRING)
    @Column(name = "blog_type", length = 20)
    val blogType: BlogType,
    var recommendations: Long = 0L,
    var views: Long = 0L,
    var isDeleted: Boolean = false
)
