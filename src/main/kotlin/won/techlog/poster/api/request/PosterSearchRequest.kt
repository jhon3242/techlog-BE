package won.techlog.poster.api.request

data class PosterSearchRequest(
    val keyword: String? = null,
    val tags: List<String>? = null,
    val blogType: String? = null,
    val cursor: String? = null
)
