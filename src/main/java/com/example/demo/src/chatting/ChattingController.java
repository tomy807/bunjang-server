package com.example.demo.src.chatting;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chatting.model.GetChattingRoomRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.NOT_EXIST_CHATTING_ROOM;

@RestController
@RequestMapping("/app/chattings")
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingProvider chattingProvider;
    private final JwtService jwtService;


    @GetMapping("")
    public BaseResponse<List<GetChattingRoomRes>> getChattingRooms() {
        try {

            int userIdx = jwtService.getUserIdx();

            if (chattingProvider.checkChattingRooms(userIdx)==0) {
                return new BaseResponse<>(NOT_EXIST_CHATTING_ROOM);
            }

            List<GetChattingRoomRes> chattingRooms = chattingProvider.getChattingRooms(userIdx);

            return new BaseResponse<>(chattingRooms);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}