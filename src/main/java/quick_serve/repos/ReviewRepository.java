package quick_serve.repos;

import quick_serve.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findFirstByBookingId(Long id);

}
