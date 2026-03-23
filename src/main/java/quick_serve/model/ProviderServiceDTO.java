package quick_serve.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProviderServiceDTO {

    private Long id;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private String description;

    private String availability;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @NotNull
    private Long provider;

    @NotNull
    private Long service;

}
