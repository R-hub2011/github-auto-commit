package com.rohith.github_auto_commit.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;


@Service
public class GitCommitService {


    @Value("${github.repo.path}")
    private String repoPath;

    public void createCommit(int number) {
       // System.out.println("Repository Path: " + repoPath);
        try {
            // Create/change file
            String file =
                    repoPath + "/commit.txt";


            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(file,true));


            writer.write(
                    "Commit number "
                            + number
                            + " created at "
                            + LocalDateTime.now()
            );


            writer.newLine();

            writer.close();

            System.out.println("File updated success");

            executeCommand(
                    "git add ."
            );


            executeCommand(
                    "git commit -m \"Automatic commit "
                            + number
                            + "\""
            );


            executeCommand(
                    "git push origin main"
            );


            System.out.println(
                    "Commit pushed successfully"
            );


        }
        catch(Exception e){

            e.printStackTrace();

        }

    }




    private void executeCommand(String command) throws Exception {

        String os = System.getProperty("os.name").toLowerCase();

        ProcessBuilder builder = new ProcessBuilder();

        if (os.contains("win")) {
        System.out.println("executeCommand os.contains = 'win'");
            // Windows
            builder.command(
                    "cmd",
                    "/c",
                    "cd /d " + repoPath + " && " + command
            );

        } else {

            // Linux
            System.out.println("executeCommand os.contains = 'Linux'");
            builder.command(
                    "bash",
                    "-c",
                    "cd " + repoPath + " && " + command
            );
        }


        Process process = builder.start();

        process.waitFor();


    }


}
