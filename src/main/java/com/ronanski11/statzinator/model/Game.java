package com.ronanski11.statzinator.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "game")
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "game_teams",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "game")
    private Stats stats;
}