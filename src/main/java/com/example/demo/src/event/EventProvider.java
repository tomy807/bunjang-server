package com.example.demo.src.event;


import com.example.demo.config.BaseException;
import com.example.demo.src.event.model.GetEventRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class EventProvider {

    private final EventDao eventDao;


    public List<GetEventRes> getEvents() throws BaseException {
        try{
            List<GetEventRes> getEventsRes = eventDao.getEvents();
            return getEventsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetEventRes getEvent(int eventIdx) throws BaseException {
        try{
            GetEventRes getEventRes = eventDao.getEvent(eventIdx);
            return getEventRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
