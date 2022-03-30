package com.example.demo.src.product;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductService productService;
    private final ProductProvider productProvider;
    private final JwtService jwtService;

    @Autowired
    public ProductController(ProductService productService,ProductProvider productProvider, JwtService jwtService) {
        this.productService = productService;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
    }

    /**
     * 상품 등록 API
     * [POST] /products
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postProducts(@RequestBody PostProductReq postProductReq) {

        if(postProductReq.getProductImgList()==null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_IMAGE);
        }

        if(postProductReq.getTitle()==null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);
        }

        if(postProductReq.getProductTagList()==null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_TAG);

        }

        if(postProductReq.getPrice()==null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRICE);

        }

        if(postProductReq.getExplanation()==null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_EXPLANATION);
        }


        try {
            // jwt 에서 userId 추출.
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }


            int productId = productService.createProduct(userIdByJwt, postProductReq);

            if(postProductReq.getProductImgList() != null){
                List<ProductImg> productImgList = postProductReq.getProductImgList();
                for(ProductImg productImg : productImgList){
                    productService.createProductImage(productId, productImg.getProductImgUrl());
                }
            }

            if(postProductReq.getProductTagList() != null){
                List<ProductTag> productTagList = postProductReq.getProductTagList();
                for(ProductTag productTag : productTagList){
                    productService.createProductTag(productId, productTag.getTagName());
                }
            }


            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품 화면 - 상품정보 API
     * [GET] /products/:productId
     * @return BaseResponse<GetProductInfoRes>
     */
    @ResponseBody
    @GetMapping("/{productId}")
    public BaseResponse<GetProductInfoRes> getProductInfo(@PathVariable(required = false) String productId) {
        if(productId == null){
            return new BaseResponse<>(EMPTY_PATH_VARIABLE);
        }
        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            if(!isRegexInteger(productId)){
                return new BaseResponse<>(INVAILD_PATH_VARIABLE);
            }
            int id = Integer.parseInt(productId);
            if(productProvider.checkProductId(id) == 0){
                return new BaseResponse<>(INVALID_PRODUCT_ID);
            }

            productService.updateViewCount(id);

            //이 부분 너무 더러움
            GetProductInfoRes getProductInfoRes = new GetProductInfoRes(id);
            getProductInfoRes.setProductInfo(productProvider.getProductInfos(userIdByJwt,id));
            getProductInfoRes.setProductTagList(productProvider.getProductTags(id));
            getProductInfoRes.setProductImgList(productProvider.getProductImages(id));
            getProductInfoRes.setStoreInfo(productProvider.getStoreInfos(id));
            int storeId=getProductInfoRes.getStoreInfo().getStoreId();
            getProductInfoRes.setSellProductList(productProvider.getSellProducts(storeId));
            int categoryId=getProductInfoRes.getProductInfo().getCategoryId();
            getProductInfoRes.setRelateProductList(productProvider.getRelateProducts(categoryId, id));
            getProductInfoRes.setReviewList(productProvider.getReviews(storeId));





            return new BaseResponse<>(getProductInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품 화면 - 상품정보 API
     * [GET] /products
     * @return BaseResponse<GetProductRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProductRes>> getProduct() {

        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetProductRes> products = productProvider.getProducts(userIdByJwt);
            return new BaseResponse<>(products);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품 화면 - 큰 카테고리 API
     * [GET] /products/largeCategory
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @GetMapping("/largeCategory")
    public BaseResponse<List<GetCategoryRes>> getLargeCategories() {

        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetCategoryRes> largeCategories = productProvider.getLargeCategories();
            return new BaseResponse<>(largeCategories);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품 화면 - 중간 카테고리 API
     * [GET] /products/middleCategory/:largecategoryId
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @GetMapping("/middleCategory/{largecategoryId}")
    public BaseResponse<List<GetCategoryRes>> getMiddleCategories(@PathVariable(required = false) String largecategoryId) {

        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            int categoryId = Integer.parseInt(largecategoryId);
            List<GetCategoryRes> middleCategories = productProvider.getMiddleCategories(categoryId);
            return new BaseResponse<>(middleCategories);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 특정 상품 화면 - 작은 카테고리 API
     * [GET] /products/smallCategory/:smallcategoryId
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @GetMapping("/smallCategory/{middlecategoryId}")
    public BaseResponse<List<GetCategoryRes>> getSmallCategories(@PathVariable(required = false) String middlecategoryId) {

        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            int categoryId = Integer.parseInt(middlecategoryId);
            List<GetCategoryRes> smallCategories = productProvider.getSmallCategories(categoryId);
            return new BaseResponse<>(smallCategories);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 상품 화면 - 상품 문의 조회 API
     * [GET] /products/smallCategory/:smallcategoryId
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @GetMapping("/{productId}/inquiry")
    public BaseResponse<List<GetInquiryRes>> getInquiries(@PathVariable(required = false) String productId) {

        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            int id = Integer.parseInt(productId);
            List<GetInquiryRes> inquiryResList = productProvider.getInquiries(id);
            return new BaseResponse<>(inquiryResList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 특정 상품 화면 - 상품 문의 API
     * [POST] /products/:productId/inquiry
     * @return BaseResponse<GetCategoryRes>
     */
    @ResponseBody
    @PostMapping("/{productId}/inquiry")
    public BaseResponse<String> postInquiry(@PathVariable("productId") String productId, @RequestBody PostInquiryReq postInquiryReq){

        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            int id = Integer.parseInt(productId);

            productService.createInquiry(userIdByJwt, id, postInquiryReq);
            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

//
//    /**
//     * 특정 상품 화면 - 상품 답장 API
//     * [POST] /products/:productId/inquiry/:inquiryId/call
//     * @return BaseResponse<GetCategoryRes>
//     */
//    @ResponseBody
//    @GetMapping("/{productId}/inquiry/{inquiryId}/call")
//    public BaseResponse<String> postInquiry(@PathVariable("productId") String productId,@PathVariable("inquiryId") String inquiryId){
//
//        try {
//            int userIdByJwt = jwtService.getUserIdx();
//
//            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
//                return new BaseResponse<>(DELETED_USER);
//            }
//            if(!isRegexInteger(productId)||!isRegexInteger(inquiryId)){
//                return new BaseResponse<>(INVAILD_PATH_VARIABLE);
//            }
//
//            int pId = Integer.parseInt(productId);
//            int iId = Integer.parseInt(inquiryId);
//
//            return new BaseResponse<>(productProvider.getInquiryCall(pId, iId));
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }


    /**
     * 특정 상품 화면 - 상품문의 삭제 API
     * [DELETE] /products/:productId/inquiry/:inquiryId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{productId}/inquiry/{inquiryId}")
    public BaseResponse<String> deleteInquiry(@PathVariable("productId") String productId,@PathVariable("inquiryId") String inquiryId){

        try {
            int userIdByJwt = jwtService.getUserIdx();

            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            if(!isRegexInteger(productId)||!isRegexInteger(inquiryId)){
                return new BaseResponse<>(INVAILD_PATH_VARIABLE);
            }

            int pId = Integer.parseInt(productId);
            int iId = Integer.parseInt(inquiryId);

            if(productProvider.checkInquiry(userIdByJwt, iId, pId) == 0){
                return new BaseResponse<>(INVALID_INQUIRY_ID);
            }
            productService.deleteInquiry(userIdByJwt, iId, pId);
            return new BaseResponse<>("success");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정상품화면 - 상품 판매 상태 변경 API
     * [PATCH] /products/:productId/sellStatus
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{productId}/sellStatus")
    public BaseResponse<String> changeSellStatus(@PathVariable("productId") String productId, @RequestBody PatchSellReq patchSellReq) throws BaseException {
        if(!isRegexInteger(productId)){
            return new BaseResponse<>(INVAILD_PATH_VARIABLE);
        }
        try{
            // jwt 에서 userId 추출
            int userIdByJwt = jwtService.getUserIdx();
            if (productProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            int id = Integer.parseInt(productId);

            if(productProvider.checkSellStatus(userIdByJwt, id) != 0)
                productService.updateSellStatus(userIdByJwt, id, patchSellReq.getStatus());
            return new BaseResponse<>("success");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}