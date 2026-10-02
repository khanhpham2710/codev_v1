package entites;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEntity {

    private UUID id;
    private final Instant createdAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    protected BaseEntity(UUID id, Instant createdAt) {
        this.id = id != null ? id : UUID.randomUUID();
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public UUID getId() {
        return id;
    }

    protected void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
