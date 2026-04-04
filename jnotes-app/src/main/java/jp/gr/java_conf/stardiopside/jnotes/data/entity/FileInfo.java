package jp.gr.java_conf.stardiopside.jnotes.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@ToString(exclude = "fileData")
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class FileInfo implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private @Nullable String contentType;

    private @Nullable String originalContentType;

    private Integer length;

    private String hashValue;

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

    @OneToOne(mappedBy = "fileInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileData fileData;

    @Override
    public FileInfo clone() {
        try {
            return (FileInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
