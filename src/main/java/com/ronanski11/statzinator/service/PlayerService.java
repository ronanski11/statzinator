package com.ronanski11.statzinator.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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

	public Player saveNewPlayer(Player player, int teamId) {
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
	
	public List<Player> searchForPlayer(String playerName, LocalDate date, LocalDate startRange,
	        LocalDate endRange, String teamName, Integer teamId) {
	    Specification<Player> spec = Specification.where(null);

	    if (playerName != null && !playerName.isEmpty()) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	                criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + playerName.toLowerCase() + "%"));
	    }

	    if (date != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	                criteriaBuilder.equal(root.get("dateOfBirth"), date));
	    }

	    if (startRange != null && endRange != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	                criteriaBuilder.between(root.get("dateOfBirth"), startRange, endRange));
	    }

	    if (teamName != null && !teamName.isEmpty()) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	                criteriaBuilder.like(criteriaBuilder.lower(root.get("team").get("name")), "%" + teamName.toLowerCase() + "%"));
	    }

	    if (teamId != null) {
	        spec = spec.and((root, query, criteriaBuilder) ->
	                criteriaBuilder.equal(root.get("team").get("id"), teamId));
	    }

	    return playerRepository.findAll(spec);
	}

	public void updatePlayerTeam(int teamId, Integer id) {
		Player player = playerRepository.findById(id).get();
        player.setTeam(teamRepository.findById(teamId).get());
        playerRepository.save(player);
	}

}
