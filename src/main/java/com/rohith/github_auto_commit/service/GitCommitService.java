package com.rohith.github_auto_commit.service;


import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;


@Service
public class GitCommitService {


    private final String repoPath =
            "C:\\Users\\rohit\\WorkPlace\\eclipseProjects\\github-auto-commit";

    public void createCommit(int number){


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

            // Windows
            builder.command(
                    "cmd",
                    "/c",
                    "cd /d " + repoPath + " && " + command
            );

        } else {

            // Linux
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