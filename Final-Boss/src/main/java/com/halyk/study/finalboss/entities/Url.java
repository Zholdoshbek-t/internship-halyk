package com.halyk.study.finalboss.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`url`")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;

    @ManyToOne
    @JoinColumn(name = "api_service_id")
    private ApiService apiService;

    @ManyToOne
    @JoinColumn(name = "http_method_id")
    private HttpMethod httpMethod;
}
