package com.rohith.github_auto_commit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

@Service
public class GitCommitService {

    @Value("${github.repo.path}")
    private String repoPath;

    public void createCommit(int number) {

        try {

            String file = repoPath + "/commit.txt";

            BufferedWriter writer =
                    new BufferedWriter(
                            new FileWriter(file, true));

            writer.write(
                    "Commit number "
                            + number
                            + " created at "
                            + LocalDateTime.now()
            );

            writer.newLine();
            writer.close();

            System.out.println("File updated successfully");


            executeCommand("git add .");


            executeCommand(
                    "git commit -m \"Automatic commit "
                            + number
                            + "\""
            );


            executeCommand(
                    "git push origin main"
            );


            System.out.println("Commit pushed successfully");


        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private void executeCommand(String command) throws Exception {

        System.out.println("Executing: " + command);

        ProcessBuilder builder =
                new ProcessBuilder(
                        "bash",
                        "-c",
                        "cd \"" + repoPath + "\" && " + command
                );


        builder.redirectErrorStream(true);

        Process process = builder.start();


        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()))) {


            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println(line);

            }
        }


        int exitCode = process.waitFor();


        System.out.println(
                "Command exit code: " + exitCode
        );


        if (exitCode != 0) {

            throw new RuntimeException(
                    "Command failed: " + command
            );
        }

    }

}
