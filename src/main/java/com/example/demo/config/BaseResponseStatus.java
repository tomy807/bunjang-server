package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    EMPTY_PATH_VARIABLE(false, 2004, "PathVariable를 입력해주세요."),
    INVAILD_PATH_VARIABLE(false, 2005, "올바르지 않은 PathVariable 형식입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_NOT_EXISTS_EMAIL(false,2017,"회원가입된 이메일이 아닙니다."),

    //  [POST] /address
    POST_ADDRESS_EMPTY_NAME(false, 2020, "이름을 입력해주세요"),
    POST_ADDRESS_EMPTY_PHONE(false, 2021, "전화번호를 입력해주세요"),
    POST_ADDRESS_EMPTY_ADDRESS(false, 2022, "주소를 입력해주세요"),
    POST_ADDRESS_EMPTY_DETAIL_ADDRESS(false, 2024, "상세주소를 입력해주세요"),
    POST_ADDRESS_EMPTY_DIRECT_ADDRESS(false, 2025, "직거래 장소를 입력해주세요"),

    // [POST] /products
    POST_PRODUCTS_EMPTY_IMAGE(false, 2026, "상품사진을 등록해주세요"),
    POST_PRODUCTS_EMPTY_TITLE(false, 2027, "상품명을 2글자 이상 입력해주세요"),
    POST_PRODUCTS_EMPTY_TAG(false, 2028, "태그를 입력해주세요"),
    POST_PRODUCTS_EMPTY_PRICE(false, 2029, "100원 이상 입력해주세요"),
    POST_PRODUCTS_EMPTY_EXPLANATION(false, 2030, "상품설명을 10자 이상 입력해주세요"),

    // [POST] /follows
    POST_FOLLOW_EMPTY_STORE_ID(false, 2031, "상점이름을 입력해주세요"),
    INVALID_STORE_ID(false, 2032, "올바르지 않은 상점입니다"),

    // [POST] /reviews
    POST_REVIEWS_EMPTY_PRODUCT_ID(false, 2033, "productId를 입력하세요."),
    POST_REVIEWS_EMPTY_DESCRIPTION(false, 2034, "reviewDescription을 입력하세요."),
    POST_REVIEWS_INVALID_SCORE(false, 2035, "1이상 5이하의 reviewScore를 입력하세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    DELETED_USER(false, 3015, "탈퇴한 유저입니다."),

    INVALID_PRODUCT_ID(false, 3021, "올바르지 않은 productId 입니다."),

    // [GET] /products
    INVALID_INQUIRY_ID(false, 3022, "올바르지 않은 inquiryId 입니다."),
    NOT_EXIST_SELLING_PRODUCT(false, 3023, "판매중인 상품이 없습니다."),
    NOT_EXIST_RESERVED_PRODUCT(false, 3024, "예약중인 상품이 없습니다."),
    NOT_EXIST_SOLDOUT_PRODUCT(false, 3025, "판매완료된 상품이 없습니다."),

    // [POST] /reviews
    EXISTS_REVIEW(false, 3027, "이미 리뷰가 존재합니다."),

    // [DELETE] /users/reviews/:reviewId
    INVALID_REVIEW_ID(false, 3028, "올바르지 않은 reviewId 입니다."),
    EMPTY_REVIEW(false, 3029, "리뷰가 없습니다"),
    DELETE_REVIEWS_INVALID_REVIEW_ID(false, 3030, "1이상의 reviewId를 입력하세요."),

    //  [GET] /chatting
    NOT_EXIST_CHATTING_ROOM(false, 3040, "채팅방이 없습니다."),

    // [post] /orders
    INVALID_PURCHASE(false, 3031, "구매 불가 상품입니다"),

    //  [GET] /users/orders
    NOT_EXIST_ORDER_PURCHASE(false, 3050, "구매한 상품이 없습니다."),
    NOT_EXIST_ORDER_SELL(false, 3051, "판매한 상품이 없습니다."),
    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_SHOP_NAME(false, 4015, "상점이름 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");



    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
