package com.example.demo.src.event;

import com.example.demo.src.event.model.GetEventRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EventDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetEventRes> getEvents() {

        return jdbcTemplate.query("select * from Event",
                (rs, rowNum) -> new GetEventRes(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("event_image_url"),
                        rs.getString("event_text"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                ));
    }

    public GetEventRes getEvent(int eventIdx) {
        return jdbcTemplate.queryForObject("select * from Event where event_id=?",
                (rs, rowNum) -> new GetEventRes(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("event_image_url"),
                        rs.getString("event_text"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                ), eventIdx);
    }
}
