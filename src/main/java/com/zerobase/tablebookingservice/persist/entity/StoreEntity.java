package com.zerobase.tablebookingservice.persist.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "STORE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String address;

    private String description;

    @ManyToOne
    private UserEntity userEntity;

    @OneToMany
    private List<BookingEntity> bookings;

    @CreatedDate
    private LocalDateTime regiAt;

    @LastModifiedDate
    private LocalDateTime modiAt;
}
