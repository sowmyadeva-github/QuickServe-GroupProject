package quick_serve.repos;

import quick_serve.domain.ProviderService;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long> {

    ProviderService findFirstByProviderId(Long id);

    ProviderService findFirstByServiceId(Long id);

}
