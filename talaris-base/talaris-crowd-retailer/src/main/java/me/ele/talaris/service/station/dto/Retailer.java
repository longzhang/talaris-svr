package me.ele.talaris.service.station.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.sql.Timestamp;

/**
 * Created by Daniel on 15/5/13.
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class Retailer {
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private Double longtitude;
    private Double latitude;
    private Integer ownerId;
    private String ownerName;
    private Long ownerMobile;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
