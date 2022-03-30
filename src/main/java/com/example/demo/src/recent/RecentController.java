package com.example.demo.src.recent;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.GetFavoriteRes;
import com.example.demo.src.recent.model.GetRecentRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/recent")
@RequiredArgsConstructor
public class RecentController {

    private final RecentProvider recentProvider;
    private final JwtService jwtService;

    @GetMapping("")
    public BaseResponse<List<GetRecentRes>> getRecents() {
        try {
            int userIdx = jwtService.getUserIdx();
            List<GetRecentRes> recents = recentProvider.getRecents(userIdx);
            return new BaseResponse<>(recents);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
