package com.example.DestinationHashGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;

@Service
public class DestinationService {
    
    // Traverse the JSON to find the first "destination" key
    public String findDestination(JsonNode rootNode) {
        Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            if (field.getKey().equals("destination")) {
                return field.getValue().asText();
            }
            if (field.getValue().isObject()) {
                String result = findDestination(field.getValue());
                if (result != null) {
                    return result;
                }
            }
            if (field.getValue().isArray()) {
                for (JsonNode arrayItem : field.getValue()) {
                    if (arrayItem.isObject()) {
                        String result = findDestination(arrayItem);
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    // Generate a random 8-character alphanumeric string
    public String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 8; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // Generate the MD5 hash
    public String generateMD5Hash(String prn, String destinationValue, String randomString) {
        String toHash = prn + destinationValue + randomString;
        return DigestUtils.md5Hex(toHash);
    }

    // Main logic to read the JSON file, generate the hash, and format the output
    public String processFile(String prn, String jsonFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(jsonFilePath));
        
        System.out.println("JSON File Loaded Successfully.");
        
        String destinationValue = findDestination(rootNode);
        if (destinationValue == null) {
            throw new RuntimeException("No 'destination' key found in JSON");
        }
        
        // System.out.println("Destination Value: " + destinationValue);
        
        String randomString = generateRandomString();
        // System.out.println("Generated Random String: " + randomString);
        
        String hash = generateMD5Hash(prn, destinationValue, randomString);
        // System.out.println("Generated Hash: " + hash);
        
        return hash + ";" + randomString;
    }
}
