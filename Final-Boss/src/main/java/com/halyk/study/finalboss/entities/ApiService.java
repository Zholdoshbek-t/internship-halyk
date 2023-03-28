package com.halyk.study.finalboss.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`api_service`")
public class ApiService {
    // select chats api_services -> Chat.getApiServices()
    // select api_services urls -> ApiService.getUrls()
    //
    // select from error_message em
    // inner join url u
    // on em.url_id = u.id
    // inner join api_service as
    // on u.api_service_id = as.id
    // where em.created_at >= :from
    // and em.created_at <= :till
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "apiService")
    private List<Url> urls = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL,
            mappedBy = "apiServices")
    private List<Chat> chats;
}