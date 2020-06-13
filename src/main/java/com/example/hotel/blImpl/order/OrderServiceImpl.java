package com.example.hotel.blImpl.order;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.po.Order;
import com.example.hotel.po.User;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final static String RESERVE_ERROR = "预订失败";
    private final static String DATE_ERROR = "宁怎么能预订今天之前的房间???";
    private final static String CREDIT_LACK = "宁的信用分不够了,赶紧充钱吧";
    private final static String ROOM_NUMBER_LACK = "预订房间数量剩余不足";
    private final static String ANNUl_ERROR = "删除订单失败";
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    HotelService hotelService;
    @Autowired
    AccountService accountService;

    @Override
    public ResponseVO addOrder(OrderVO orderVO) {
        if(accountService.getUserInfo(orderVO.getUserId()).getCredit()<=0){
            return ResponseVO.buildFailure(CREDIT_LACK);
        }
        int reserveRoomNum = orderVO.getRoomNum();//需要的数量
        int totalNum = hotelService.getRoomTotalNum(orderVO.getHotelId(),orderVO.getRoomType());//该酒店，这种房间的总数量
        int usedNum = this.getUsedNum(orderVO);
        int validNum = totalNum - usedNum;
        if(reserveRoomNum > validNum){
            return ResponseVO.buildFailure(ROOM_NUMBER_LACK);
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String curdate = sf.format(date);
            if(curdate.compareTo(orderVO.getCheckInDate())>0){
                return ResponseVO.buildFailure(DATE_ERROR);
            }
            orderVO.setCreateDate(curdate);
            orderVO.setOrderState("已预订");
            User user = accountService.getUserInfo(orderVO.getUserId());
            orderVO.setClientName(user.getUserName());
            orderVO.setPhoneNumber(user.getPhoneNumber());
            Order order = new Order();
            BeanUtils.copyProperties(orderVO,order);
            orderMapper.addOrder(order);
            //hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),orderVO.getRoomNum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(RESERVE_ERROR);
        }
       return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public List<Order> getUserOrders(int userId) {
        return orderMapper.getUserOrders(userId);
    }

    @Override
    public ResponseVO annulOrder(int orderId) {
        // 取消订单逻辑的具体实现（注意可能有和别的业务类之间的交互
        try{
            Order order=orderMapper.getOrderById(orderId);
            if(order.getOrderState().equals("已撤销"))
                return ResponseVO.buildFailure(ANNUl_ERROR);
            orderMapper.annulOrder(orderId);
            if(notRevocable(order)){
                User user = accountService.getUserInfo(order.getUserId());
                user.setCredit(user.getCredit()-order.getPrice()/2);
                accountService.updateCredit(order.getUserId(),user.getCredit()-order.getPrice()/2);
            }
            //hotelService.updateRoomInfo(order.getHotelId(),order.getRoomType(),-order.getRoomNum());
        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ANNUl_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    /**
     * @param hotelId
     * @return
     */
    @Override
    public List<Order> getHotelOrders(Integer hotelId) {
        List<Order> orders = this.getAllOrders();
        return orders.stream().filter(order -> order.getHotelId().equals(hotelId)).collect(Collectors.toList());
    }


    private boolean notRevocable(Order order){

        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date curTime = new Date(System.currentTimeMillis());
        String latestTime=order.getCheckInDate()+" 08:00:00";
        try{
            int result=curTime.compareTo(sf.parse(latestTime));
            return result>0;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return false;
    }

    private int getUsedNum(OrderVO orderVO){
        List<Order> orderList = this.getAllOrders().stream().
                filter(o -> o.getHotelId().equals(orderVO.getHotelId())).
                filter(o -> o.getRoomType().equals(orderVO.getRoomType())).
                filter(o -> o.getOrderState().equals("已预订")).collect(Collectors.toList());

        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        final long oneDay = 1000 * 60 * 60 * 24L;
        try {
            long checkInDate = sf.parse(orderVO.getCheckInDate()).getTime();
            long checkOutDate = sf.parse(orderVO.getCheckOutDate()).getTime();
            int result = 0;
            for(long date = checkInDate; date < checkOutDate; date += oneDay ){
                int tmp = 0;
                for(Order o : orderList){
                    if(sf.parse(o.getCheckInDate()).getTime() <= date && date < sf.parse(o.getCheckOutDate()).getTime()){
                        tmp += o.getRoomNum();
                    }
                }
                result = Math.max(result, tmp);
            }
            return result;
        }
        catch (ParseException e){
            e.printStackTrace();
            return Integer.MAX_VALUE;
        }
    }
}
