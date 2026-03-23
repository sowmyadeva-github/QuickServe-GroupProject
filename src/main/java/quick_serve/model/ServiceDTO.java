package quick_serve.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ServiceDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 100)
    private String category;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal basePrice;

    @Size(max = 500)
    private String imageUrl;

}
