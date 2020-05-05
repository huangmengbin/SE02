package com.example.hotel.controller.coupon;

import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.HotelTargetMoneyCouponVO;
import com.example.hotel.vo.TimeCouponVO;
import com.example.hotel.vo.HotelTargetRoomCouponVO;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/hotelTargetMoney")
    public ResponseVO addHotelTargetMoneyCoupon(@RequestBody HotelTargetMoneyCouponVO hotelTargetMoneyCouponVO) {
        try {   //虽然这里有try catch, 但如果 HotelTargetMoneyCouponVO 格式不合法(比如价格无法转换成int), 将不会进入这里
            CouponVO couponVO = couponService.addHotelTargetMoneyCoupon(hotelTargetMoneyCouponVO);
            return ResponseVO.buildSuccess(couponVO);
        }
        catch (Exception e){
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @PostMapping("/time")
    public ResponseVO addTimeCoupon(@RequestBody TimeCouponVO timeCouponVO) {
        try {   //虽然这里有try catch, 但如果 HotelTargetMoneyCouponVO 格式不合法(比如价格无法转换成int), 将不会进入这里
            CouponVO couponVO = couponService.addTimeCoupon(timeCouponVO);
            return ResponseVO.buildSuccess(couponVO);
        }
        catch (Exception e){
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @PostMapping("/hotelTargetRoom")
    public ResponseVO addHotelTargetRoomCoupon(@RequestBody HotelTargetRoomCouponVO hotelTargetRoomCouponVO) {
        try {   //虽然这里有try catch, 但如果 HotelTargetMoneyCouponVO 格式不合法(比如价格无法转换成int), 将不会进入这里
            CouponVO couponVO = couponService.addHotelTargetRoomCoupon(hotelTargetRoomCouponVO);
            return ResponseVO.buildSuccess(couponVO);
        }
        catch (Exception e){
            return ResponseVO.buildFailure(e.getMessage());
        }
    }


    @GetMapping("/hotelAllCoupons")
    public ResponseVO getHotelAllCoupons(@RequestParam Integer hotelId) {
        return ResponseVO.buildSuccess(couponService.getHotelAllCoupon(hotelId));
    }

    @GetMapping("/orderMatchCoupons")
    public ResponseVO getOrderMatchCoupons(@RequestParam Integer userId,
                                           @RequestParam Integer hotelId,
                                           @RequestParam Double orderPrice,
                                           @RequestParam Integer roomNum,
                                           @RequestParam String checkIn,
                                           @RequestParam String checkOut) {
        OrderVO requestOrderVO = new OrderVO();
        requestOrderVO.setUserId(userId);
        requestOrderVO.setHotelId(hotelId);
        requestOrderVO.setPrice(orderPrice);
        requestOrderVO.setRoomNum(roomNum);
        requestOrderVO.setCheckInDate(checkIn);
        requestOrderVO.setCheckOutDate(checkOut);
        return ResponseVO.buildSuccess(couponService.getMatchOrderCoupon(requestOrderVO));
    }


}
