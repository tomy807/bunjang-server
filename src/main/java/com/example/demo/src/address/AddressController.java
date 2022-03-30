package com.example.demo.src.address;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.GetDirectAddressRes;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.src.address.model.PostDirectAddressReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AddressProvider addressProvider;
    private final JwtService jwtService;

    @PostMapping("/delivery")
    public BaseResponse<String> createAddress(@RequestBody PostAddressReq postAddressReq) {

        if (postAddressReq.getName() == null) {
            return new BaseResponse<>(POST_ADDRESS_EMPTY_NAME);
        }
        if (postAddressReq.getPhone() == null) {
            return new BaseResponse<>(POST_ADDRESS_EMPTY_PHONE);
        }
        if (postAddressReq.getAddress() == null) {
            return new BaseResponse<>(POST_ADDRESS_EMPTY_ADDRESS);
        }
        if (postAddressReq.getDetailAddress() == null) {
            return new BaseResponse<>(POST_ADDRESS_EMPTY_DETAIL_ADDRESS);
        }

        try {
            int userIdx = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인

            addressService.createAddress(postAddressReq, userIdx);

            return new BaseResponse<>("주소가 추가되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/direct")
    public BaseResponse<String> createDirectAddress(@RequestBody PostDirectAddressReq postDirectAddressReq) {

        if (postDirectAddressReq.getDirectAddress() == null) {
            return new BaseResponse<>(POST_ADDRESS_EMPTY_DIRECT_ADDRESS);
        }

        try {
            int userIdx = jwtService.getUserIdx();
//            userIdx와 접근한 유저가 같은지 확인

            addressService.createDirectAddress(postDirectAddressReq, userIdx);

            return new BaseResponse<>("직거래 주소가 추가되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/clean")
    public BaseResponse<String> cleanDirectAddress() {
        try{
            int userIdx = jwtService.getUserIdx();

            if (addressProvider.checkUserStatusByUserId(userIdx) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }
            addressService.cleanDirectAddress(userIdx);

            return new BaseResponse<>("지역설정안함");

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @GetMapping("/direct")
    public BaseResponse<List<GetDirectAddressRes>> getDirectAddresses() {
        try{
            int userIdx = jwtService.getUserIdx();

            if (addressProvider.checkUserStatusByUserId(userIdx) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            List<GetDirectAddressRes> directAddresses = addressProvider.getDirectAddresses(userIdx);

            return new BaseResponse<>(directAddresses);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 메인 화면 - 주소 표시 API
     * [GET] /addresses/mainDirect
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @GetMapping("/mainDirect")
    public BaseResponse<String> getMainDirectAddress() {
        try {
            // jwt 에서 userId 추출.
            int userIdByJwt = jwtService.getUserIdx();

            if (addressProvider.checkUserStatusByUserId(userIdByJwt) == 0) {
                return new BaseResponse<>(DELETED_USER);
            }

            return new BaseResponse<>(addressProvider.getMainDirectAddress(userIdByJwt));
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
