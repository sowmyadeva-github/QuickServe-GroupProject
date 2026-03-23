package quick_serve.service;

import quick_serve.domain.SupportTicket;
import quick_serve.domain.User;
import quick_serve.events.BeforeDeleteUser;
import quick_serve.model.SupportTicketDTO;
import quick_serve.repos.SupportTicketRepository;
import quick_serve.repos.UserRepository;
import quick_serve.util.NotFoundException;
import quick_serve.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    public SupportTicketService(final SupportTicketRepository supportTicketRepository,
            final UserRepository userRepository) {
        this.supportTicketRepository = supportTicketRepository;
        this.userRepository = userRepository;
    }

    public List<SupportTicketDTO> findAll() {
        final List<SupportTicket> supportTickets = supportTicketRepository.findAll(Sort.by("id"));
        return supportTickets.stream()
                .map(supportTicket -> mapToDTO(supportTicket, new SupportTicketDTO()))
                .toList();
    }

    public SupportTicketDTO get(final Long id) {
        return supportTicketRepository.findById(id)
                .map(supportTicket -> mapToDTO(supportTicket, new SupportTicketDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SupportTicketDTO supportTicketDTO) {
        final SupportTicket supportTicket = new SupportTicket();
        mapToEntity(supportTicketDTO, supportTicket);
        return supportTicketRepository.save(supportTicket).getId();
    }

    public void update(final Long id, final SupportTicketDTO supportTicketDTO) {
        final SupportTicket supportTicket = supportTicketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(supportTicketDTO, supportTicket);
        supportTicketRepository.save(supportTicket);
    }

    public void delete(final Long id) {
        final SupportTicket supportTicket = supportTicketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        supportTicketRepository.delete(supportTicket);
    }

    private SupportTicketDTO mapToDTO(final SupportTicket supportTicket,
            final SupportTicketDTO supportTicketDTO) {
        supportTicketDTO.setId(supportTicket.getId());
        supportTicketDTO.setSubject(supportTicket.getSubject());
        supportTicketDTO.setMessage(supportTicket.getMessage());
        supportTicketDTO.setStatus(supportTicket.getStatus());
        supportTicketDTO.setCreatedAt(supportTicket.getCreatedAt());
        supportTicketDTO.setUser(supportTicket.getUser() == null ? null : supportTicket.getUser().getId());
        return supportTicketDTO;
    }

    private SupportTicket mapToEntity(final SupportTicketDTO supportTicketDTO,
            final SupportTicket supportTicket) {
        supportTicket.setSubject(supportTicketDTO.getSubject());
        supportTicket.setMessage(supportTicketDTO.getMessage());
        supportTicket.setStatus(supportTicketDTO.getStatus());
        supportTicket.setCreatedAt(supportTicketDTO.getCreatedAt());
        final User user = supportTicketDTO.getUser() == null ? null : userRepository.findById(supportTicketDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        supportTicket.setUser(user);
        return supportTicket;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final SupportTicket userSupportTicket = supportTicketRepository.findFirstByUserId(event.getId());
        if (userSupportTicket != null) {
            referencedException.setKey("user.supportTicket.user.referenced");
            referencedException.addParam(userSupportTicket.getId());
            throw referencedException;
        }
    }

}
