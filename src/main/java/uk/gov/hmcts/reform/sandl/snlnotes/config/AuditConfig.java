package uk.gov.hmcts.reform.sandl.snlnotes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class AuditConfig implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
