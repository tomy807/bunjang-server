package com.example.demo.src.address;


import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.GetDirectAddressRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class AddressProvider {

    private final AddressDao addressDao;
    public List<GetDirectAddressRes> getDirectAddresses(int userIdx) throws BaseException {
        try {

            List<GetDirectAddressRes> addresses = addressDao.getDirectAddresses(userIdx);
            return addresses;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getMainDirectAddress(int userId) throws BaseException {
        try {
            return addressDao.getMainDirectAddress(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserStatusByUserId(int userId) throws BaseException {
        try {
            return addressDao.checkUserStatusByUserId(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
