package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = BaseException.class)
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;

    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
    }

    public int createProduct(int userId, PostProductReq postProductReq) throws BaseException {
        try {
            String address = productProvider.getMainDirectAddress(userId);
            return productDao.createProduct(userId, address, postProductReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void createProductImage(int productId, String productImageUrl) throws BaseException {
        try {
            productDao.createProductImage(productId, productImageUrl);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createProductTag(int productId, String tagName) throws BaseException {
        try {
            productDao.createProductTag(productId, tagName);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createInquiry(int userId, int productId, PostInquiryReq postInquiryReq) throws BaseException {
        try {
            productDao.createInquiry(userId, productId, postInquiryReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteInquiry(int userId, int inquiryId, int productId) throws BaseException {
        try{
            productDao.deleteInquiry(userId, inquiryId, productId);

        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateSellStatus(int userId, int productId, String status) throws BaseException {
        try {
            if(status.equals("SELLING"))
                productDao.changeSellingStatus(userId, productId);
            else if(status.equals("RESERVED"))
                productDao.changeReservedStatus(userId, productId);
            else if(status.equals("SOLDOUT"))
                productDao.changeSoldoutStatus(userId, productId);


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void updateViewCount(int productId) throws BaseException {
        try {
            productDao.updateViewCount(productId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}