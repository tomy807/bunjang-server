package com.example.demo.src.chatting;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.GetDirectAddressRes;
import com.example.demo.src.address.model.GetFirstMessageRes;
import com.example.demo.src.chatting.model.GetChattingRoomRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class ChattingProvider {

    private final ChattingDao chattingDao;

    public List<GetChattingRoomRes> getChattingRooms(int userIdx) throws BaseException {

        try {

            List<GetChattingRoomRes> chattingRooms = chattingDao.getChattingRooms(userIdx);
            for (GetChattingRoomRes chattingRoom : chattingRooms) {
                int chatRoomIdx = chattingRoom.getChatRoomIdx();
                GetFirstMessageRes message = chattingDao.getMessage(chatRoomIdx);
                chattingRoom.setRecentMessageTime(message.getCreatedAt());
                chattingRoom.setRecentMessage(message.getFirstMessage());
            }
            return chattingRooms;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkChattingRooms(int userIdx) throws BaseException {
        try {
            return chattingDao.checkChattingRooms(userIdx);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}