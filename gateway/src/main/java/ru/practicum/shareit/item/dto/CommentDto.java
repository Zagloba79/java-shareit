package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @Nullable
    private Long id;
    @NotBlank
    private String text;
    @Nullable
    private String authorName;
    @Nullable
    private LocalDateTime created;

    public CommentDto(String text) {
        this.text = text;
    }
}
