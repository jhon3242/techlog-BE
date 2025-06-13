package won.techlog.poster.api

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import won.techlog.poster.api.request.PosterSearchRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.api.response.PostersResponse
import won.techlog.poster.domain.PosterService

@RestController
@RequestMapping("/api")
class PosterController(
    private val posterService: PosterService
) {
    @GetMapping("/posters/{id}")
    fun getPoster(
        @PathVariable id: Long
    ): PosterResponse = posterService.getPoster(id)

    @GetMapping("/posters")
    fun searchPosters(
        @ModelAttribute request: PosterSearchRequest
    ): PostersResponse {
        return posterService.searchPosters(request)
    }

    @PutMapping("/posters/{id}/like")
    fun like(
        @PathVariable id: Long
    ) {
        posterService.like(id)
    }

    @DeleteMapping("/posters/{id}/like")
    fun cancelLike(
        @PathVariable id: Long
    ) {
        posterService.cancelLike(id)
    }

    @PutMapping("/posters/{id}/view")
    fun increaseView(
        @PathVariable id: Long
    ) {
        posterService.increaseView(id)
    }
}
