package won.techlog.poster.domain

interface PosterRepositoryCustom {
    fun searchPosters(
        keyword: String? = null,
        tagNames: List<String>? = null,
        blogType: String? = null
    ): List<Poster>
}
