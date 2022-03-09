package com.misaka.service.vo;

import lombok.Data;

@Data
public class PlayersVO {

  private String matchId;
  private int playerSlot;
  private int heroId;
  private String steamAccountId;
  private boolean isRadiant;
  private PlaybackDataVO playbackData;
  private SteamAccount steamAccount;

  @Data
  public static class SteamAccount {
    private String id;
    private String steamId;
    private String realName;
    private String name;
  }

}
