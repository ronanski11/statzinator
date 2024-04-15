package com.ronanski11.statzinator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ronanski11.statzinator.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

}
