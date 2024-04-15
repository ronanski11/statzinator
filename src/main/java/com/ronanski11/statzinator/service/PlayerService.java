package com.ronanski11.statzinator.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.repository.PlayerRepository;
import com.ronanski11.statzinator.repository.TeamRepository;

@Service
public class PlayerService {

	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	TeamRepository teamRepository;

	public List<Player> getAllPlayers() {
		return playerRepository.findAll();
	}

	public Player getPlayerById(Integer id) {
		return playerRepository.findById(id).get();
	}

	public List<Player> getPlayerByBirthday(LocalDate date, LocalDate startRange, LocalDate endRange) {
		return (startRange == null || endRange == null) // Checks if one of or both ranges is null
				? playerRepository.findByDateOfBirth(date)
				: playerRepository.findByDateOfBirthBetween(startRange, endRange);
	}

	public List<Player> getPlayerByName(String name) {
		return playerRepository.findByNameLike(name);
	}

	public Player saveNewPlayer(Player player, Integer teamId) {
	    player.setTeam(teamRepository.findById(teamId).get());
	    return playerRepository.save(player);
	}


	public Player updatePlayer(Player player, Integer id) {
        return playerRepository.findById(id)
                .map(playerOrig -> {
                	playerOrig.setFullName(player.getFullName());
                	playerOrig.setHeight(player.getHeight());
                	playerOrig.setWeight(player.getWeight());
                	playerOrig.setDateOfBirth(player.getDateOfBirth());
                	playerOrig.setTeam(player.getTeam());
                    return playerRepository.save(playerOrig);
                })
                .orElseGet(() -> playerRepository.save(player));
	}

	public MessageResponse deletePlayer(Integer id) {
		playerRepository.deleteById(id);
		return new MessageResponse(String.format("Player %s deleted", id));
	}

}
