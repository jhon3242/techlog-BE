package won.techlog.blog.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import won.techlog.blog.api.request.BlogRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.blog.domain.BlogService

@RestController
@RequestMapping("/api")
class BlogController(
    private val blogService: BlogService
) {
    @PostMapping("/blogs")
    fun parseBlogs(
        @RequestBody blogRequest: BlogRequest
    ): List<BlogResponse> =
        blogService.parseBlogs(blogRequest.url)
            .map { BlogResponse(it) }

    @PostMapping("/blog")
    fun parseBlog(
        @RequestBody blogRequest: BlogRequest
    ): BlogResponse = BlogResponse(blogService.parseBlog(blogRequest.url))
}
