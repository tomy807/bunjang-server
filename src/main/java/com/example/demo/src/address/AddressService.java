package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.src.address.model.PostDirectAddressReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressDao addressDao;


    /**
     *
     * @param postAddressReq
     * @param userIdx
     * @throws BaseException
     * 기존에 MAIN 주소가 있고 새로 만들 주소를 MAIN으로 바꾸면 원래의 MAIN은 SUB로 바뀜
     */
    public void createAddress(PostAddressReq postAddressReq, int userIdx) throws BaseException {
        try {

            List<GetAddressRes> mainAddress = addressDao.getAddress(userIdx);
            if (Objects.equals(postAddressReq.getMain(), "MAIN") && !mainAddress.isEmpty()) {

                addressDao.modifyAddressMain(mainAddress.get(0).getAddressIdx());
            }
            addressDao.createAddress(postAddressReq, userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createDirectAddress(PostDirectAddressReq postDirectAddressReq, int userIdx) throws BaseException {
        try {
            cleanDirectAddress(userIdx);
            addressDao.createDirectAddress(postDirectAddressReq, userIdx);

        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void cleanDirectAddress(int userIdx) throws BaseException {
        try {
            addressDao.cleanDirectAddress(userIdx);

        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
