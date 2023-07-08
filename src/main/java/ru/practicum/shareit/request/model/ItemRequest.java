package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @MapsId
    @OneToOne
    @JoinColumn(name = "request_id")
    private Long id;
    @Column(length=1000, nullable = false)
    private String description;
    @Column
    private User requester;
    @Column(nullable = false)
    private LocalDate created;
}