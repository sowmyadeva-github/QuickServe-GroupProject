package quick_serve.rest;

import quick_serve.model.ProviderServiceDTO;
import quick_serve.service.ProviderServiceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/providerServices", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProviderServiceResource {

    private final ProviderServiceService providerServiceService;

    public ProviderServiceResource(final ProviderServiceService providerServiceService) {
        this.providerServiceService = providerServiceService;
    }

    @GetMapping
    public ResponseEntity<List<ProviderServiceDTO>> getAllProviderServices() {
        return ResponseEntity.ok(providerServiceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderServiceDTO> getProviderService(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(providerServiceService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createProviderService(
            @RequestBody @Valid final ProviderServiceDTO providerServiceDTO) {
        final Long createdId = providerServiceService.create(providerServiceDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProviderService(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProviderServiceDTO providerServiceDTO) {
        providerServiceService.update(id, providerServiceDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProviderService(@PathVariable(name = "id") final Long id) {
        providerServiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
