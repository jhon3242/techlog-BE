package won.techlog.poster.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import won.techlog.common.admin.AdminCheck
import won.techlog.poster.api.request.PosterCreateRequest
import won.techlog.poster.api.request.PosterUpdateRequest
import won.techlog.poster.api.request.PostersCreateRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.api.response.PostersResponse
import won.techlog.poster.domain.PosterService

@AdminCheck
@RestController
@RequestMapping("/api")
class AdminPosterController(
    private val posterService: PosterService
) {
    @PostMapping("/poster")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPoster(
        @RequestBody request: PosterCreateRequest
    ): PosterResponse {
        return posterService.createPoster(request.toPoster(), request.tags)
    }

    @PostMapping("/posters")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPosters(
        @RequestBody request: PostersCreateRequest
    ): PostersResponse {
        val posterResponse =
            request.posters
                .map { createPoster(it) }
        return PostersResponse(posterResponse)
    }

    @PutMapping("/posters/{id}")
    fun updatePoster(
        @PathVariable id: Long,
        @RequestBody request: PosterUpdateRequest
    ): PosterResponse {
        return posterService.updatePoster(id, request)
    }

    @DeleteMapping("posters/{id}")
    fun deletePoster(
        @PathVariable id: Long
    ) = posterService.deletePoster(id)
}
