package com.example.demo.src.order;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.order.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexInteger;

@RestController
@RequestMapping("/app/orders")
public class OrderController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderService orderService;
    private final OrderProvider orderProvider;
    private final JwtService jwtService;

    @Autowired
    public OrderController(OrderService orderService, OrderProvider orderProvider, JwtService jwtService) {
        this.orderService = orderService;
        this.orderProvider = orderProvider;
        this.jwtService = jwtService;
    }

    /**
     * 주문하기 API
     * [POST] /orders
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postOrders(@RequestBody PostOrderReq postOrderReq) {

        try {
            // jwt 에서 userId 추출.
            int userIdByJwt = jwtService.getUserIdx();

            if(orderProvider.getSellStatus(postOrderReq.getProductId()).equals("SELLING")){
                int orderId = orderService.createOrder(postOrderReq, userIdByJwt);
            }
            else
                return new BaseResponse<>(INVALID_PURCHASE);

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 상품 구매 화면 API
     * [GET] /orders/:productId
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @GetMapping("/{productId}")
    public BaseResponse<GetProductOrderRes> getOrderView(@PathVariable(required = false) String productId) {

        try {
            int userIdByJwt = jwtService.getUserIdx();

            int id = Integer.parseInt(productId);
            GetProductOrderRes getProductOrderRes = orderProvider.getOrderView(userIdByJwt, id);
            return new BaseResponse<>(getProductOrderRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 주문 명세서 API
     * [GET] /orders/:orderId
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @GetMapping("/details/{orderId}")
    public BaseResponse<GetOrderDetailRes> getOrderDetail(@PathVariable(required = false) String orderId) {

        try {
            int userIdByJwt = jwtService.getUserIdx();

            int id = Integer.parseInt(orderId);
            GetOrderDetailRes getOrderDetailRes = orderProvider.getOrderDetail(id);

            return new BaseResponse<>(getOrderDetailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 구매 취소 API
     * [GET] /orders/cancel
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @PostMapping("/cancel")
    public BaseResponse<String> postCancel(@RequestBody PostCancelReq postCancelReq) {

        try {
            // jwt 에서 userId 추출.
            int userIdByJwt = jwtService.getUserIdx();

            if(orderProvider.getOrderStatus(postCancelReq.getOrderId()).equals("RESERVED")){
               orderService.changeCancelStatus(postCancelReq, userIdByJwt);
            }
            else
                return new BaseResponse<>(INVALID_PURCHASE);

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 구매 확정 API
     * [GET] /orders/confirm
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @PostMapping("/confirm")
    public BaseResponse<String> postConfirm(@RequestBody PostConfirmReq postConfirmReq) {

        try {
            // jwt 에서 userId 추출.
            int userIdByJwt = jwtService.getUserIdx();

            if(orderProvider.getOrderStatus(postConfirmReq.getOrderId()).equals("RESERVED")){
                orderService.changeConfirmStatus(postConfirmReq, userIdByJwt);
            }
            else
                return new BaseResponse<>(INVALID_PURCHASE);

            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}