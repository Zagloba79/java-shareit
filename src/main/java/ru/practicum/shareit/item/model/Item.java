package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 1000, nullable = false)
    private String description;
    @Column(nullable = false)
    private Boolean available;
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column(nullable = false)
    private ItemRequest request;
}

//    @ElementCollection
//    @CollectionTable(name="tags", joinColumns=@JoinColumn(name="item_id"))
//    @Column(name="name")
//    private Set<String> tags = new HashSet<>();
//    https://practicum.yandex.ru/learn/java-developer/courses/ec5e03ed-e12f-43c5-93d1-638a79454e03/sprints/93440/topics/40bf15e5-a64b-400c-b9cc-eea2ee6d32e9/lessons/5e8e7972-490b-44d0-9374-68d743852e87/