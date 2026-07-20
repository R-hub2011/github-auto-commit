package com.rohith.github_auto_commit.scheduler;

import com.rohith.github_auto_commit.service.GitCommitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CommitScheduler {

    private final GitCommitService gitCommitService;

    // ANCHOR: Hardcode the exact Sunday you want the grid pattern to start
    private static final LocalDate START_DATE = LocalDate.of(2026, 7, 19);

    public CommitScheduler(GitCommitService gitCommitService){
        this.gitCommitService = gitCommitService;
    }

    private static final int[][] pattern = {
            {6,6,6,6,1,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,6,6,6,6,1,1,1,1,6,6,6,1,1,1,1,1,6,1,1,1,1,1,6,1}, // Sun
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,6,1,1,1,6,1,1,1,6,1,1,1,6,6,1,1,1,1,6,6,1}, // Mon
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,1,1,1,6,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1}, // Tue
            {6,6,6,6,1,1,1,6,6,6,1,6,6,6,6,1,1,6,1,1,1,6,1,6,6,6,6,1,1,6,6,6,6,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1}, // Wed
            {6,1,1,6,1,1,1,1,1,1,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1}, // Thu
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,6,1,6,1,1,1,1,1,1,6,1,1,1,6,1,1,1,1,6,1,1,1,1,1,6,1}, // Fri
            {6,1,1,1,6,1,1,1,1,1,1,6,1,1,1,6,1,1,6,6,6,1,1,6,6,6,6,1,1,6,6,6,6,6,1,1,1,6,6,6,1,1,1,1,6,6,6,1,1,1,6,6,6}  // Sat
    };

    @Scheduled(
            cron = "0  * *  * * *",
            zone = "America/New_York"
    )
    public void executeCommitJob() throws InterruptedException {

        LocalDate today = LocalDate.now();

        // Calculate exact absolute days since the fixed start date
        long days = java.time.temporal.ChronoUnit.DAYS.between(START_DATE, today);

        if(days < 0){
            System.out.println("Pattern not started yet. Starts on: " + START_DATE);
            return;
        }

        int week = (int) days / 7;

        // Since START_DATE is guaranteed to be a Sunday,
        // remainder 0 = Sunday, 1 = Monday, ..., 6 = Saturday.
        int day = (int) days % 7;

        if(week >= pattern[0].length){
            System.out.println("Pattern completed");
            return;
        }

        int commits = pattern[day][week];

        System.out.println("Week: " + week + " Day: " + day + " Commits: " + commits);

        for(int i = 1; i <= commits; i++){
            gitCommitService.createCommit(i);

            // Heads up: 300000ms is 5 minutes. 6 commits will take 25 minutes to finish execution loop.
            if (i < commits) {
                Thread.sleep(300000);
            }
        }
    }
}
