package com.example.demo.src.event;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.event.model.GetEventRes;
import com.example.demo.src.user.model.GetUserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/events")
@RequiredArgsConstructor
public class EventController {

    private final EventProvider eventProvider;

    @GetMapping("")
    public BaseResponse<List<GetEventRes>> getEvents() {

        try {
            List<GetEventRes> events = eventProvider.getEvents();

            return new BaseResponse<>(events);
        } catch (BaseException exception) {

            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("{eventIdx}")
    public BaseResponse<GetEventRes> getEvent(@PathVariable int eventIdx) {
        try {
            GetEventRes events = eventProvider.getEvent(eventIdx);

            return new BaseResponse<>(events);
        } catch (BaseException exception) {

            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
