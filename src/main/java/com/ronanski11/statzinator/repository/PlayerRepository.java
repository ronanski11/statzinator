package com.ronanski11.statzinator.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ronanski11.statzinator.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

	List<Player> findByDateOfBirthBetween(LocalDate startRange, LocalDate endRange);

	List<Player> findByDateOfBirth(LocalDate date);

	@Query("SELECT p FROM Player p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Player> findByNameLike(@Param("name") String name);


}
