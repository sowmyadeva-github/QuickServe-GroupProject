package quick_serve.repos;

import quick_serve.domain.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

    SupportTicket findFirstByUserId(Long id);

}
