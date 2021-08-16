package dev.martinpayne.crudapi.controller;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import dev.martinpayne.crudapi.domainobject.Post;
import dev.martinpayne.crudapi.dto.PostWithNoId;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public record PostController(BigtableDataClient dataClient, String tableId) {

    @GetMapping("/posts")
    public Flux<Post> getAllPosts() {
        return Flux.create(sink -> {
            var query = Query.create(tableId);
            var rows = dataClient.readRows(query);
            for (var r : rows) {
                var id = r.getKey().toStringUtf8();
                var title = r.getCells().get(1).getValue().toStringUtf8();
                var content = r.getCells().get(0).getValue().toStringUtf8();
                sink.next(new Post(id, title, content));
            }
            sink.complete();
        });
    }

    @GetMapping("/posts/{id}")
    public Mono<Post> getPost(@PathVariable String id) {
        var row = dataClient.readRow(tableId, id);
        var title = row.getCells("Post", "title").get(0).getValue().toStringUtf8();
        var content = row.getCells("Post", "content").get(0).getValue().toStringUtf8();
        return Mono.just(new Post(id, title, content));
    }

    @PostMapping(value = "/posts")
    public Mono<Post> createPost(@RequestBody PostWithNoId post) {
        var id = UUID.randomUUID().toString();
        var rowMutation = RowMutation.create(tableId, id);
        rowMutation.setCell("Post", "title", post.title());
        rowMutation.setCell("Post", "content", post.content());
        dataClient.mutateRow(rowMutation);

        return getPost(id);
    }

    @PutMapping(value = "/posts/{id}")
    public Mono<Post> updatePost(@PathVariable String id, @RequestBody PostWithNoId post) {
        var rowMutation = RowMutation.create(tableId, id);
        rowMutation.setCell("Post", "title", post.title());
        rowMutation.setCell("Post", "content", post.content());
        dataClient.mutateRow(rowMutation);

        return getPost(id);
    }

    @DeleteMapping(value = "/posts/{id}")
    public Mono<String> deletePost(@PathVariable String id) {
        var rowMutation = RowMutation.create(tableId, id);
        rowMutation.deleteRow();
        dataClient.mutateRow(rowMutation);

        return Mono.just("deleted row with id: " + id);
    }
}
