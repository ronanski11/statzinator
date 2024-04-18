package com.ronanski11.statzinator.dto;

import java.util.Map;

import lombok.Data;

@Data
public class StatsDto {
	
    private Map<String, String> stats;

    private Integer gameId;

}
