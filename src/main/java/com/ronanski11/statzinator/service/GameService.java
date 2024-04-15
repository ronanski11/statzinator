package com.ronanski11.statzinator.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronanski11.statzinator.model.Game;
import com.ronanski11.statzinator.model.GameStatus;
import com.ronanski11.statzinator.repository.GameRepository;

@Service
public class GameService {

	@Autowired
	GameRepository gameRepository;
	
	public Game getById(Integer id) {
		return gameRepository.findById(id).get();
	}

	public Game saveNewGame(Game game) {
		return gameRepository.save(game);
	}
	
	public List<Game> getGamesByDate(LocalDate date) {
		return gameRepository.findByTimeBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));
	}

	public List<Game> getAllGamesByTeam(Integer teamId, GameStatus status) {
		return status == null ? gameRepository.findByTeamId(teamId) : gameRepository.findByTeamIdAndStatus(teamId, status);
	}

	public List<Game> getAllGames(GameStatus status) {
		return status == null ? gameRepository.findAll() : gameRepository.findByStatus(status);
	}

	

}
