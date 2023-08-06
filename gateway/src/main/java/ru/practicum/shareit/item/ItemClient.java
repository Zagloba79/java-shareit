package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.FullItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(Long userId, FullItemDto fullItemDto) {
        return post("", userId, fullItemDto);
    }

    public ResponseEntity<Object> getItemById(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsByOwner(Long userId, Integer from, Integer size) {
        String path = "?from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path, userId);
    }

    public ResponseEntity<Object> update(FullItemDto fullItemDto, Long itemId, Long userId) {
        return patch("/" + itemId, userId, fullItemDto);
    }

    public ResponseEntity<Object> delete(Long itemId, Long userId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsBySearchQuery(String text, Integer from, Integer size) {
        String path = "/search?text=" + text + "&from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path);
    }

    public ResponseEntity<Object> createComment(CommentDto commentDto, Long itemId, Long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}