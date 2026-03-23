package quick_serve.repos;

import quick_serve.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findFirstByCustomerId(Long id);

    Booking findFirstByProviderServiceId(Long id);

}
