/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usrun.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author huyna3
 */
@Getter
@Setter
public class TeamStatDTO {

  private long teamId;
  private String teamName;
  private String avatar;
  private long totalDistance;
  private long maxTime;
  private long maxDistance;
  private long memInWeek;
  private int totalMember;
  private long totalActivity;

  public TeamStatDTO(long teamId, String teamName, String avatar, long totalDistance, long maxTime,
      long maxDistance, long memInWeek, int totalMember, long totalActivity) {
    this.teamId = teamId;
    this.avatar = avatar;
    this.teamName = teamName;
    this.totalDistance = totalDistance;
    this.maxTime = maxTime;
    this.maxDistance = maxDistance;
    this.memInWeek = memInWeek;
    this.totalMember = totalMember;
    this.totalActivity = totalActivity;
  }

  public TeamStatDTO() {

  }

}
