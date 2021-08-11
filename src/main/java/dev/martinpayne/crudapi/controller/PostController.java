package dev.martinpayne.crudapi.controller;

import dev.martinpayne.crudapi.domainobject.Post;
import dev.martinpayne.crudapi.dto.PostWithNoId;
import dev.martinpayne.crudapi.repository.PostRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public record PostController(PostRepository postRepository) {

    @GetMapping("/posts")
    public Flux<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/posts/{id}")
    public Mono<Post> getPost(@PathVariable String id) {
        return postRepository.findById(id);
    }

    @PostMapping(value = "/posts")
    public Mono<Post> createPost(@RequestBody PostWithNoId post) {
        return postRepository.save(new Post(UUID.randomUUID().toString(), post.title(), post.content()));
    }

    @PutMapping(value = "/posts/{id}")
    public Mono<Post> updatePost(@PathVariable String id, @RequestBody PostWithNoId post) {
        return postRepository.save(new Post(id, post.title(), post.content()));
    }

    @DeleteMapping(value = "/posts/{id}")
    public Mono<String> deletePost(@PathVariable String id) {
        return postRepository.deleteById(id)
                .then(Mono.just("post with id: " + id + " was deleted"));
    }
}
