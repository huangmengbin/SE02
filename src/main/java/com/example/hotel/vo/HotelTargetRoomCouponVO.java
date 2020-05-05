package com.example.hotel.vo;

/**
 * 酒店多间金额优惠 eg 3间房减150
 */
public class HotelTargetRoomCouponVO extends CouponVO {

    private Integer hotelId;

    private Integer targetRoom;

    private Integer discountMoney;

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getTargetRoom() {
        return targetRoom;
    }

    public void setTargetRoom(Integer targetRoom) {
        this.targetRoom = targetRoom;
    }

    public Integer getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Integer discountMoney) {
        this.discountMoney = discountMoney;
    }

}
