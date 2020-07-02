package com.example.hotel.vo;


import lombok.*;

/**
 * @author chenyizong
 * @date 2020-02-29
 *
 */

@Data
public class OrderVO {
    private Integer id;
    private Integer userId;
    private Integer hotelId;
    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private String roomType;
    private Integer roomNum;
    private Integer peopleNum;
    private Boolean haveChild;
    private String createDate;
    private Double price;
    private String tenantName;
    private String phoneNumber;
    private String orderState;
    private String comment;
    private Integer commentScore;






}
