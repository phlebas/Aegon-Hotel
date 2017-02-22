package uk.co.alpha60.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import uk.co.alpha60.model.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingRestController {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingRestController(BookingRepository bookingRepository,
                                 CustomerRepository customerRepository,
                                 RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Retrieves any bookings for a specific room ID
     * @param roomId the room Id
     * @return a list of bookings
     */
    @RequestMapping(path="/room/{roomId}", method = RequestMethod.GET)
    Collection<Booking> getBookingsForRoom(@PathVariable Long roomId) {
        validateRoom(roomId);
        return bookingRepository.findByRoomId(roomId);
    }

    /**
     * Retrieves any bookings for a specific customer ID
     * @param customerId the customer Id
     * @return a list of bookings
     */
    @RequestMapping(path = "/customer/{customerId}", method = RequestMethod.GET)
    Collection<Booking> getBookingsForCustomer(@PathVariable Long customerId) {
        validateCustomerId(customerId);
        return bookingRepository.findByCustomerId(customerId);
    }

    /**
     * Determines the availability of a specific room during a given date range
     * @param roomId the room Id
     * @param checkIn checkin date
     * @param checkOut checkout date
     * @return True iff a the room is available between these dates.
     */
    @RequestMapping(path = "/available/{roomId}/{checkIn}/{checkOut}", method = RequestMethod.GET)
    public Boolean isRoomAvailable(@PathVariable Long roomId,
                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                                   @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        validateRoom(roomId);
        validateDates(checkIn, checkOut);
        return roomHasNoBookings(roomId, checkIn, checkOut);
    }

    private Boolean roomHasNoBookings(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> bookings = bookingRepository.getBookings(roomId, checkIn, checkOut);
        return bookings.isEmpty();
    }

    /**
     * Makes a booking for a room.
     * @param booking the booking object
     * @return the Booking object created.
     */
    @RequestMapping(path="/book", method= RequestMethod.POST)
    public Booking makeBooking(@RequestBody Booking booking) {

        validateDates(booking.getCheckIn(), booking.getCheckOut());
        validateRoom(booking.getId());
        validateCustomerId(booking.getCustomer().getId());
        return bookingRepository.save(booking);

    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new InvalidBookingDatesException("Checkout date cannot be before checkin date.");
        }
    }

    private void validateRoom(Long roomId) throws RoomNotFoundException {
        roomRepository.findById(roomId).orElseThrow(
                () -> new RoomNotFoundException(roomId));
    }

    private Customer validateCustomerId(Long customerId) throws RoomNotFoundException {
        return customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException(customerId));
    }

}
