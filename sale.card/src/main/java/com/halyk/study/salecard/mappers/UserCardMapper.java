package com.halyk.study.salecard.mappers;

import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.entity.UserCard;
import org.springframework.stereotype.Component;

@Component
public class UserCardMapper {

    public static Response mapToUserCardResponse(UserCard userCard) {
        Response response = ProductMapper.mapToCardResponse(userCard.getProduct());

        response.setUserCardId(userCard.getId());

        return response;
    }
}
