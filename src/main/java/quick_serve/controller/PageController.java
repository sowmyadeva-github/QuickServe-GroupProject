package quick_serve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Public pages
    @GetMapping({"/", "/index"})
    public String home() {
        return "index";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    // Customer pages
    @GetMapping("/customer-dashboard")
    public String customerDashboard() {
        return "customer-dashboard";
    }

    @GetMapping("/booking")
    public String booking() {
        return "booking";
    }

    @GetMapping("/booking-history")
    public String bookingHistory() {
        return "booking-history";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    // Provider pages
    @GetMapping("/provider-dashboard")
    public String providerDashboard() {
        return "provider-dashboard";
    }

    @GetMapping("/provider-services")
    public String providerServices() {
        return "provider-services";
    }

    @GetMapping("/provider-bookings")
    public String providerBookings() {
        return "provider-bookings";
    }

    @GetMapping("/provider-earnings")
    public String providerEarnings() {
        return "provider-earnings";
    }

    // Admin pages
    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/admin-users")
    public String adminUsers() {
        return "admin-users";
    }

    @GetMapping("/admin-providers")
    public String adminProviders() {
        return "admin-providers";
    }

    @GetMapping("/admin-bookings")
    public String adminBookings() {
        return "admin-bookings";
    }

    @GetMapping("/admin-analytics")
    public String adminAnalytics() {
        return "admin-analytics";
    }
}