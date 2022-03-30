package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class OrderProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderDao orderDao;
    private final JwtService jwtService;

    @Autowired
    public OrderProvider(OrderDao orderDao, JwtService jwtService) {
        this.orderDao = orderDao;
        this.jwtService = jwtService;
    }


    public String getUserAddress(int userId) throws BaseException{
        try {
            return orderDao.getUserAddress(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getPoint(int userId) throws BaseException {
        try {
            int point = orderDao.getPoint(userId);
            return point;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getProductPrice(int productId) throws BaseException {
        try {
            int price = orderDao.getProductPrice(productId);
            return price;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetProductOrderRes getOrderView(int userId, int productId) throws BaseException {
        try{
            return orderDao.getOrderView(userId, productId);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getSellStatus(int productId) throws BaseException {
        try{
            return orderDao.getSellStatus(productId);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOrderDetailRes getOrderDetail(int orderId) throws BaseException {
        try{
            return orderDao.getOrderDetail(orderId);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getOrderStatus(int orderId) throws BaseException {
        try{
            return orderDao.getOrderStatus(orderId);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}