package com.ronanski11.statzinator.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronanski11.statzinator.dto.TeamDto;
import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Team;
import com.ronanski11.statzinator.repository.PlayerRepository;
import com.ronanski11.statzinator.repository.TeamRepository;

@Service
public class TeamService {

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	PlayerService playerService;

	public List<Team> getAllTeams() {
		return teamRepository.findAll();
	}

	public Team getTeamById(int id) {
		return teamRepository.findById(id).get();
	}

	public Team saveNewTeam(TeamDto teamDto) {
		Team team = new Team();
		List<Integer> playerIds = teamDto.getPlayerIds();
		team.setCoach(teamDto.getCoach());
		team.setName(teamDto.getName());
		Team savedTeam = teamRepository.save(team);
		playerIds.forEach(id -> playerService.updatePlayerTeam(savedTeam.getId(), id));
		return savedTeam;
	}

	public Team updateTeam(TeamDto teamDto, int id) {
		return teamRepository.findById(id).map(team -> {
			if (teamDto.getPlayerIds() != null) {
				List<Integer> playerIds = teamDto.getPlayerIds();
				team.getPlayers().forEach(player -> {
					if (!playerIds.contains(player.getId())) {
						player.setTeam(null);
						playerRepository.save(player);
					}
				});
				team.setPlayers(playerIds.stream().map(playerRepository::findById).filter(java.util.Optional::isPresent)
						.map(java.util.Optional::get).collect(Collectors.toList()));
				playerIds.forEach(playerId -> playerService.updatePlayerTeam(team.getId(), playerId));
			}
			if (teamDto.getCoach() != null) {
				team.setCoach(teamDto.getCoach());
			}
			if (teamDto.getName() != null) {
				team.setName(teamDto.getName());
			}
			return teamRepository.save(team);
		}).orElseThrow(() -> new IllegalArgumentException("Team not found"));
	}

	public MessageResponse deleteTeam(Integer id) {
		Team team = teamRepository.findById(id).get();
		team.getPlayers().forEach(player -> {
			player.setTeam(null);
			playerRepository.save(player);
		});
		teamRepository.deleteById(id);
		return new MessageResponse(String.format("Team %s deleted", id));
	}

}
