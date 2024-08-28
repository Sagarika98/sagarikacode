package com.example.DestinationHashGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineApp implements CommandLineRunner {

    @Autowired
    private DestinationService destinationService;

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN> <jsonFilePath>");
            return;
        }

        String prn = args[0].trim().toLowerCase();
        String jsonFilePath = args[1];

        try {
            String result = destinationService.processFile(prn, jsonFilePath);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
