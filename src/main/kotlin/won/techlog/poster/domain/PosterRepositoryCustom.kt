package won.techlog.poster.domain

interface PosterRepositoryCustom {
    fun searchPosters(
        keyword: String?,
        tagNames: List<String>?,
        blogType: String?
    ): List<Poster>
}
