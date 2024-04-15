package com.ronanski11.statzinator.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stats")
@Data
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "stats_entries", joinColumns = {@JoinColumn(name = "stats_id")})
    @MapKeyColumn(name = "stat_key")
    @Column(name = "stat_value")
    private Map<String, String> stats;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;
}