package quick_serve.service;

import quick_serve.events.BeforeDeleteService;
import quick_serve.model.ServiceDTO;
import quick_serve.repos.ServiceRepository;
import quick_serve.util.CustomCollectors;
import quick_serve.util.NotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ApplicationEventPublisher publisher;

    public ServiceService(final ServiceRepository serviceRepository,
            final ApplicationEventPublisher publisher) {
        this.serviceRepository = serviceRepository;
        this.publisher = publisher;
    }

    public List<ServiceDTO> findAll() {
        final List<quick_serve.domain.Service> services = serviceRepository.findAll(Sort.by("id"));
        return services.stream()
                .map(service -> mapToDTO(service, new ServiceDTO()))
                .toList();
    }

    public ServiceDTO get(final Long id) {
        return serviceRepository.findById(id)
                .map(service -> mapToDTO(service, new ServiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ServiceDTO serviceDTO) {
        final quick_serve.domain.Service service = new quick_serve.domain.Service();
        mapToEntity(serviceDTO, service);
        return serviceRepository.save(service).getId();
    }

    public void update(final Long id, final ServiceDTO serviceDTO) {
        final quick_serve.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(serviceDTO, service);
        serviceRepository.save(service);
    }

    public void delete(final Long id) {
        final quick_serve.domain.Service service = serviceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteService(id));
        serviceRepository.delete(service);
    }

    private ServiceDTO mapToDTO(final quick_serve.domain.Service service,
            final ServiceDTO serviceDTO) {
        serviceDTO.setId(service.getId());
        serviceDTO.setName(service.getName());
        serviceDTO.setDescription(service.getDescription());
        serviceDTO.setCategory(service.getCategory());
        serviceDTO.setBasePrice(service.getBasePrice());
        serviceDTO.setImageUrl(service.getImageUrl());
        return serviceDTO;
    }

    private quick_serve.domain.Service mapToEntity(final ServiceDTO serviceDTO,
                                                   final quick_serve.domain.Service service) {
        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setCategory(serviceDTO.getCategory());
        service.setBasePrice(serviceDTO.getBasePrice());
        service.setImageUrl(serviceDTO.getImageUrl());
        return service;
    }

    public Map<Long, String> getServiceValues() {
        return serviceRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(quick_serve.domain.Service::getId, quick_serve.domain.Service::getName));
    }

}
