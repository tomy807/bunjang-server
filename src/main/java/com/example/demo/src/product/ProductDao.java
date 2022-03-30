package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkUserStatusByUserId(int userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from Users where user_id = ? and status = 'ACTIVE')";
        int checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public int createProduct(int userId, String address, PostProductReq postProductReq){
        String createProductQuery = "insert into Products(user_id, product_title,\n" +
                "category_large_id,category_middle_id,category_small_id,\n" +
                "product_status,exchange_possible,price,shipping_fee,\n" +
                "explanation, quantity, secure_payment, direct_address) \n" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{userId, postProductReq.getTitle(), postProductReq.getCategoryLarge(),
                postProductReq.getCategoryMiddle(),postProductReq.getCategorySmall(),postProductReq.getProductStatus(),
                postProductReq.getExchangePossible(),postProductReq.getPrice(),postProductReq.getShippingFee(),
                postProductReq.getExplanation(), postProductReq.getQuantity(), postProductReq.getSecurePayment(), address};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }


    public void createProductImage(int productId, String productImageUrl) {
        String createProductImageQuery = "insert into ProductImages(product_id, product_image_url) values(?,?)";
        Object[] createProductImageParams = new Object[]{productId, productImageUrl};
        this.jdbcTemplate.update(createProductImageQuery, createProductImageParams);
    }

    public void createProductTag(int productId, String tagName) {
        String createProductTagQuery = "insert into Tag(product_id, tag_name) values(?,?)";
        Object[] createProductTagParams = new Object[]{productId, tagName};
        this.jdbcTemplate.update(createProductTagQuery, createProductTagParams);
    }

    public List<ProductImg> getProductImages(int productId){
        String getProductImagesQuery = "select product_image_id as productImgId, product_image_url as productImgUrl from ProductImages where product_id = ? ";
        int getProductImagesParams = productId;
        return this.jdbcTemplate.query(getProductImagesQuery,
                (rs, rowNum) -> new ProductImg(
                        rs.getInt("productImgId"),
                        rs.getString("productImgUrl")),
                getProductImagesParams);
    }

    public int checkProductId(int productId) {
        String checkProductIdQuery = "select(exists(select * from Products where product_id = ?))";
        int checkProductIdParams = productId;
        return this.jdbcTemplate.queryForObject(checkProductIdQuery, int.class, checkProductIdParams);
    }


    public ProductInfo getProductInfos(int userId, int productId) {
        String getProductInfoQuery = "select view_count as viewCount, product_title as title, price,\n" +
                "       (case when F.user_id=? then 'LIKE' else 'UNLIKE' end) as myFavorite,\n" +
                "          direct_address as directAddress,\n" +
                "           case when product_status='USED' then '중고'\n" +
                "                else '새상품' end as productStatus,\n" +
                "           case when shipping_fee='EXCLUDE' then '배송비별도'\n" +
                "                else '배송비포함' end as shippingFee,\n" +
                "           case when exchange_possible = 'EXCHANGEABLE' then '·교환가능' else \"\" end as exchangePossible,\n" +
                "          quantity, explanation,\n" +
                "       secure_payment as securePayment,\n" +
                "       case when sell_status = 'SELLING' then '판매중'\n" +
                "            when sell_status = 'RESERVED' then '예약중'\n" +
                "           when sell_status = 'SOLDOUT' then '판매완료'\n" +
                "           end as sellStatus,\n" +
                "        (case when timestampdiff(second , Products.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(second, Products.createdAt, current_timestamp),'초 전')\n" +
                "            when timestampdiff(minute , Products.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(minute, Products.createdAt, current_timestamp),'분 전')\n" +
                "            when timestampdiff(hour, Products.createdAt, current_timestamp) <24\n" +
                "                then concat(timestampdiff(hour, Products.createdAt, current_timestamp),'시간 전')\n" +
                "            else concat(datediff( current_timestamp, Products.createdAt),'일 전')\n" +
                "            end) as createdAt,\n" +
                "       (select case when fCount is null then 0 else fCount end) as favoriteCount,\n" +
                "       CS.category_small_name as category,CS.category_small_id as categoryId,\n" +
                "       (select case when ICount is null then 0 else ICount end) as productInquiry\n" +
                "from Products\n" +
                "left join (select user_id, product_id, count(*) as fCount from Favorites group by product_id) as F on Products.product_id = F.product_id\n" +
                "left join CategorySmall CS on Products.category_small_id = CS.category_small_id\n" +
                "left join (select product_id, count(*) as ICount from ProductInquiry group by product_id) as PI on Products.product_id = PI.product_id\n" +
                "where Products.product_id=?";
        Object[] getProductInfoParams = new Object[]{userId, productId};
        return this.jdbcTemplate.queryForObject(getProductInfoQuery,
                (rs, rowNum) -> new ProductInfo(
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("directAddress"),
                        rs.getString("productStatus"),
                        rs.getString("shippingFee"),
                        rs.getString("exchangePossible"),
                        rs.getInt("quantity"),

                        rs.getString("explanation"),
                        rs.getString("securePayment"),
                        rs.getString("sellStatus"),
                        rs.getString("createdAt"),
                        rs.getString("favoriteCount"),
                        rs.getInt("categoryId"),
                        rs.getString("category"),
                        rs.getString("productInquiry"),
                        rs.getString("myFavorite"),
                        rs.getInt("viewCount")
                ),
                getProductInfoParams);
    }

    public List<ProductTag> getProductTags(int productId) {
        String getProductTagQuery = "select tag_id as tagId, tag_name as tagName from Tag where product_id=?";
        int getProductTagParams = productId;
        return this.jdbcTemplate.query(getProductTagQuery,
                (rs, rowNum) -> new ProductTag(
                        rs.getInt("tagId"),
                        rs.getString("tagName")
                ),
                getProductTagParams);
    }

    public List<GetProductRes> getProducts(int userId) {
        String getProductsQuery = "select Products.product_id as productIdx, product_title as title, price,\n" +
                "       (select product_image_url from ProductImages where Products.product_id = ProductImages.product_id limit 1) as productImg,\n"+
                "         (case when direct_address LIKE '지역정보 없음' then direct_address else SUBSTRING_INDEX(direct_address,\" \",-2) end) as directAddress,\n" +
                "       (case when F.user_id=? then 'LIKE' else 'UNLIKE' end) as myFavorite,\n" +
                "       secure_payment as securePayment,\n" +
                "        (case when timestampdiff(second , Products.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(second, Products.createdAt, current_timestamp),'초 전')\n" +
                "            when timestampdiff(minute , Products.createdAt, current_timestamp) <60\n" +
                "                then concat(timestampdiff(minute, Products.createdAt, current_timestamp),'분 전')\n" +
                "            when timestampdiff(hour, Products.createdAt, current_timestamp) <24\n" +
                "                then concat(timestampdiff(hour, Products.createdAt, current_timestamp),'시간 전')\n" +
                "            else concat(datediff( current_timestamp, Products.createdAt),'일 전')\n" +
                "            end) as createdAt,\n" +
                "       (select case when fCount is null then 0 else fCount end) as favoriteCount\n" +
                "from Products\n" +
                "left join (select product_id, user_id, count(*) as fCount from Favorites group by product_id) as F on Products.product_id = F.product_id\n" +
                "where sell_status='SELLING'";
        int getProductParams = userId;
        return this.jdbcTemplate.query(getProductsQuery,
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("productIdx"),
                        rs.getString("productImg"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("directAddress"),
                        rs.getString("securePayment"),
                        rs.getString("myFavorite"),
                        rs.getString("createdAt"),
                        rs.getString("favoriteCount")
                ), getProductParams);
    }

    public StoreInfo getStoreInfos(int productId) {
        String getStoreInfoQuery = "select P.user_id as storeId, shop_name as storeName,\n" +
                "       case when fCount is not null then fCount else 0 end as followerCount,\n" +
                "       case when rate is not null then rate else 0 end as starRate,\n" +
                "       case when rCount is not null then rCount else 0 end as reviewCount\n" +
                "from Products P\n" +
                "inner join Users U on P.user_id = U.user_id\n" +
                "left join (select following_user_id, count(*) as fCount from Following group by following_user_id) as F on U.user_id = F.following_user_id\n" +
                "left join (select store_id, ROUND(SUM(rate) / COUNT(store_id),1) as rate, count(*) as rCount from Reviews group by store_id)as R on U.user_id = R.store_id\n" +
                "where product_id=?";
        int getStoreInfoParams = productId;
        return this.jdbcTemplate.queryForObject(getStoreInfoQuery,
                (rs, rowNum) -> new StoreInfo(
                        rs.getInt("storeId"),
                        rs.getString("storeName"),
                        rs.getInt("followerCount"),
                        rs.getDouble("starRate"),
                        rs.getInt("reviewCount")

                ),
                getStoreInfoParams);
    }

    public List<SellProduct> getSellProducts(int storeId) {
        String getSellProductsQuery = "select  product_id as productIdx, price,\n" +
                "       (select product_image_url from ProductImages where product_id = product_id limit 1) as productImgUrl\n" +
                "from Products P\n" +
                "where user_id=? limit 6";
        int getSellProductsParams = storeId;
        return this.jdbcTemplate.query(getSellProductsQuery,
                (rs, rowNum) -> new SellProduct(
                        rs.getInt("productIdx"),
                        rs.getString("productImgUrl"),
                        rs.getInt("price")


                ),
                getSellProductsParams);
    }

    public List<RelateProduct> getRelateProducts(int categoryId, int productId) {
        String getRelateProductsQuery = "select  product_id as productIdx, price,product_title as title, (select product_image_url from ProductImages where product_id = product_id limit 1) as productImgUrl\n" +
                "                                        from Products P inner join CategorySmall CS on P.category_small_id = CS.category_small_id\n" +
                "                                where CS.category_small_id = ? and product_id != ? limit 18";
        Object[] getRelateProductsParams = new Object[]{categoryId, productId};
        return this.jdbcTemplate.query(getRelateProductsQuery,
                (rs, rowNum) -> new RelateProduct(
                        rs.getInt("productIdx"),
                        rs.getString("productImgUrl"),
                        rs.getString("title"),
                        rs.getInt("price")

                ),
                getRelateProductsParams);
    }

    public List<Review> getReviews(int storeId) {
        String getReviewsQuery = "select U.profile_Url as profileUrl, U.shop_name as storeName, product_title as title,rate,text as explanation,secure_payment as securePayment,\n" +
                "       (case when timestampdiff(second , R.createdAt, current_timestamp) <60\n" +
                "                                then concat(timestampdiff(second, R.createdAt, current_timestamp),'초 전')\n" +
                "                            when timestampdiff(minute , R.createdAt, current_timestamp) <60\n" +
                "                                then concat(timestampdiff(minute, R.createdAt, current_timestamp),'분 전')\n" +
                "                            when timestampdiff(hour, R.createdAt, current_timestamp) <24\n" +
                "                                then concat(timestampdiff(hour, R.createdAt, current_timestamp),'시간 전')\n" +
                "                            else concat(datediff( current_timestamp, R.createdAt),'일 전')\n" +
                "                            end) as createdAt\n" +
                "                from Reviews R\n" +
                "                join Products P on P.product_id = R.product_id\n" +
                "                left  join Users U on R.user_id = U.user_id\n" +
                "                where R.store_id=? limit 2";
        int getReviewsParams = storeId;
        return this.jdbcTemplate.query(getReviewsQuery,
                (rs, rowNum) -> new Review(
                        rs.getString("profileUrl"),
                        rs.getString("storeName"),
                        rs.getString("title"),
                        rs.getDouble("rate"),
                        rs.getString("explanation"),
                        rs.getString("securePayment")
                ),
                getReviewsParams);
    }

    public List<GetCategoryRes> getLargeCategories() {
        String getCategoryQuery = "select category_large_id as categoryIdx, category_icon_image_Url as categoryUrl, category_large_name as categoryName\n" +
                "from CategoryLarge\n" +
                "where icon_type='CATEGORY'";
        return this.jdbcTemplate.query(getCategoryQuery,
                (rs, rowNum) -> new GetCategoryRes(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryUrl"),
                        rs.getString("categoryName")
                ));
    }
    public List<GetCategoryRes> getMiddleCategories(int categoryId) {
        String getCategoryQuery = "select category_middle_id as categoryIdx, category_icon_image_Url as categoryUrl, category_middle_name as categoryName from CategoryMiddle where category_large_id=?";
        int getCategoryParams = categoryId;
        return this.jdbcTemplate.query(getCategoryQuery,
                (rs, rowNum) -> new GetCategoryRes(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryUrl"),
                        rs.getString("categoryName")
                ), getCategoryParams);
    }
    public List<GetCategoryRes> getSmallCategories(int categoryId) {
        String getCategoryQuery = "select category_small_id as categoryIdx, category_icon_image_Url as categoryUrl, category_small_name as categoryName from CategorySmall where category_middle_id=?";
        int getCategoryParams = categoryId;
        return this.jdbcTemplate.query(getCategoryQuery,
                (rs, rowNum) -> new GetCategoryRes(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryUrl"),
                        rs.getString("categoryName")
                ), getCategoryParams);
    }

    public List<GetInquiryRes> getInquiries(int productId) {
        String getInquiryQuery = "select product_inquiry_id as inquiryId, shop_name as storeName, text, profile_Url as profileUrl,\n" +
                "                       (case when timestampdiff(second , PI.createdAt, current_timestamp) <60\n" +
                "                                then concat(timestampdiff(second, PI.createdAt, current_timestamp),'초 전')\n" +
                "                            when timestampdiff(minute , PI.createdAt, current_timestamp) <60\n" +
                "                                then concat(timestampdiff(minute, PI.createdAt, current_timestamp),'분 전')\n" +
                "                            when timestampdiff(hour, PI.createdAt, current_timestamp) <24\n" +
                "                                then concat(timestampdiff(hour, PI.createdAt, current_timestamp),'시간 전')\n" +
                "                            else concat(datediff( current_timestamp, PI.createdAt),'일 전')\n" +
                "                            end) as createdAt\n" +
                "                       from ProductInquiry PI, Users\n" +
                "                        where product_id=? and PI.user_id = Users.user_id";
        int getInquiryParams = productId;
        return this.jdbcTemplate.query(getInquiryQuery,
                (rs, rowNum) -> new GetInquiryRes(
                        rs.getInt("inquiryId"),
                        rs.getString("storeName"),
                        rs.getString("createdAt"),
                        rs.getString("text"),
                        rs.getString("profileUrl")
                ), getInquiryParams);
    }

    public void createInquiry(int userId, int productId, PostInquiryReq postInquiryReq) {
        String createInquiryQuery = "insert into ProductInquiry(user_id, product_id, text) values(?,?,?)";
        Object[] createInquiryParams = new Object[]{userId, productId, postInquiryReq.getText()};
        this.jdbcTemplate.update(createInquiryQuery, createInquiryParams);
    }

    public String getInquiryCall(int prductId, int inquiryId) {
        String getInquiryCallQuery = "select shop_name from ProductInquiry PI\n" +
                "inner join Users U on PI.user_id = U.user_id\n" +
                "where PI.product_id=? and PI.product_inquiry_id=?";
        Object[] getInquiryCallParams = new Object[]{prductId, inquiryId};
        return this.jdbcTemplate.queryForObject(getInquiryCallQuery, String.class, getInquiryCallParams);
    }


    public int checkInquiry(int userId, int inquiryId, int productId){
        String checkInquiryIdQuery = "select(exists(select * from ProductInquiry where user_id = ? and product_inquiry_id = ? and product_id=?))";
        Object[] checkInquiryIdParams = new Object[]{userId, inquiryId, productId};
        return this.jdbcTemplate.queryForObject(checkInquiryIdQuery, int.class, checkInquiryIdParams);
    }

    public void deleteInquiry(int userId, int inquiryId, int productId){
        String deleteInquiryQuery = "delete from ProductInquiry where user_id = ? and product_inquiry_id = ? and product_id=?";
        Object[] deleteInquiryParams = new Object[]{userId, inquiryId, productId};
        this.jdbcTemplate.update(deleteInquiryQuery, deleteInquiryParams);
    }


    public int checkSellStatus(int userId, int productId){
        String checkLikesQuery = "select(exists(select * from Products where user_id = ? and product_id = ?))";
        Object[] checkLikesParams = new Object[]{userId, productId};
        return this.jdbcTemplate.queryForObject(checkLikesQuery, int.class, checkLikesParams);
    }


    public void changeSellingStatus(int userId, int productId){
        String changeStatusQuery = "update Products set sell_status = 'SELLING' where user_id=? and product_id=?";
        Object[] changeStatusParams = new Object[]{userId, productId};
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }

    public void changeReservedStatus(int userId, int productId){
        String changeStatusQuery = "update Products set sell_status = 'RESERVED' where user_id=? and product_id=?";
        Object[] changeStatusParams = new Object[]{userId, productId};
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }
    public void changeSoldoutStatus(int userId, int productId){
        String changeStatusQuery = "update Products set sell_status = 'SOLDOUT' where user_id=? and product_id=?";
        Object[] changeStatusParams = new Object[]{userId, productId};
        this.jdbcTemplate.update(changeStatusQuery, changeStatusParams);
    }

    public String getMainDirectAddress(int userId) {
        String getMainAddressQuery = "select case when count(*)=0  then '지역정보 없음' else address end from Address where user_id = ? and main='MAIN' and address_type='DIRECT'";
        int getMainAddressParams = userId;
        return this.jdbcTemplate.queryForObject(getMainAddressQuery, String.class, getMainAddressParams);
    }

    public void updateViewCount(int productId){
        String updateViewCountQuery = "update Products set view_count = view_count+1 where product_id=?";
        int updateViewCountParams =  productId;
        this.jdbcTemplate.update(updateViewCountQuery, updateViewCountParams);
    }

}