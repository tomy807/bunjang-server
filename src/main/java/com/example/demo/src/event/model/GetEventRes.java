package com.example.demo.src.event.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetEventRes {

    private Integer eventIdx;
    private String eventName;
    private String eventImageUrl;
    private String eventText;
    private Date startDate;
    private Date endDate;

}
