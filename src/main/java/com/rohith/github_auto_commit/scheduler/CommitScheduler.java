package com.rohith.github_auto_commit.scheduler;




import com.rohith.github_auto_commit.service.GitCommitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class CommitScheduler {


    private final GitCommitService gitCommitService;


    public CommitScheduler(GitCommitService gitCommitService){

        this.gitCommitService = gitCommitService;

    }



    // Runs every day at 9 AM
    //@Scheduled(cron = "0 30 17 * * MON-FRI", zone = "America/New_York")
    @Scheduled(cron = "0 */1 * * * ?")
    public void executeCommitJob() throws InterruptedException {


        Random random = new Random();


        int commitCount = random.nextInt(4) + 2;


        System.out.println(
                "Today's commits: " + commitCount
        );


        for(int i=1;i<=commitCount;i++){

            gitCommitService.createCommit(i);
            Thread.sleep(5000);

        }

    }

}