package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndAndStartIsBeforeAndEndIsAfter(Long bookerId,
                                                                 LocalDateTime start,
                                                                 LocalDateTime end,
                                                                 Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_OwnerId(Long ownerId, Pageable pageable);

    List<Booking> findByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId,
                                                                  LocalDateTime start,
                                                                  LocalDateTime end,
                                                                  Pageable pageable);

    List<Booking> findByItem_OwnerIdAndEndIsBefore(Long bookerId,
                                                   LocalDateTime end,
                                                   Pageable pageable);

    List<Booking> findByItem_OwnerIdAndStartIsAfter(Long bookerId,
                                                    LocalDateTime start,
                                                    Pageable pageable);

    List<Booking> findByItem_OwnerIdAndStatus(Long bookerId, BookingStatus status, Pageable pageable);

    Booking findByItemIdAndStatusIsAndStartBeforeAndEndAfter(Long itemId,
                                                             BookingStatus status,
                                                             LocalDateTime start,
                                                             LocalDateTime end);

    Booking findFirstByItemIdAndStatusIsAndEndBeforeOrderByEndDesc(Long itemId,
                                                                   BookingStatus status,
                                                                   LocalDateTime end);

    Booking findFirstByItemIdAndStatusIsAndStartAfterOrderByStartAsc(Long itemId,
                                                                     BookingStatus status,
                                                                     LocalDateTime end);

    Booking findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(Long itemId,
                                                                Long userId,
                                                                LocalDateTime end,
                                                                BookingStatus status);

    Booking findByIdAndBookerId(Long id, Long userId);

    Optional<Booking> findByIdAndItem_OwnerId(Long id, Long userId);
}