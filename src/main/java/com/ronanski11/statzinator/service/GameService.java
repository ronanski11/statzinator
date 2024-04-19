package com.ronanski11.statzinator.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronanski11.statzinator.dto.GameDto;
import com.ronanski11.statzinator.model.Game;
import com.ronanski11.statzinator.model.GameStatus;
import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Team;
import com.ronanski11.statzinator.repository.GameRepository;
import com.ronanski11.statzinator.repository.TeamRepository;

@Service
public class GameService {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	StatsService statsService;

	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public Game getById(Integer id) {
		return gameRepository.findById(id).get();
	}

	public Game saveNewGame(GameDto gameDto) {
		Game game = new Game();
		
		game.setTeams(gameDto.getTeamIds().stream()
                .map(teamRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toList()));
		game.setStatus(gameDto.getStatus());
		game.setTime(gameDto.getTime());
		
		return gameRepository.save(game);
	}
	
    public Game updateGame(int id, GameDto gameDto) {
        return gameRepository.findById(id)
                .map(game -> {
                	if (gameDto.getTeamIds() != null) {
                        List<Team> teams = new ArrayList<>();
                        for (int teamId : gameDto.getTeamIds()) {
                            teams.add(teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team not found")));
                        }
                        game.setTeams(teams);
                    }                    if (gameDto.getStatus() != null) game.setStatus(gameDto.getStatus());
                    if (gameDto.getTime() != null) game.setTime(gameDto.getTime());
                    return gameRepository.save(game);
                })
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
    }
    
    public MessageResponse deleteGame(Integer id) {
    	statsService.deleteStats(gameRepository.findById(id).get().getStats().getId());
		gameRepository.deleteById(id);
		return new MessageResponse(String.format("Game %s deleted. The associated stats were also deleted.", id));
	}

	public List<Game> getGamesByDate(LocalDate date) {
		return gameRepository.findByTimeBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));
	}

	public List<Game> getAllGamesByTeam(Integer teamId, GameStatus status) {
		return status == null ? gameRepository.findByTeamId(teamId)
				: gameRepository.findByTeamIdAndStatus(teamId, status);
	}

	public List<Game> getAllGames(GameStatus status) {
		return status == null ? gameRepository.findAll() : gameRepository.findByStatus(status);
	}

}
