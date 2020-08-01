package usrun.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import usrun.config.StatisticConfig;
import usrun.dto.TeamDTO;
import usrun.dto.TeamStatDTO;
import usrun.dto.UserActivityStatDTO;
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

  private Scheduler queryDataScheduler;

  @Data
  @AllArgsConstructor
  class StatisticData {

    private List<TeamDTO> teams;
    private Map<Long, List<TeamMember>> teamMembersByTeam;
    private Map<Long, UserActivityStatDTO> userActivityStats;
  }

  public StatisticService(
      TeamRepository teamRepository, TeamMemberRepository teamMemberRepository,
      UserActivityRepository userActivityRepository, CacheClient cacheClient,
      StatisticConfig statisticConfig) {
    this.teamRepository = teamRepository;
    this.teamMemberRepository = teamMemberRepository;
    this.userActivityRepository = userActivityRepository;
    this.cacheClient = cacheClient;
    this.teamLeaderBoardScheduler = Executors.newScheduledThreadPool(1);
    this.queryDataScheduler = Schedulers.newBoundedElastic(10, 100, "query-data-scheduler");
    this.statisticConfig = statisticConfig;
  }

  public void start() {
    teamLeaderBoardScheduler
        .scheduleAtFixedRate(this::buildTeamLeaderBoard, 0,
            statisticConfig.getTeamIntervalInSeconds(), TimeUnit.SECONDS);
  }

  private void buildTeamLeaderBoard() {
    long startTime = System.currentTimeMillis();
    StatisticData statisticData = getData();
    log.info("get data {} ms", System.currentTimeMillis() - startTime);
    List<TeamDTO> teams = statisticData.getTeams();
    Map<Long, List<TeamMember>> teamMembersByTeam = statisticData.getTeamMembersByTeam();
    Map<Long, UserActivityStatDTO> userActivityStats = statisticData.getUserActivityStats();
    long firstDayOfWeek = getFirstDayOfWeek();

    long statisticStartTime = System.currentTimeMillis();
    List<TeamStatDTO> teamStats = teams.stream().map(team -> {
      long totalDistance = 0;
      long maxTime = 0;
      long maxDistance = 0;
      int totalActivity = 0;
      int memInWeek = 0;
      int totalMember = 0;

      List<TeamMember> teamMembers = teamMembersByTeam.get(team.getTeamId());
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

      TeamStatDTO teamStat = new TeamStatDTO(team.getTeamId(), team.getTeamName(),
          team.getThumbnail(),
          totalDistance, maxTime, maxDistance, memInWeek, totalMember, totalActivity);
      return teamStat;
    }).sorted((a, b) -> Long.compare(b.getTotalDistance(), a.getTotalDistance()))
        .collect(Collectors.toList());
    log.info("statistic {} ms", System.currentTimeMillis() - statisticStartTime);

    long cacheTime = System.currentTimeMillis();
    cacheClient.setTeamStat(teamStats);
    log.info("cache {} ms", System.currentTimeMillis() - cacheTime);
    log.info("finish build teamLeaderBoard in {} ms", System.currentTimeMillis() - startTime);
  }

  private void buildEventLeaderBoard() {

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

  public StatisticData getData() {
    Mono<List<TeamDTO>> teamsMono = getTeams();
    Mono<Map<Long, List<TeamMember>>> teamMembersMono = getTeamMembersByTeam();
    Mono<Map<Long, UserActivityStatDTO>> userActivityStatsMono = getUserActivityStats();

    return Mono.zip(teamsMono, teamMembersMono, userActivityStatsMono)
        .flatMap(
            tuple -> Mono.just(new StatisticData(tuple.getT1(), tuple.getT2(), tuple.getT3())))
        .block();

  }

  private Mono<Map<Long, UserActivityStatDTO>> getUserActivityStats() {
    return Mono.fromCallable(() -> userActivityRepository.getStat().stream()
        .collect(Collectors.toMap(UserActivityStatDTO::getUserId,
            Function.identity())))
        .publishOn(queryDataScheduler);
  }

  private Mono<Map<Long, List<TeamMember>>> getTeamMembersByTeam() {
    return Mono.fromCallable(() -> teamMemberRepository
        .getAllByLessEqualTeamMemberType(TeamMemberType.MEMBER).stream()
        .collect(Collectors.groupingBy(TeamMember::getTeamId)))
        .publishOn(queryDataScheduler);
  }

  private Mono<List<TeamDTO>> getTeams() {
    return Mono.fromCallable(() -> teamRepository.findAllTeamDTO())
        .publishOn(queryDataScheduler);
  }
}
