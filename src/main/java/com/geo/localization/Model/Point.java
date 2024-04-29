package com.geo.localization.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    private String name;
    private double longitude;
    private double latitude;
    private String ipAdress;

}
