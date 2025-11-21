package com.geo.localization.Services;

import org.springframework.stereotype.Service;

import com.geo.localization.Model.Point;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import java.awt.geom.Point2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class GeoLocalizationService {
    private DatabaseReader reader;

    // public GeoLocalizationService() throws IOException {
    //     try{
    //     // InputStream inputStream = getClass().getClassLoader().getResourceAsStream("GeoLite2-City.mmdb");
    //     File database = new File("GeoLite2-City.mmdb");

    //     this.reader = new DatabaseReader.Builder(database).build();
    //     System.out.println("FILE FOUND");
    //     }
    //     catch(FileNotFoundException ex)
    //     {
    //         System.out.println("FILE NOT FOUND");
    //         System.out.println(ex.getLocalizedMessage());
    //     }
        
    // }
     public GeoLocalizationService() throws IOException {
        try {
            // Get the input stream from the classpath
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("GeoLite2-City.mmdb");
            
            // Create a temporary file to hold the MMDB data
            Path tempFile = Files.createTempFile("geoip-temp", ".mmdb");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            
            // Create the DatabaseReader with the temporary file
            File database = tempFile.toFile();
            this.reader = new DatabaseReader.Builder(database).build();
            
            // Clean up the input stream
            inputStream.close();
            
            System.out.println("Database loaded successfully");
        } catch (IOException ex) {
            throw new RuntimeException("Failed to initialize GeoIP database", ex);
        }
    }

    public CityResponse getLocation(String ipAddress) throws IOException, GeoIp2Exception{
        InetAddress ip = InetAddress.getByName(ipAddress);
        return reader.city(ip);
    }

    
    
}
