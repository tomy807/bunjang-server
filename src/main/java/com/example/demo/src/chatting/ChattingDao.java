package com.example.demo.src.chatting;

import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.GetFirstMessageRes;
import com.example.demo.src.chatting.model.GetChattingRoomRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChattingDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetChattingRoomRes> getChattingRooms(int userIdx) {

        return jdbcTemplate.query("select CRJ.user_id as shopIdx,CRJ.chat_room_id as chatRoomIdx, shop_name\n" +
                        "from ChatRoomJoin\n" +
                        "         inner join ChatRoomJoin CRJ on ChatRoomJoin.chat_room_id = CRJ.chat_room_id\n" +
                        "         inner join Users U on CRJ.user_id = U.user_id\n" +
                        "where ChatRoomJoin.user_id = ?\n" +
                        "  and CRJ.user_id != ?\n" +
                        "group by CRJ.user_id",
                (rs, rowNum) -> new GetChattingRoomRes(
                        rs.getInt("shopIdx"),
                        rs.getInt("chatRoomIdx"),
                        rs.getString("shop_name"),
                        null, null
                ), userIdx, userIdx);
    }


    public GetFirstMessageRes getMessage(int chatRoomIdx) {

        return jdbcTemplate.queryForObject("select message,date_format(createdAt,'%m월 %d일 %H:%i') as createdAt from ChatMessage where chat_room_id = ? order by createdAt desc limit 1 ",
                (rs, rowNum) -> new GetFirstMessageRes(rs.getString("message"), rs.getString("createdAt")), chatRoomIdx);

    }

    public int checkChattingRooms(int userIdx) {
        return jdbcTemplate.queryForObject("select count(CRJ.chat_room_id) as roomCount\n" +
                "from ChatRoomJoin\n" +
                "         inner join ChatRoomJoin CRJ on ChatRoomJoin.chat_room_id = CRJ.chat_room_id\n" +
                "where ChatRoomJoin.user_id = ?\n" +
                "  and CRJ.user_id != ?", int.class, userIdx, userIdx);
    }
}