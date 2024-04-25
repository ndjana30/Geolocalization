package com.geo.localization.Rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geo.localization.Services.GeoLocalizationService;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;

@RestController()
public class GeoLocalizationEndpoints {
    @Autowired
    private GeoLocalizationService geoLocalizationService;

    @GetMapping("user")
    public String getLocation(@RequestParam String ipAddress) throws IOException, GeoIp2Exception {
        CityResponse response = geoLocalizationService.getLocation(ipAddress);
        Country country = response.getCountry();
        City city = response.getCity();
        Location location = response.getLocation();

        return "Country: " + country.getName() + ", City: " + city.getName() + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
    }
}
