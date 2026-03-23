package quick_serve.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "\"role\"")
    private String role;

    @Column(nullable = false)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "longtext")
    private String address;

    @Column(length = 500)
    private String profilePicture;

    @Column
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "provider")
    private Set<ProviderService> providerProviderServices = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Booking> customerBookings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<SupportTicket> userSupportTickets = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Notification> userNotifications = new HashSet<>();

}
