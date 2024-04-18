package com.ronanski11.statzinator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronanski11.statzinator.dto.StatsDto;
import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Stats;
import com.ronanski11.statzinator.repository.GameRepository;
import com.ronanski11.statzinator.repository.StatsRepository;

@Service
public class StatsService {

	@Autowired
	StatsRepository statsRepository;

	@Autowired
	GameRepository gameRepository;

	public List<Stats> getAllStats() {
		return statsRepository.findAll();
	}

	public Stats getStatsById(int id) {
		return statsRepository.findById(id).get();
	}

	public Stats saveNewStats(StatsDto statsDto) {
		Stats stats = new Stats();
		stats.setGame(gameRepository.findById(statsDto.getGameId()).get());
		stats.setStats(statsDto.getStats());
		return statsRepository.save(stats);
	}

	public Stats updateStats(StatsDto statsDto, int id) {
		return statsRepository.findById(id).map(stats -> {
			if (statsDto.getGameId() != null) {
				stats.setGame(gameRepository.findById(statsDto.getGameId()).get());
			}
			if (statsDto.getStats() != null) {
				stats.setStats(statsDto.getStats());
			}

			return statsRepository.save(stats);
		}).orElseThrow(() -> new IllegalArgumentException("Game not found"));
	}
	
    public MessageResponse deleteStats(Integer id) {
		statsRepository.deleteById(id);
		return new MessageResponse(String.format("Stats %s deleted", id));
	}

}
