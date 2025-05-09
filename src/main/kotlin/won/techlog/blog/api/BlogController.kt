package won.techlog.blog.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import won.techlog.blog.api.request.BlogFetchRequest
import won.techlog.blog.api.request.BlogParseRequest
import won.techlog.blog.api.response.BlogResponse
import won.techlog.blog.domain.BlogService
import won.techlog.common.admin.AdminCheck

@AdminCheck
@RestController
@RequestMapping("/api")
class BlogController(
    private val blogService: BlogService
) {
    @PostMapping("/blogs")
    fun parseBlogs(
        @RequestBody blogParseRequest: BlogParseRequest
    ): List<BlogResponse> =
        blogService.parseBlogs(blogParseRequest.url)
            .map { BlogResponse(it) }

    @PostMapping("/blog")
    fun parseBlog(
        @RequestBody blogParseRequest: BlogParseRequest
    ): BlogResponse = BlogResponse(blogService.parseBlog(blogParseRequest.url))

    @PostMapping("/blogs/fetch")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun fetchBlogs(
        @RequestBody blogFetchRequest: BlogFetchRequest
    ) {
        blogService.fetchBlogs(blogFetchRequest.blogType)
    }

//    @PostMapping("/blogs/async")
//    suspend fun parseBlogsAsync(
//        @RequestBody blogParseRequest: BlogParseRequest
//    ): List<BlogResponse> =
//        blogService.parseBlogsAsync(blogParseRequest.url)
//            .map { BlogResponse(it) }
}
