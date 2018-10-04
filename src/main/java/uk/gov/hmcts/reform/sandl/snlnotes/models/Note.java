package uk.gov.hmcts.reform.sandl.snlnotes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Entity
@Data
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Note {
    @Id
    private UUID id;

    private String content;

    private String type;

    private UUID entityId;

    private String entityType;

    @CreatedDate
    @Column(updatable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    private String modifiedBy;
}
