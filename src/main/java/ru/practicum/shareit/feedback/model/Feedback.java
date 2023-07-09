package ru.practicum.shareit.feedback.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @OneToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(length = 1000)
    private String comment;
    @Column(nullable = false)
    private LocalDateTime created;
}