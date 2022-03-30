package com.example.demo.src.order;

import com.example.demo.src.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String getUserAddress(int userId){
        String getUserAddressQuery = "select concat(address, ' ', detail_address) as address from Address where user_id = ? and address_type='DELIVERY' and main='MAIN'";
        int getUserAddressParams = userId;
        return this.jdbcTemplate.queryForObject(getUserAddressQuery, String.class, getUserAddressParams);
    }

    public int makeOrderInfo(int userId, String address, int totalPrice, PostOrderReq postOrderReq, String name, String phone){
        String makeOrderInfoQuery = "insert into Orders(user_id, product_id, address, total_price, trading_method, pay_method, address_option, name, phone) values(?,?,?,?,?,?,?,?,?)";
        Object[] makeOrderInfoParams = new Object[]{userId,postOrderReq.getProductId(),address,totalPrice, postOrderReq.getTradingMethod(),
                postOrderReq.getPayMethod(), postOrderReq.getAddressOption(), name, phone};
        this.jdbcTemplate.update(makeOrderInfoQuery, makeOrderInfoParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int getPoint(int userId){
        String getPointQuery = "select point from Users where user_id = ?";
        int getPointParams = userId;
        return this.jdbcTemplate.queryForObject(getPointQuery, int.class, getPointParams);
    }

    public int getProductPrice(int productId){
        String getProductPriceQuery = "select price from Products where product_id = ?";
        int getProductPriceParams = productId;
        return this.jdbcTemplate.queryForObject(getProductPriceQuery, int.class, getProductPriceParams);
    }

    public void changeReservedStatus(int productId){
        String changeStatusQuery = "update Products set sell_status = 'RESERVED' where product_id=?";
        int changeStatusParams = productId;
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }

    public void changePoint(int userId, int usedPoint){
        String changePointQuery = "update Users set point = point-? where user_id=?";
        Object[] changePointParams = new Object[]{usedPoint, userId};
        this.jdbcTemplate.update(changePointQuery, changePointParams);
    }

    public GetProductOrderRes getOrderView(int userId, int productId) {
        String getOrderViewQuery = "select point, product_title as title, price, shipping_fee as shippingFee, \n" +
                "       (select product_image_url from ProductImages where Products.product_id = ProductImages.product_id limit 1) as productImg,\n" +
                "       address, detailAddress, name, A.phone as phone\n" +
                "from  Products, Users\n" +
                "left outer join (select Address.address as address, detail_address as detailAddress, name, phone, user_id from Address where address_type='DELIVERY' and main='MAIN') as  A on Users.user_id = A.user_id\n" +
                "where Users.user_id=? and product_id=?";
        Object[] getOrderViewParams = new Object[]{userId, productId};
        return this.jdbcTemplate.queryForObject(getOrderViewQuery,
                (rs, rowNum) -> new GetProductOrderRes(
                        rs.getInt("point"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getString("productImg"),
                        rs.getString("shippingFee")
                ),
                getOrderViewParams);
    }


    public String getSellStatus(int productId) {
        String getSellStatusQuery = "select sell_status from Products where product_id=? ";
        int getSellStatusParams =  productId;

        return this.jdbcTemplate.queryForObject(getSellStatusQuery, String.class, getSellStatusParams);
    }

    public GetOrderDetailRes getOrderDetail(int orderId) {
        String getOrderViewQuery = "select P.product_id as productId, order_id as orderId, trading_method as tradingMethod,  U.shop_name as buyer, O.total_price as totalPrice,\n" +
                "       YEAR(O.createdAt) as year, MONTH(O.createdAt) as month, DAY(O.createdAt) as day, date_format(O.createdAt, '%h:%i:%s') as time,\n" +
                "        product_title as title, price, (select product_image_url from ProductImages where P.product_id = ProductImages.product_id limit 1) as productImg, \n" +
                "       U2.shop_name as seller, P.shipping_fee as shippingFee, U2.phone as sellerPhone,\n" +
                "        O.address as address, O.name, O.phone as buyerPhone, (case when AA.address is null then '지역정보 없음' else AA.address end) as directAddress\n" +
                "from Orders O\n" +
                "join Products P on P.product_id = O.product_id\n" +
                "join Users U on U.user_id = O.user_id\n" +
                "join Users U2 on U2.user_id = P.user_id\n" +
                "left outer join  (select address, user_id from Address where address_type='DIRECT' and main='MAIN') as  AA on O.user_id = AA.user_id\n" +
                "where order_id =?";
        int getOrderViewParams = orderId;
        return this.jdbcTemplate.queryForObject(getOrderViewQuery,
                (rs, rowNum) -> new GetOrderDetailRes(
                        rs.getInt("productId"),
                        rs.getString("productImg"),
                        rs.getString("title"),
                        rs.getInt("price"),

                        rs.getInt("orderId"),
                        rs.getString("tradingMethod"),
                        rs.getString("buyer"),
                        rs.getString("seller"),
                        rs.getString("shippingFee"),
                        rs.getInt("totalPrice"),
                        rs.getInt("year"),
                        rs.getInt("month"),
                        rs.getInt("day"),
                        rs.getString("time"),


                        rs.getString("address"),
                        rs.getString("directAddress"),


                        rs.getString("name"),
                        rs.getString("buyerPhone"),
                        rs.getString("sellerPhone")
                ), getOrderViewParams);
    }

    public String getDeliverName(int userId){
        String getUserAddressQuery = "select name from Address where user_id = ? and address_type='DELIVERY' and main='MAIN'";
        int getUserAddressParams = userId;
        return this.jdbcTemplate.queryForObject(getUserAddressQuery, String.class, getUserAddressParams);
    }

    public String getDeliverPhone(int userId){
        String getUserAddressQuery = "select phone from Address where user_id = ? and address_type='DELIVERY' and main='MAIN'";
        int getUserAddressParams = userId;
        return this.jdbcTemplate.queryForObject(getUserAddressQuery, String.class, getUserAddressParams);
    }

    public String getUserPhone(int userId){
        String getUserAddressQuery = "select phone from Users where user_id = ? ";
        int getUserAddressParams = userId;
        return this.jdbcTemplate.queryForObject(getUserAddressQuery, String.class, getUserAddressParams);
    }

    public String getOrderStatus(int orderId) {
        String getSellStatusQuery = "select order_status from Orders where order_id=? ";
        int getSellStatusParams =  orderId;

        return this.jdbcTemplate.queryForObject(getSellStatusQuery, String.class, getSellStatusParams);
    }

    public int getBuyerId(int orderId) {
        String getSellStatusQuery = "select user_id from Orders where order_id=? ";
        int getSellStatusParams =  orderId;

        return this.jdbcTemplate.queryForObject(getSellStatusQuery, int.class, getSellStatusParams);
    }

    public int getSellerId(int orderId) {
        String getSellStatusQuery = "select P.user_id from Orders join Products P on P.product_id = Orders.product_id where order_id=? ";
        int getSellStatusParams =  orderId;

        return this.jdbcTemplate.queryForObject(getSellStatusQuery, int.class, getSellStatusParams);
    }

    public int getProductId(int orderId) {
        String getSellStatusQuery = "select product_id from Orders where order_id=? ";
        int getSellStatusParams =  orderId;

        return this.jdbcTemplate.queryForObject(getSellStatusQuery, int.class, getSellStatusParams);
    }

    public void changeSellingStatus(int productId){
        String changeStatusQuery = "update Products set sell_status = 'SELLING' where  product_id=?";
        int changeStatusParams = productId;
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }

    public void changeCancelStatus(int orderId){
        String changeStatusQuery = "update Orders set order_status = 'CANCEL' where  order_id=?";
        int changeStatusParams = orderId;
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }

    public void changeConfirmStatus(int orderId){
        String changeStatusQuery = "update Orders set order_status = 'CONFIRM' where  order_id=?";
        int changeStatusParams = orderId;
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }

    public void changeSoldoutStatus(int productId){
        String changeStatusQuery = "update Products set sell_status = 'SOLDOUT' where  product_id=?";
        int changeStatusParams = productId;
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }

}