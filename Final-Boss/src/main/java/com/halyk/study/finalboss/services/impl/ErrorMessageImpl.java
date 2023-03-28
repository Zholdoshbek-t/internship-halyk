package com.halyk.study.finalboss.services.impl;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.dtos.enums.Status;
import com.halyk.study.finalboss.entities.ApiService;
import com.halyk.study.finalboss.entities.Chat;
import com.halyk.study.finalboss.entities.ErrorMessage;
import com.halyk.study.finalboss.entities.Url;
import com.halyk.study.finalboss.mappers.ErrorMessageMapper;
import com.halyk.study.finalboss.repositories.ChatRepository;
import com.halyk.study.finalboss.repositories.ErrorMessageRepository;
import com.halyk.study.finalboss.services.ErrorMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ErrorMessageImpl implements ErrorMessageService {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final ErrorMessageRepository errorMessageRepository;

    private final ChatRepository chatRepository;


    @Override
    public void saveErrorMessage(Url url, int code) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .url(url)
                .code(code)
                .createdAt(LocalDateTime.now())
                .build();

        errorMessageRepository.save(errorMessage);
    }

    @Override
    public Response getTopNErrorMessagesBetweenTwoDates(LocalDate from, LocalDate till, int limitNumber) {
        List<ErrorMessage> messages = errorMessageRepository.findAllByTwoDatesLimit(from, till, limitNumber);

        return Response.builder()
                .message(String.format("Here you can see newest %d error messages between %s and %s." +
                                "\nThe total number of error messages is %d.", limitNumber,
                        DATE_FORMATTER.format(from), DATE_FORMATTER.format(till), messages.size()))
                .status(Status.SUCCESS)
                .errorMessageResponses(
                        messages.stream()
                                .map(ErrorMessageMapper::createErrorMessageResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public Response getAllErrorMessagesBetweenTwoDates(LocalDate from, LocalDate till) {
        List<ErrorMessage> messages = errorMessageRepository.findAllByTwoDates(from, till);

        return Response.builder()
                .message(String.format("Here you can see all error messages between %s and %s." +
                                "\nThe total number of error messages is %d.",
                        DATE_FORMATTER.format(from), DATE_FORMATTER.format(till), messages.size()))
                .status(Status.SUCCESS)
                .errorMessageResponses(
                        messages.stream()
                                .map(ErrorMessageMapper::createErrorMessageResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public void getExcelBetweenTwoDates(LocalDate from, LocalDate till) {

    }

    @Override
    public Response findAllByApiServiceUrlDateFromTillLimit(Long chatId, LocalDate from,
                                                                  LocalDate till, Integer limitNumber) {
        Optional<Chat> chat = chatRepository.findByChatId(chatId);

        if (chat.isPresent()) {
            List<ErrorMessage> messages = errorMessageRepository
                    .findAllByApiServiceUrlDateFromTillLimit(
                            chat.get().getApiServices()
                                    .stream()
                                    .map(ApiService::getId)
                                    .collect(Collectors.toList()),
                            from, till, limitNumber);

            return Response.builder()
                    .message(String.format("Here you can see newest %d error messages between %s and %s." +
                                    "\nThe total number of error messages is %d.",
                            limitNumber, DATE_FORMATTER.format(from), DATE_FORMATTER.format(till), messages.size()))
                    .status(Status.SUCCESS)
                    .errorMessageResponses(
                            messages.stream()
                                    .map(ErrorMessageMapper::createErrorMessageResponse)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }

        return getTopNErrorMessagesBetweenTwoDates(from, till, limitNumber);
    }

    @Override
    public Response findAllByApiServiceUrlDateFromTill(Long chatId, LocalDate from,
                                                             LocalDate till) {
        Optional<Chat> chat = chatRepository.findByChatId(chatId);

        if (chat.isPresent()) {
            List<ErrorMessage> messages = errorMessageRepository
                    .findAllByApiServiceUrlDateFromTill(
                            chat.get().getApiServices()
                                    .stream()
                                    .map(ApiService::getId)
                                    .collect(Collectors.toList()),
                            from, till);

            return Response.builder()
                    .message(String.format("Here you can see all error messages according to your" +
                                    "API Service subscribes between %s and %s." +
                                    "\nThe total number of error messages is %d.",
                            DATE_FORMATTER.format(from), DATE_FORMATTER.format(till), messages.size()))
                    .status(Status.SUCCESS)
                    .errorMessageResponses(
                            messages.stream()
                                    .map(ErrorMessageMapper::createErrorMessageResponse)
                                    .collect(Collectors.toList())
                    )
                    .build();
        }

        return getAllErrorMessagesBetweenTwoDates(from, till);
    }
}
