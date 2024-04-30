package com.geo.localization.Rest;

import java.io.IOException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.geo.localization.Model.Point;
import com.geo.localization.Services.GeoLocalizationService;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import jakarta.servlet.http.HttpServletRequest;


@RestController()
public class GeoLocalizationEndpoints {
    @Autowired
    public HttpServletRequest request;
    @Autowired
    private GeoLocalizationService geoLocalizationService;
    
    @GetMapping("user/{ip1}/{ip2}")
    //Method to look for the latitude of two different points using their ipAdresses
    public Object adressParams(@PathVariable String ip1, @PathVariable String ip2) throws IOException, GeoIp2Exception
    {
        CityResponse response1 = geoLocalizationService.getLocation(ip1);
        CityResponse response2 = geoLocalizationService.getLocation(ip2);
        Country country1 = response1.getCountry();
        City city1 = response1.getCity();
        Location location1 = response1.getLocation();

        Country country2 = response2.getCountry();
        City city2 = response2.getCity();
        Location location2 = response2.getLocation();

        //NOW I ATTRIBUTE TO THE REQUIRED POINTS, THE LONGITUDES AND LATITUDES I GOT
        Point A = new Point(country1.getName()+"\t" + city1.getName(), location1.getLongitude(), location1.getLatitude(), ip1);
        Point B = new Point(country2.getName()+"\t" + city2.getName(), location2.getLongitude(), location2.getLatitude(), ip2);

        // NOW I CALCULATE THE DISTANCE BETWEEN POINT A AND B
        double lat1Rad = Math.toRadians(A.getLatitude());
        double lon1Rad = Math.toRadians(A.getLongitude());
        double lat2Rad = Math.toRadians(B.getLatitude());
        double lon2Rad = Math.toRadians(B.getLongitude());

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = 6371 * c; 
        // c is the earth's radius in kilo metres

        return "Distance between \t"+ A.getName() + "\t" +"and \t" + B.getName()+ "\t  is"+ distance+"\t kilo metres";
    }

    

    @GetMapping("/user/ip")
    public String getUserIP() {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
    // @GetMapping("user")
    // public String getLocation(@RequestParam String ipAddress) throws IOException, GeoIp2Exception {
    //     CityResponse response = geoLocalizationService.getLocation(ipAddress);
    //     Country country = response.getCountry();
    //     City city = response.getCity();
    //     Location location = response.getLocation();

    //     return "Country: " + country.getName() + ", City: " + city.getName() + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
    // }
}
