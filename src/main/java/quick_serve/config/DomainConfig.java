package quick_serve.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.bootify.quick_serve.domain")
@EnableJpaRepositories("io.bootify.quick_serve.repos")
@EnableTransactionManagement
public class DomainConfig {
}
