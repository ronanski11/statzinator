package com.ronanski11.statzinator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ronanski11.statzinator.model.Stats;

public interface StatsRepository extends JpaRepository<Stats, Integer>{

}
