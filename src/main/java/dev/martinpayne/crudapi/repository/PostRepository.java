package dev.martinpayne.crudapi.repository;

import dev.martinpayne.crudapi.domainobject.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}
