package com.ronanski11.statzinator.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ronanski11.statzinator.model.GameStatus;
import com.ronanski11.statzinator.model.Stats;
import lombok.Data;

@Data
public class GameDto {

    private List<Integer> teamIds;

    private LocalDateTime time;

    private GameStatus status;

    private Stats stats;
	
}
