package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId,
                                                              LocalDateTime start,
                                                              LocalDateTime end,
                                                              Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemId(Long itemId);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItem_OwnerId(Long ownerId, Sort sort);

    List<Booking> findByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId,
                                                                   LocalDateTime start,
                                                                   LocalDateTime end,
                                                                   Sort sort);

    List<Booking> findByItem_OwnerIdAndEndIsBefore(Long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByItem_OwnerIdAndStartIsAfter(Long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByItem_OwnerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime end);

    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime end);

    Booking findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(Long itemId,
                                                                Long userId,
                                                                LocalDateTime end,
                                                                BookingStatus status);
}