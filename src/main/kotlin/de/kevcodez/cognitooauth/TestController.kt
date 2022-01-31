package de.kevcodez.cognitooauth

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('user')")
    fun hello(): String {
        return "I am authenticated user!"
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('LEVEL_ADMIN')")
    fun admin(): String {
        return "I am an authenticated admin!"
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('LEVEL_USER') OR hasAuthority('LEVEL_ADMIN')")
    fun user(): String {
        return "I am an authenticated user!"
    }
}

