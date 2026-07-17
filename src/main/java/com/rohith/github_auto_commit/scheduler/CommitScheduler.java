package com.rohith.github_auto_commit.scheduler;


import com.rohith.github_auto_commit.service.GitCommitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
public class CommitScheduler {

    private final GitCommitService gitCommitService;

    public CommitScheduler(
            GitCommitService gitCommitService){
        this.gitCommitService = gitCommitService;
    }

    private static final int[][] pattern = {
            {6,6,6,6,1,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,6,6,6,6,1,1,1,1,6,6,6,1,1,1,1,1,6,1,1,1,1,1,6,1},
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,6,1,1,1,6,1,1,1,6,1,1,1,6,6,1,1,1,1,6,6,1},
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,6,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1},
            {6,6,6,6,1,1,1,6,6,6,1,6,6,6,6,1,1,6,1,1,1,6,1,6,6,6,6,1,1,6,6,6,6,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1},
            {6,1,1,6,1,1,1,1,1,1,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1},
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1},
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,6,1,1,6,6,6,1,1,6,6,6,6,1,1,6,6,6,6,6,1,1,1,6,6,6,1,1,1,1,6,6,6,1,1,1,6,6,6}
    };

  /*
       Runs every day at 5:30 PM EST
    */
    @Scheduled(
            cron = "0 30 17 * * *",
            zone = "America/New_York"
    )
    public void executeCommitJob()
            throws InterruptedException {

        LocalDate startDate =
                getNextSunday();

        LocalDate today =
                LocalDate.now();

        long days =
                java.time.temporal.ChronoUnit.DAYS
                        .between(
                                startDate,
                                today
                        );

        if(days < 0){
            System.out.println(
                    "Pattern not started yet"
            );
            return;
        }

        int week =
                (int) days / 7;

        int day =
                today.getDayOfWeek()
                        .getValue();

        /*
          Java:
          Monday=1
          Sunday=7

          Convert:
          Sunday=0
          Monday=1
        */
        if(day == 7){
            day = 0;
        }


        if(week >= pattern[0].length){

            System.out.println(
                    "Pattern completed"
            );
            return;
        }
        int commits =
                pattern[day][week];

        System.out.println("Week: "+ week + " Day: "+ day+" Commits: "+ commits);

        for(int i=1;i<=commits;i++){
            gitCommitService
                    .createCommit(i);
            /*
              Optional delay between commits
            */
            Thread.sleep(
                    300000
            );
        }
    }

    private LocalDate getNextSunday(){

        LocalDate today =
                LocalDate.now();

        return today.with(
                DayOfWeek.SUNDAY
        ).plusWeeks(1);
    }
}