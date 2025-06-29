package com.web.primefacesexampletest.database;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Request {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NonNull
    @Column(nullable = false)
    Float x;

    @NonNull
    @Column(nullable = false)
    Float y;

    @NonNull
    @Column(nullable = false)
    Float r;

    @NonNull
    @Column
    Boolean hit;

    @Builder.Default
    @NonNull
    @Column(nullable = false)
    Timestamp startTime = Timestamp.valueOf(LocalDateTime.now());

    @NonNull
    @Column(nullable = false)
    Timestamp endTime;

}
