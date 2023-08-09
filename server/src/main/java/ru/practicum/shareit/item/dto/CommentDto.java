package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    @NotBlank
    private String authorName;
    @DateTimeFormat
    private LocalDateTime created;

    public CommentDto(String text, String authorName, LocalDateTime created) {
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}