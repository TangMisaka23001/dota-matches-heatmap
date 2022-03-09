package com.misaka.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchVO {

  private String id;
  private boolean didRadiantWin;
  private int durationSeconds;
  private List<PlayersVO> players;


}
