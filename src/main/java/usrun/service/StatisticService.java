package usrun.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usrun.config.StatisticConfig;
import usrun.dto.TeamStatDTO;
import usrun.dto.UserActivityStatDTO;
import usrun.model.Team;
import usrun.model.junction.TeamMember;
import usrun.model.type.TeamMemberType;
import usrun.repository.TeamMemberRepository;
import usrun.repository.TeamRepository;
import usrun.repository.UserActivityRepository;
import usrun.utility.CacheClient;

/**
 * @author phuctt4
 */
@Slf4j
@Service
public class StatisticService {

  private final TeamRepository teamRepository;

  private final TeamMemberRepository teamMemberRepository;

  private final UserActivityRepository userActivityRepository;

  private final CacheClient cacheClient;

  private final StatisticConfig statisticConfig;

  private ScheduledExecutorService teamLeaderBoardScheduler;

  public StatisticService(
      TeamRepository teamRepository, TeamMemberRepository teamMemberRepository,
      UserActivityRepository userActivityRepository, CacheClient cacheClient,
      StatisticConfig statisticConfig) {
    this.teamRepository = teamRepository;
    this.teamMemberRepository = teamMemberRepository;
    this.userActivityRepository = userActivityRepository;
    this.cacheClient = cacheClient;
    this.teamLeaderBoardScheduler = Executors.newScheduledThreadPool(1);
    this.statisticConfig = statisticConfig;
  }

  public void start() {
    teamLeaderBoardScheduler
        .scheduleAtFixedRate(this::buildTeamLeaderBoard, 0, statisticConfig.getTeamIntervalInSeconds(), TimeUnit.SECONDS);
  }

  private void buildTeamLeaderBoard() {
    long startTime = System.currentTimeMillis();
    List<Team> teams = teamRepository.findAllTeam();
    Map<Long, List<TeamMember>> teamMembersByTeam = teamMemberRepository
        .getAllByLessEqualTeamMemberType(TeamMemberType.MEMBER).stream()
        .collect(Collectors.groupingBy(TeamMember::getTeamId));
    Map<Long, UserActivityStatDTO> userActivityStats = userActivityRepository.getStat().stream()
        .collect(Collectors.toMap(UserActivityStatDTO::getUserId,
            Function.identity()));
    long firstDayOfWeek = getFirstDayOfWeek();

    List<TeamStatDTO> teamStats = teams.stream().map(team -> {
      long totalDistance = 0;
      long maxTime = 0;
      long maxDistance = 0;
      int totalActivity = 0;
      int memInWeek = 0;
      int totalMember = 0;

      List<TeamMember> teamMembers = teamMembersByTeam.get(team.getId());
      if (teamMembers != null && !teamMembers.isEmpty()) {
        for (TeamMember teamMember : teamMembers) {
          long addDate = teamMember.getAddTime().getTime();
          if (addDate > firstDayOfWeek) {
            memInWeek++;
          }
          UserActivityStatDTO userActivityStat = userActivityStats.get(teamMember.getUserId());
          if (userActivityStat != null) {
            totalDistance += userActivityStat.getTotalDistance();
            maxTime = Math.max(maxTime, userActivityStat.getMaxTime());
            maxDistance = Math.max(maxDistance, userActivityStat.getMaxDistance());
            totalActivity += userActivityStat.getTotalUserAcitivity();
          }
        }
        totalMember = teamMembers.size();
      }

      TeamStatDTO teamStat = new TeamStatDTO(team.getId(), team.getTeamName(), team.getThumbnail(),
          totalDistance, maxTime, maxDistance, memInWeek, totalMember, totalActivity);
      return teamStat;
    }).sorted((a, b) -> Long.compare(b.getTotalDistance(), a.getTotalDistance()))
        .collect(Collectors.toList());

    cacheClient.setTeamStat(teamStats);
    log.info("finish build teamLeaderBoard in {} ms", System.currentTimeMillis() - startTime);
  }


  private long getFirstDayOfWeek() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.clear(Calendar.MINUTE);
    cal.clear(Calendar.SECOND);
    cal.clear(Calendar.MILLISECOND);
    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
    return cal.getTimeInMillis();
  }
}
