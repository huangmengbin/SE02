package com.example.hotel.bl.coupon;

import com.example.hotel.blImpl.coupon.*;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.SubClass;
import com.example.hotel.vo.OrderVO;

@FunctionalInterface
@SubClass(BirthdayCouponStrategyImpl.class)
@SubClass(TimeCouponStrategyImpl.class)
@SubClass(TargetMoneyCouponStrategyImpl.class)
@SubClass(TargetRoomCouponStrategyImpl.class)
public interface CouponMatchStrategy {
    boolean isMatch(OrderVO orderVO, Coupon coupon);
}
