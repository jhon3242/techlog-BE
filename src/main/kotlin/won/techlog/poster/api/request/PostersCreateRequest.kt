package won.techlog.poster.api.request

import won.techlog.poster.domain.Poster

data class PostersCreateRequest(
    val posters: List<PosterCreateRequest>
) {
    fun toPosters(): List<Poster> = posters.map { it.toPoster() }
}
