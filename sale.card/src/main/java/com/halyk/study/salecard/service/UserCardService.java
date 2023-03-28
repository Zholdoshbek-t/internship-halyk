package com.halyk.study.salecard.service;

import com.halyk.study.salecard.dto.Request;
import com.halyk.study.salecard.dto.Response;

import java.awt.image.BufferedImage;

public interface UserCardService {

    Response getUserCards(Request request);

    Response getUserCardsSortedByName(Request request);

    Response getUserCardsSortedByDate(Request request);

    BufferedImage getBarcode(Request request);

    Response createUserCard(Request request);

    Response updateUserCard(Request request);

    Response deleteUserCard(Request request);
}
