package quick_serve.service;

import quick_serve.domain.Booking;
import quick_serve.domain.ProviderService;
import quick_serve.domain.User;
import quick_serve.events.BeforeDeleteBooking;
import quick_serve.events.BeforeDeleteProviderService;
import quick_serve.events.BeforeDeleteUser;
import quick_serve.model.BookingDTO;
import quick_serve.repos.BookingRepository;
import quick_serve.repos.ProviderServiceRepository;
import quick_serve.repos.UserRepository;
import quick_serve.util.CustomCollectors;
import quick_serve.util.NotFoundException;
import quick_serve.util.ReferencedException;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProviderServiceRepository providerServiceRepository;
    private final ApplicationEventPublisher publisher;

    public BookingService(final BookingRepository bookingRepository,
            final UserRepository userRepository,
            final ProviderServiceRepository providerServiceRepository,
            final ApplicationEventPublisher publisher) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.providerServiceRepository = providerServiceRepository;
        this.publisher = publisher;
    }

    public List<BookingDTO> findAll() {
        final List<Booking> bookings = bookingRepository.findAll(Sort.by("id"));
        return bookings.stream()
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .toList();
    }

    public BookingDTO get(final Long id) {
        return bookingRepository.findById(id)
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        mapToEntity(bookingDTO, booking);
        return bookingRepository.save(booking).getId();
    }

    public void update(final Long id, final BookingDTO bookingDTO) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingDTO, booking);
        bookingRepository.save(booking);
    }

    public void delete(final Long id) {
        final Booking booking = bookingRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteBooking(id));
        bookingRepository.delete(booking);
    }

    private BookingDTO mapToDTO(final Booking booking, final BookingDTO bookingDTO) {
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingDate(booking.getBookingDate());
        bookingDTO.setAddress(booking.getAddress());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setPaymentStatus(booking.getPaymentStatus());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        bookingDTO.setCreatedAt(booking.getCreatedAt());
        bookingDTO.setCustomer(booking.getCustomer() == null ? null : booking.getCustomer().getId());
        bookingDTO.setProviderService(booking.getProviderService() == null ? null : booking.getProviderService().getId());
        return bookingDTO;
    }

    private Booking mapToEntity(final BookingDTO bookingDTO, final Booking booking) {
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setAddress(bookingDTO.getAddress());
        booking.setStatus(bookingDTO.getStatus());
        booking.setPaymentStatus(bookingDTO.getPaymentStatus());
        booking.setTotalPrice(bookingDTO.getTotalPrice());
        booking.setCreatedAt(bookingDTO.getCreatedAt());
        final User customer = bookingDTO.getCustomer() == null ? null : userRepository.findById(bookingDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        booking.setCustomer(customer);
        final ProviderService providerService = bookingDTO.getProviderService() == null ? null : providerServiceRepository.findById(bookingDTO.getProviderService())
                .orElseThrow(() -> new NotFoundException("providerService not found"));
        booking.setProviderService(providerService);
        return booking;
    }

    public Map<Long, String> getBookingValues() {
        return bookingRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Booking::getId, Booking::getStatus));
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Booking customerBooking = bookingRepository.findFirstByCustomerId(event.getId());
        if (customerBooking != null) {
            referencedException.setKey("user.booking.customer.referenced");
            referencedException.addParam(customerBooking.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteProviderService.class)
    public void on(final BeforeDeleteProviderService event) {
        final ReferencedException referencedException = new ReferencedException();
        final Booking providerServiceBooking = bookingRepository.findFirstByProviderServiceId(event.getId());
        if (providerServiceBooking != null) {
            referencedException.setKey("providerService.booking.providerService.referenced");
            referencedException.addParam(providerServiceBooking.getId());
            throw referencedException;
        }
    }

}
