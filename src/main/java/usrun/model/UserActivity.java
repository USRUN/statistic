/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usrun.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author huyna
 */
@Data
public class UserActivity {

  private long userActivityId;
  private long userId;
  private Date createTime;
  private long totalDistance;
  private long totalTime;
  private long totalStep;
  private double avgPace;
  private double avgHeart;
  private double maxHeart;
  private int calories;
  private double elevGain;
  private double elevMax;
  private List<String> photos;
  private String title;
  private String description;
  private long totalLove;
  private int totalComment;
  private int totalShare;
  private boolean processed;
  private int deleted;
  private int privacy;

  public UserActivity(long userActivityId, long userId, Date createTime, long totalDistance,
      long totalTime, long totalStep, double avgPace, double avgHeart, double maxHeart,
      int calories, double elevGain, double elevMax, List<String> photos, String title,
      String description,
      int totalLove, int totalComment, int totalShare, boolean processed, int deleted,
      int privacy) {
    this.userActivityId = userActivityId;
    this.userId = userId;
    this.createTime = createTime;
    this.totalDistance = totalDistance;
    this.totalTime = totalTime;
    this.totalStep = totalStep;
    this.avgPace = avgPace;
    this.avgHeart = avgHeart;
    this.maxHeart = maxHeart;
    this.calories = calories;
    this.elevGain = elevGain;
    this.elevMax = elevMax;
    this.photos = photos;
    this.title = title;
    this.description = description;
    this.totalLove = totalLove;
    this.totalComment = totalComment;
    this.totalShare = totalShare;
    this.processed = processed;
    this.deleted = deleted;
    this.privacy = privacy;
  }

  public UserActivity(long userActivityId, long userId, long totalDistance, long totalTime,
      double avgPace, Date createTime) {
    this.userActivityId = userActivityId;
    this.userId = userId;
    this.totalDistance = totalDistance;
    this.totalTime = totalTime;
    this.avgPace = avgPace;
    this.createTime = createTime;
  }

  public UserActivity() {
    this.totalDistance = 0l;
    this.totalTime = 0l;
    this.totalStep = 0l;
    this.avgPace = 0d;
    this.avgHeart = 0d;
    this.maxHeart = 0d;
    this.calories = 0;
    this.elevGain = 0d;
    this.elevMax = 0d;
    this.totalLove = 0;
    this.totalComment = 0;
    this.totalShare = 0;
    this.photos = Collections.emptyList();
  }
}
