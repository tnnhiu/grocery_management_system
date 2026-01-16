package com.tnnhiu.grocery_management_server.modules.identity.entity;

import com.tnnhiu.grocery_management_server.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(exclude = {"role", "password"})
public class User extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[a-z0-9]+$")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @NotBlank()
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @PrePersist
    @PreUpdate
    public void normalizeUsername() {
        if (this.username != null) {
            this.username = this.username.toLowerCase();
        }
    }
}
