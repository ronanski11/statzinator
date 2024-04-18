package com.ronanski11.statzinator.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.model.Team;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer>, JpaSpecificationExecutor<Player> {

	List<Player> findByDateOfBirthBetween(LocalDate startRange, LocalDate endRange);

	List<Player> findByDateOfBirth(LocalDate date);

	@Query("SELECT p FROM Player p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Player> findByNameLike(@Param("name") String name);

    @Query("SELECT p FROM Player p WHERE LOWER(p.team.name) LIKE LOWER(CONCAT('%', :teamName, '%'))")
    List<Player> findByTeamNameLike(@Param("teamName") String teamName);

	List<Player> findByTeam(Optional<Team> byId);

}
