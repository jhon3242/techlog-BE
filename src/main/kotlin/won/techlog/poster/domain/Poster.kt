package won.techlog.poster.domain

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import won.techlog.blog.domain.BlogMetaData

@Entity
class Poster(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Embedded
    val blogMetaData: BlogMetaData
)
