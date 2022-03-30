package com.example.demo.src.chatting.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetChattingRoomRes {

    private Integer shopIdx;
    private Integer chatRoomIdx;
    private String shopName;
    private String recentMessageTime;
    private String recentMessage;

}
