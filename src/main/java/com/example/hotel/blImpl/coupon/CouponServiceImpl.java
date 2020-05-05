package com.example.hotel.blImpl.coupon;
import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.data.coupon.CouponMapper;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.CouponType;
import com.example.hotel.util.SubClass;
import com.example.hotel.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.*;

@Service
public class CouponServiceImpl implements CouponService {



    private final CouponMapper couponMapper;

    private static HashMap<Integer,CouponMatchStrategy> hashMap = new HashMap<>();

    @Autowired
    public CouponServiceImpl(CouponMapper couponMapper) {
        this.couponMapper = couponMapper;
    }



    @Override
    public List<Coupon> getMatchOrderCoupon(OrderVO orderVO) {

        List<Coupon> hotelCoupons = getHotelAllCoupon(orderVO.getHotelId());

        List<Coupon> availAbleCoupons = new ArrayList<>();

        for (Coupon hotelCoupon : hotelCoupons) {
            if (hashMap.get(hotelCoupon.getCouponType()).isMatch(orderVO, hotelCoupon)) {
                availAbleCoupons.add(hotelCoupon);
            }
        }
        return availAbleCoupons;
    }

    @Override
    public List<Coupon> getHotelAllCoupon(Integer hotelId) {
        return couponMapper.selectByHotelId(hotelId);
    }

    @Override
    public CouponVO addHotelTargetMoneyCoupon(HotelTargetMoneyCouponVO hotelTargetMoneyCouponVO) {
        if(hotelTargetMoneyCouponVO.getTargetMoney() <= hotelTargetMoneyCouponVO.getDiscountMoney()){
            throw new RuntimeException("你在想peach");
        }
        Coupon coupon = new Coupon();
        coupon.setCouponName(hotelTargetMoneyCouponVO.getName());
        coupon.setDescription(hotelTargetMoneyCouponVO.getDescription());
        coupon.setCouponType(hotelTargetMoneyCouponVO.getType());
        coupon.setTargetMoney(hotelTargetMoneyCouponVO.getTargetMoney());
        coupon.setHotelId(hotelTargetMoneyCouponVO.getHotelId());
        coupon.setDiscountMoney(hotelTargetMoneyCouponVO.getDiscountMoney());
        coupon.setStatus(1);///？？？
        int result = couponMapper.insertCoupon(coupon);
        hotelTargetMoneyCouponVO.setId(result);
        return hotelTargetMoneyCouponVO;
    }

    @Override
    public CouponVO addTimeCoupon(TimeCouponVO timeCouponVO) {
        Coupon coupon = new Coupon();
        coupon.setCouponName(timeCouponVO.getName());
        coupon.setDescription(timeCouponVO.getDescription());
        coupon.setCouponType(timeCouponVO.getType());
        coupon.setTargetMoney(timeCouponVO.getTargetMoney());
        coupon.setHotelId(-1);//默认由平台发起
        coupon.setDiscountMoney(timeCouponVO.getDiscount());
        coupon.setStatus(1);///？？？
        int result = couponMapper.insertCoupon(coupon);
        timeCouponVO.setId(result);
        return timeCouponVO;
    }

    @Override
    public CouponVO addHotelTargetRoomCoupon(HotelTargetRoomCouponVO hotelTargetRoomCouponVO) {
        Coupon coupon = new Coupon();
        coupon.setCouponName(hotelTargetRoomCouponVO.getName());
        coupon.setDescription(hotelTargetRoomCouponVO.getDescription());
        coupon.setCouponType(hotelTargetRoomCouponVO.getType());
        coupon.setTargetMoney(hotelTargetRoomCouponVO.getTargetRoom());
        coupon.setHotelId(hotelTargetRoomCouponVO.getHotelId());
        coupon.setDiscountMoney(hotelTargetRoomCouponVO.getDiscountMoney());
        coupon.setStatus(1);///？？？
        int result = couponMapper.insertCoupon(coupon);
        hotelTargetRoomCouponVO.setId(result);
        return hotelTargetRoomCouponVO;
    }


    static {

        Arrays.stream(CouponMatchStrategy.class.getAnnotationsByType(SubClass.class))
                .map(SubClass::value)
                .filter(aClass -> Arrays.stream(aClass.getInterfaces()).anyMatch(a -> a==CouponMatchStrategy.class))
                .map(aClass->(Class<? extends CouponMatchStrategy>) aClass)
                .forEach( couponMatchStrategyImpl /*对于match接口的每一个子类*/ ->
                        {
                            try {
                                assertEquals("\n\n有一个类的isMatch方法没有贴上CouponType注解:"+couponMatchStrategyImpl.getName()+"\n\n",
                                        1 ,
                                    Arrays.stream(new Method[]{couponMatchStrategyImpl.getMethod("isMatch", OrderVO.class, Coupon.class)})
                                    .filter(method -> method.isAnnotationPresent(CouponType.class))
                                    .map(method -> method.getAnnotation(CouponType.class))
                                    .map(CouponType::value)
                                    .peek(couponTypeKey->{
                                        try {
                                            if (hashMap.get(couponTypeKey) != null) {
                                                throw new RuntimeException("发现两个同样的优惠值：" + couponTypeKey);
                                            } else {
                                                hashMap.put(couponTypeKey, couponMatchStrategyImpl.newInstance());
                                                System.err.println("\t"+couponTypeKey+" -> "+couponMatchStrategyImpl);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    })
                                    .count());
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }
                );

    }
}
