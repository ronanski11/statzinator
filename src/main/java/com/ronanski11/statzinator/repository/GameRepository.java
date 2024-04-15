package com.ronanski11.statzinator.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ronanski11.statzinator.model.Game;
import com.ronanski11.statzinator.model.GameStatus;

public interface GameRepository extends JpaRepository<Game, Integer> {
	
    List<Game> findByTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
    
    @Query("SELECT g FROM Game g JOIN g.teams t WHERE t.id = :teamId")
    List<Game> findByTeamId(int teamId);
    
    @Query("SELECT g FROM Game g JOIN g.teams t WHERE t.id = :teamId AND g.status = :status")
    List<Game> findByTeamIdAndStatus(int teamId, GameStatus status);

	List<Game> findByStatus(GameStatus status);
    
}
