package com.misaka.service.vo;

import java.util.List;
import lombok.Data;

@Data
public class PlaybackDataVO {

  List<PlayerUpdatePositionEvents> playerUpdatePositionEvents;

  @Data
  public static class PlayerUpdatePositionEvents {
    private int time;
    private int x;
    private int y;
  }

}
