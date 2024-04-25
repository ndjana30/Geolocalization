package com.geo.localization.Services;

import org.springframework.stereotype.Service;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeoLocalizationService {
    private DatabaseReader reader;

    public GeoLocalizationService() throws IOException {
        try{
        File database = new File("src/main/resources/GeoLite2-City.mmdb");
        this.reader = new DatabaseReader.Builder(database).build();
        System.out.println("FILE FOUND");
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("FILE NOT FOUND");
            System.out.println(ex.getLocalizedMessage());
        }
        
    }

    public CityResponse getLocation(String ipAddress) throws IOException, GeoIp2Exception{
        InetAddress ip = InetAddress.getByName(ipAddress);
        return reader.city(ip);
    }
    
}
