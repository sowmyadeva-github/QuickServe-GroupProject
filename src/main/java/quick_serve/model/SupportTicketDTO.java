package quick_serve.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
public class SupportTicketDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    private String message;

    @NotNull
    @Size(max = 255)
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

}
