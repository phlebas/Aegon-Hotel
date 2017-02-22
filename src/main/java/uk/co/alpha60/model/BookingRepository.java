package uk.co.alpha60.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByRoomId(Long roomId);

    @Query("select b from Booking b where b.id = :roomId and " +
            "((:checkIn >= b.checkIn and :checkIn <= b.checkOut) or" +
            " (:checkOut >= b.checkOut and :checkOut <= b.checkOut) )")
    List<Booking> getBookings(@Param("roomId") Long roomId, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
