package won.techlog.poster.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.domain.PosterService

@RestController
@RequestMapping("/api")
class PosterController(
    private val posterService: PosterService
) {
    @GetMapping("/posters")
    fun getPosters(
        @RequestParam page: Int,
        @RequestParam size: Int
    ): List<PosterResponse> =
        posterService.getPosters(page, size)


    @GetMapping("/posters/{id}")
    fun getPoster(
        @PathVariable id: Long
    ): PosterResponse = posterService.getPoster(id)

    @PutMapping("/posters/{id}/recommend")
    fun recommend(
        @PathVariable id: Long
    ) {
        posterService.recommend(id)
    }

    @PutMapping("/posters/{id}/view")
    fun increaseView(
        @PathVariable id: Long
    ) {
        posterService.increaseView(id)
    }
}
