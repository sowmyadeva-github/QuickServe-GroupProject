package quick_serve.service;

import quick_serve.domain.Booking;
import quick_serve.domain.Review;
import quick_serve.events.BeforeDeleteBooking;
import quick_serve.model.ReviewDTO;
import quick_serve.repos.BookingRepository;
import quick_serve.repos.ReviewRepository;
import quick_serve.util.NotFoundException;
import quick_serve.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    public ReviewService(final ReviewRepository reviewRepository,
            final BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<ReviewDTO> findAll() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("id"));
        return reviews.stream()
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .toList();
    }

    public ReviewDTO get(final Long id) {
        return reviewRepository.findById(id)
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReviewDTO reviewDTO) {
        final Review review = new Review();
        mapToEntity(reviewDTO, review);
        return reviewRepository.save(review).getId();
    }

    public void update(final Long id, final ReviewDTO reviewDTO) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);
    }

    public void delete(final Long id) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        reviewRepository.delete(review);
    }

    private ReviewDTO mapToDTO(final Review review, final ReviewDTO reviewDTO) {
        reviewDTO.setId(review.getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setBooking(review.getBooking() == null ? null : review.getBooking().getId());
        return reviewDTO;
    }

    private Review mapToEntity(final ReviewDTO reviewDTO, final Review review) {
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(reviewDTO.getCreatedAt());
        final Booking booking = reviewDTO.getBooking() == null ? null : bookingRepository.findById(reviewDTO.getBooking())
                .orElseThrow(() -> new NotFoundException("booking not found"));
        review.setBooking(booking);
        return review;
    }

    @EventListener(BeforeDeleteBooking.class)
    public void on(final BeforeDeleteBooking event) {
        final ReferencedException referencedException = new ReferencedException();
        final Review bookingReview = reviewRepository.findFirstByBookingId(event.getId());
        if (bookingReview != null) {
            referencedException.setKey("booking.review.booking.referenced");
            referencedException.addParam(bookingReview.getId());
            throw referencedException;
        }
    }

}
