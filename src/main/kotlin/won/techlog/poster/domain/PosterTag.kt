package won.techlog.poster.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import won.techlog.tag.domain.Tag

@Entity
class PosterTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val poster: Poster,
    @ManyToOne(fetch = FetchType.LAZY)
    val tag: Tag
)
