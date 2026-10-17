package jp.gr.java_conf.stardiopside.jnotes.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "postal_codes")
public class PostalCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 5)
    private String localGovernmentCode;

    @Size(max = 5)
    private String oldPostalCode;

    @Size(max = 7)
    private String postalCode;

    private String kanaPrefectureName;

    private String kanaMunicipalityName;

    private String kanaAreaName;

    private String kanjiPrefectureName;

    private String kanjiMunicipalityName;

    private String kanjiAreaName;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(updatable = false)
    @CreatedBy
    private @Nullable String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private @Nullable String updatedBy;

    @Version
    private Integer version;

}
