package dev.martinpayne.crudapi.domainobject;

import org.apache.logging.log4j.util.Strings;

public record Post(String id, String title, String content) {

    public Post {
        if (Strings.isBlank(id)) {
            throw new IllegalArgumentException("id for Post must not be null, empty, or only whitespace");
        }

        if (Strings.isBlank(title)) {
            throw new IllegalArgumentException("title for Post must not be null, empty, or only whitespace");
        }

        if (Strings.isBlank(content)) {
            throw new IllegalArgumentException("content for Post must not be null, empty, or only whitespace");
        }
    }
}
