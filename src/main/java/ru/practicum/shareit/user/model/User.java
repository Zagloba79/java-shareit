package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id // TODO надо дописать про каскад
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(length = 512, nullable = false, unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private UserState state;
}

//    @OneToMany
//    @JoinColumn(name = "TEACHER_ID", referencedColumnName = "ID")
//    private List<Course> courses;
//    Использование этой аннотации сообщит JPA, что COURSE должна иметь столбец внешнего ключа TEACHER_ID
//    который ссылается на столбец ID таблицы TEACHER