package com.halyk.study.finalboss.services;

import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.entities.ErrorMessage;
import com.halyk.study.finalboss.entities.Url;
import java.time.LocalDate;
import java.util.List;

public interface ErrorMessageService {

    void saveErrorMessage(Url url, int code);

    Response getTopNErrorMessagesBetweenTwoDates(LocalDate from, LocalDate till, int limitNumber);

    Response getAllErrorMessagesBetweenTwoDates(LocalDate from, LocalDate till);

    void getExcelBetweenTwoDates(LocalDate from, LocalDate till);

    Response findAllByApiServiceUrlDateFromTillLimit(Long chatId, LocalDate from,
                                                               LocalDate till, Integer limitNumber);

    Response findAllByApiServiceUrlDateFromTill(Long chatId, LocalDate from, LocalDate till);
}
