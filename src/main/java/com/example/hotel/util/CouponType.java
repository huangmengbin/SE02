package com.example.hotel.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface CouponType {
    int
            BirthdayCouponStrategy=1,
            TargetRoomCouponStrategy=2,
            TargetMoneyCouponStrategy=3,
            TimeCouponStrategy = 4;
    int value();
}
