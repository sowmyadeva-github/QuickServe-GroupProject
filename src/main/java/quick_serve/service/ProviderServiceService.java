package quick_serve.service;

import quick_serve.domain.ProviderService;
import quick_serve.domain.User;
import quick_serve.events.BeforeDeleteProviderService;
import quick_serve.events.BeforeDeleteService;
import quick_serve.events.BeforeDeleteUser;
import quick_serve.model.ProviderServiceDTO;
import quick_serve.repos.ProviderServiceRepository;
import quick_serve.repos.ServiceRepository;
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
public class ProviderServiceService {

    private final ProviderServiceRepository providerServiceRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final ApplicationEventPublisher publisher;

    public ProviderServiceService(final ProviderServiceRepository providerServiceRepository,
            final UserRepository userRepository, final ServiceRepository serviceRepository,
            final ApplicationEventPublisher publisher) {
        this.providerServiceRepository = providerServiceRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.publisher = publisher;
    }

    public List<ProviderServiceDTO> findAll() {
        final List<ProviderService> providerServices = providerServiceRepository.findAll(Sort.by("id"));
        return providerServices.stream()
                .map(providerService -> mapToDTO(providerService, new ProviderServiceDTO()))
                .toList();
    }

    public ProviderServiceDTO get(final Long id) {
        return providerServiceRepository.findById(id)
                .map(providerService -> mapToDTO(providerService, new ProviderServiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProviderServiceDTO providerServiceDTO) {
        final ProviderService providerService = new ProviderService();
        mapToEntity(providerServiceDTO, providerService);
        return providerServiceRepository.save(providerService).getId();
    }

    public void update(final Long id, final ProviderServiceDTO providerServiceDTO) {
        final ProviderService providerService = providerServiceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(providerServiceDTO, providerService);
        providerServiceRepository.save(providerService);
    }

    public void delete(final Long id) {
        final ProviderService providerService = providerServiceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteProviderService(id));
        providerServiceRepository.delete(providerService);
    }

    private ProviderServiceDTO mapToDTO(final ProviderService providerService,
            final ProviderServiceDTO providerServiceDTO) {
        providerServiceDTO.setId(providerService.getId());
        providerServiceDTO.setPrice(providerService.getPrice());
        providerServiceDTO.setDescription(providerService.getDescription());
        providerServiceDTO.setAvailability(providerService.getAvailability());
        providerServiceDTO.setIsAvailable(providerService.getIsAvailable());
        providerServiceDTO.setProvider(providerService.getProvider() == null ? null : providerService.getProvider().getId());
        providerServiceDTO.setService(providerService.getService() == null ? null : providerService.getService().getId());
        return providerServiceDTO;
    }

    private ProviderService mapToEntity(final ProviderServiceDTO providerServiceDTO,
            final ProviderService providerService) {
        providerService.setPrice(providerServiceDTO.getPrice());
        providerService.setDescription(providerServiceDTO.getDescription());
        providerService.setAvailability(providerServiceDTO.getAvailability());
        providerService.setIsAvailable(providerServiceDTO.getIsAvailable());
        final User provider = providerServiceDTO.getProvider() == null ? null : userRepository.findById(providerServiceDTO.getProvider())
                .orElseThrow(() -> new NotFoundException("provider not found"));
        providerService.setProvider(provider);
        final quick_serve.domain.Service service = providerServiceDTO.getService() == null ? null : serviceRepository.findById(providerServiceDTO.getService())
                .orElseThrow(() -> new NotFoundException("service not found"));
        providerService.setService(service);
        return providerService;
    }

    public Map<Long, Long> getProviderServiceValues() {
        return providerServiceRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ProviderService::getId, ProviderService::getId));
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final ProviderService providerProviderService = providerServiceRepository.findFirstByProviderId(event.getId());
        if (providerProviderService != null) {
            referencedException.setKey("user.providerService.provider.referenced");
            referencedException.addParam(providerProviderService.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteService.class)
    public void on(final BeforeDeleteService event) {
        final ReferencedException referencedException = new ReferencedException();
        final ProviderService serviceProviderService = providerServiceRepository.findFirstByServiceId(event.getId());
        if (serviceProviderService != null) {
            referencedException.setKey("service.providerService.service.referenced");
            referencedException.addParam(serviceProviderService.getId());
            throw referencedException;
        }
    }

}
