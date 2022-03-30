package com.example.demo.src.address;

import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.GetDirectAddressRes;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.src.address.model.PostDirectAddressReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int createAddress(PostAddressReq postAddressReq, int userIdx) {
        Object[] createAddressParams = new Object[]{userIdx, postAddressReq.getName(), postAddressReq.getPhone(), postAddressReq.getAddress(), postAddressReq.getDetailAddress(),postAddressReq.getMain()};
        return jdbcTemplate.update("insert into Address(user_id, name, phone, address, detail_address,main) VALUE (?,?,?,?,?,?)", createAddressParams);
    }

    public List<GetAddressRes> getAddress(int userIdx) {
        return jdbcTemplate.query("select address_id,name,phone,address,detail_address,main from Address where user_id=? and main='MAIN'",
                (rs, rowNum) -> new GetAddressRes(
                        rs.getInt("address_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("detail_address"),
                        rs.getString("main")
                ), userIdx);
    }

    public void modifyAddressMain(Integer addressIdx) {
        jdbcTemplate.update("update Address set main='SUB' where address_id=?", addressIdx);
    }

    public void createDirectAddress(PostDirectAddressReq postDirectAddressReq, int userIdx) {
        Object[] createDirectAddressParams = new Object[]{userIdx, postDirectAddressReq.getDirectAddress(), "DIRECT"};
        jdbcTemplate.update("insert into Address(user_id,address,address_type,main) VALUE (?,?,?,'MAIN')", createDirectAddressParams);
    }

    public List<GetDirectAddressRes> getDirectAddresses(int userIdx) {
        return jdbcTemplate.query("select address,main from Address where user_id=? and address_type='DIRECT'",
                (rs, rowNum) -> new GetDirectAddressRes(
                        rs.getString("address"),
                        rs.getString("main")), userIdx);
    }

    public String getMainDirectAddress(int userId) {
        String getMainAddressQuery = "select case when count(*)=0  then '지역정보 없음' else address end from Address where user_id = ? and main='MAIN' and address_type='DIRECT'";
        int getMainAddressParams = userId;
        return this.jdbcTemplate.queryForObject(getMainAddressQuery, String.class, getMainAddressParams);
    }

    public int checkUserStatusByUserId(int userId) {
        String checkUserStatusByUserIdQuery = "select exists(select * from Users where user_id = ? and status = 'ACTIVE')";
        int checkUserStatusByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserStatusByUserIdQuery, int.class, checkUserStatusByUserIdParams);
    }

    public void cleanDirectAddress(int userIdx) {
        Integer addressIdx = jdbcTemplate.queryForObject("select case when count(*)=0 then 0 else address_id end from Address where user_id=? and main='MAIN' and address_type='DIRECT'", int.class, userIdx);
        if (addressIdx != 0) {
            jdbcTemplate.update("update Address set main='SUB' where address_id=?", addressIdx);

        }
    }
}
