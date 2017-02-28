package uk.co.alpha60.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.co.alpha60.HotelApplication;
import uk.co.alpha60.model.Booking;
import uk.co.alpha60.model.BookingRepository;
import uk.co.alpha60.model.Customer;
import uk.co.alpha60.model.CustomerRepository;
import uk.co.alpha60.model.Room;
import uk.co.alpha60.model.RoomRepository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelApplication.class)
@WebAppConfiguration
public class BookingRestControllerTest {

   private MockMvc mockMvc;

   private HttpMessageConverter mappingJackson2HttpMessageConverter;

   private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
         MediaType.APPLICATION_JSON.getSubtype(),
         Charset.forName("utf8"));

   private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

   @Autowired
   private WebApplicationContext webApplicationContext;

   @Autowired
   private BookingRepository bookingRepository;

   @Autowired
   private CustomerRepository customerRepository;

   @Autowired
   private RoomRepository roomRepository;
   private Booking booking1;
   private Customer customer1;
   private Room room1;

   @Autowired
   void setConverters(HttpMessageConverter<?>[] converters) {

      mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

      assertNotNull("The JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
   }

   @Before
   public void setup() throws Exception {
      mockMvc = webAppContextSetup(webApplicationContext).build();

      bookingRepository.deleteAllInBatch();
      customerRepository.deleteAllInBatch();
      roomRepository.deleteAllInBatch();

      customer1 = customerRepository.save(new Customer(1L, "customer1"));

      room1 = new Room(1L, 1, 100.0f);
      room1 = roomRepository.save(room1);
      Room room2 = new Room(2L, 1, 100.0f);
      room2 = roomRepository.save(room2);
      Room room3 = new Room(3L, 2, 200.0f);
      room3 = roomRepository.save(room3);
      Room room4 = new Room(4L, 2, 200.0f);
      room4 = roomRepository.save(room4);
      Room room5 = new Room(5L, 2, 200.0f);
      room5 = roomRepository.save(room5);

      LocalDate checkin = LocalDate.now();
      LocalDate checkout = LocalDate.now().plusDays(3);
      booking1 = bookingRepository.save(new Booking(1L, room1, customer1, checkin, checkout));

   }

   @Test
   public void getBookingsForRoom() throws Exception {
      mockMvc.perform(get("/bookings/room/" + room1.getId())
            .contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$[0].id", is(booking1.getId().intValue())))
            .andExpect(jsonPath("$[0].checkOut", is(booking1.getCheckOut().format(ISO_DATE))))
            .andExpect(jsonPath("$[0].checkIn", is(booking1.getCheckIn().format(ISO_DATE))));
   }

   @Test
   public void checkIsRoomBooked() throws Exception {
      String urlTemplate = "/bookings/available/" + room1.getId()
            + "/" + booking1.getCheckIn().format(ISO_DATE)
            + "/" + booking1.getCheckOut().format(ISO_DATE);
      mockMvc.perform(get(urlTemplate)
            .contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType));
   }

   @Test
   public void postBookingForRoom() throws Exception {
      String urlTemplate = "/bookings/book";
      LocalDate checkIn = LocalDate.now().plusDays(10);
      Booking booking = new Booking(2L, room1, customer1, checkIn, checkIn.plusDays(12));

      String json = gson.toJson(booking);

      mockMvc.perform(post(urlTemplate)
            .content(json)
            .contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.customer.id", is(booking.getCustomer().getId().intValue())))
            .andExpect(jsonPath("$.room.id", is(booking.getRoom().getId().intValue())))
            .andExpect(jsonPath("$.checkIn", is(booking.getCheckIn().format(ISO_DATE))))
            .andExpect(jsonPath("$.checkOut", is(booking.getCheckOut().format(ISO_DATE))));
   }

   @Test
   public void findSingleBookingForCustomer() throws Exception {
      mockMvc.perform(get("/bookings/customer/" + customer1.getId())
            .contentType(contentType))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$[0].id", is(booking1.getId().intValue())))
            .andExpect(jsonPath("$[0].checkOut", is(booking1.getCheckOut().format(ISO_DATE))))
            .andExpect(jsonPath("$[0].checkIn", is(booking1.getCheckIn().format(ISO_DATE))));
   }

   @Test
   public void customerNotFound() throws Exception {
      mockMvc.perform(get("/bookings/customer/0")
            .contentType(contentType))
            .andExpect(status().isNotFound());
   }

   @Test
   public void roomNotFound() throws Exception {
      mockMvc.perform(get("/bookings/room/0")
            .contentType(contentType))
            .andExpect(status().isNotFound());
   }

   @Test
   public void testJSONWriter() throws IOException {
      LocalDateAdapter localDateAdapter = new LocalDateAdapter();
      Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, localDateAdapter).create();

      LocalDate checkIn = LocalDate.now().plusDays(10);
      Booking booking = new Booking(2L, room1, customer1, checkIn, checkIn.plusDays(12));

      String json = gson.toJson(booking);
      assertNotNull(json);
   }

   class LocalDateAdapter implements JsonSerializer<LocalDate> {

      public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
         return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
      }
   }
}