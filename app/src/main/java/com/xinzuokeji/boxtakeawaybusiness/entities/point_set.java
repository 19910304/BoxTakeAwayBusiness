package com.xinzuokeji.boxtakeawaybusiness.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/22.
 */

public class point_set implements Serializable {
    //     "lng": 106.4873671532,
//             "lat": 29.494108200216
    public String lng;
    public String lat;

//    public point_set() {
//    }

//    public point_set(double latitude, double longitude) {
//        lat = String.valueOf(latitude);
//        lng = String.valueOf(longitude);
//
//    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }


}
