package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = BaseException.class)
public class OrderService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final OrderProvider orderProvider;
    private final JwtService jwtService;

    @Autowired
    public OrderService(OrderDao orderDao, OrderProvider orderProvider, JwtService jwtService) {
        this.orderDao = orderDao;
        this.orderProvider = orderProvider;
        this.jwtService = jwtService;
    }

    public int createOrder(PostOrderReq postOrderReq, int userId) throws BaseException {
        try {

            String address = null;
            String name = null;
            String phone = null;
            if((postOrderReq.getTradingMethod()).equals("DELIVERY")) {
                address = orderDao.getUserAddress(userId);
                name =  orderDao.getDeliverName(userId);
                phone =  orderDao.getDeliverPhone(userId);
            }
            else {
                phone =  orderDao.getUserPhone(userId);
            }

            int point = postOrderReq.getPoint();
            int price = orderProvider.getProductPrice(postOrderReq.getProductId());

            int totalPrice = price-point+postOrderReq.getTax();


            int orderId = orderDao.makeOrderInfo(userId, address, totalPrice, postOrderReq, name, phone);

            orderDao.changeReservedStatus(postOrderReq.getProductId());

            orderDao.changePoint(userId, point);

            return orderId;


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void changeCancelStatus(PostCancelReq postCancelReq, int userId) throws BaseException {
        try {
            int buyerId = orderDao.getBuyerId(postCancelReq.getOrderId());
            int sellerId = orderDao.getSellerId(postCancelReq.getOrderId());

            int productId = orderDao.getProductId(postCancelReq.getOrderId());
            if(userId == buyerId || userId == sellerId){
                orderDao.changeCancelStatus(postCancelReq.getOrderId());
                orderDao.changeSellingStatus(productId);
            }


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void changeConfirmStatus(PostConfirmReq postConfirmReq, int userId) throws BaseException {
        try {
            int buyerId = orderDao.getBuyerId(postConfirmReq.getOrderId());
            int sellerId = orderDao.getSellerId(postConfirmReq.getOrderId());

            int productId = orderDao.getProductId(postConfirmReq.getOrderId());
            if(userId == buyerId || userId == sellerId){
                orderDao.changeConfirmStatus(postConfirmReq.getOrderId());
                orderDao.changeSoldoutStatus(productId);
            }


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}