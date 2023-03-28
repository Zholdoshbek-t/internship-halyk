package com.halyk.study.finalboss.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`error_message`")
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int code;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "url_id"
    )
    private Url url;
}
