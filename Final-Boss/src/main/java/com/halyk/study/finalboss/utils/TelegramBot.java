package com.halyk.study.finalboss.utils;

import com.halyk.study.finalboss.configs.BotConfig;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.entities.ApiService;
import com.halyk.study.finalboss.entities.Chat;
import com.halyk.study.finalboss.repositories.ApiServiceRepository;
import com.halyk.study.finalboss.repositories.ChatRepository;
import com.halyk.study.finalboss.services.ChatService;
import com.halyk.study.finalboss.services.ErrorMessageService;
import com.halyk.study.finalboss.services.TelegramMessageService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    public static final DateTimeFormatter DATE_TIME_FILE_SAVE = DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm_ss");

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBot.class);

    private static final Map<Long, Boolean> chatIdRemoving = new HashMap<>();

    private static final Map<Long, Boolean> chatIdAdding = new HashMap<>();

    private static final Map<Long, Boolean> chatIdTwoDatesTop = new HashMap<>();
    // change message that calling another method will finish these two methods
    // add methods local messages
    private static final Map<Long, Boolean> chatIdTwoDatesAll = new HashMap<>();
    // hint: if you did not add any service, get will return empty
    private static final Map<Long, Boolean> chatIdTwoDatesReport = new HashMap<>();

    private static final Map<Long, LocalDate> chatIdDateFrom = new HashMap<>();

    private static final Map<Long, LocalDate> chatIdDateTill = new HashMap<>();

    private static final Map<Long, Integer> chatIdLimit = new HashMap<>();

    private final TelegramMessageService telegramMessageService;

    private final ApiServiceRepository apiServiceRepository;

    private final ErrorMessageService errorMessageService;

    private final ChatRepository chatRepository;

    private final ChatService chatService;

    private final BotConfig botConfig;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    sendMessage(chatId, telegramMessageService.getHelpMessage().getTelegramMsgText());
                    break;
                case "/addservice":
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    checkAddService(chatId);
                    sendAddServiceMessage(chatId, telegramMessageService.getMessageByNameTg("ADD_SERVICE"));
                    break;
                case "/removeservice":
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    checkRemoveService(chatId);
                    sendRemoveServiceMessage(chatId, telegramMessageService.getMessageByNameTg("REMOVE_SERVICE"));
                    break;
                case "/gettop": // print to enter limit number -> date from -> date till
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    checkTwoDatesTop(chatId);
                    sendMessage(chatId, telegramMessageService.getMessageByNameTg("TWO_DATES_TOP_N"));
                    break;
                case "/getall":
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    checkTwoDatesAll(chatId);
                    sendMessage(chatId, telegramMessageService.getMessageByNameTg("TWO_DATES_ALL"));
                    break;
                case "/getreport":
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    checkTwoDatesReport(chatId);
                    sendMessage(chatId, telegramMessageService.getMessageByNameTg("GET_REPORT"));
                    break;
                case "/stop":  // not sending but services are attached
                    stopAddingRemoving(chatId);
                    stopDatesTopAllReport(chatId);
                    break;
                default:
                    if (chatIdTwoDatesAll.get(chatId)) {
                        addDate(chatId, message);
                    } else if (chatIdTwoDatesTop.get(chatId)) {
                        if (chatIdLimit.get(chatId) == null) {
                            addLimit(chatId, message);
                        } else {
                            addDate(chatId, message);
                        }
                    } else if (chatIdTwoDatesReport.get(chatId)) {
                        addDate(chatId, message);
                    } else {
                        stopAddingRemoving(chatId);
                        stopDatesTopAllReport(chatId);
                        sendMessage(chatId, "Sorry, command was not recognized.");
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String callDate = update.getCallbackQuery().getData(); //CRUD
//            System.out.println(update.getCallbackQuery().getMessage().getMessageId());
            long chatId = update.getCallbackQuery().getMessage().getChatId(); //412412597

            if (chatIdAdding.get(chatId)) {
                Chat chat = chatService.getChatByChatId(chatId);

                if (callDate.equals("STOP")) {
                    chatIdAdding.put(chatId, false);
                    sendMessage(chatId, "Add Service process was stopped.");
                } else {
                    Optional<ApiService> apiService = apiServiceRepository.findByName(callDate);

                    if (apiService.isPresent()) {
                        boolean add = true;
                        for (ApiService ap : chat.getApiServices()) {
                            if (ap.getName().equals(apiService.get().getName())) {
                                add = false;
                                break;
                            }
                        }
                        if (add) {
                            chat.getApiServices().add(apiService.get());
                            chatRepository.save(chat);
                            sendMessage(chatId, callDate + " Service was added.");
                        } else {
                            sendMessage(chatId, callDate + " Service is already added.");
                        }
                    }
                }
            } else if (chatIdRemoving.get(chatId)) {
                Chat chat = chatService.getChatByChatId(chatId);

                if (callDate.equals("STOP")) {
                    chatIdRemoving.put(chatId, false);
                    sendMessage(chatId, "Remove Service process was stopped.");
                } else {
                    Optional<ApiService> apiService = apiServiceRepository.findByName(callDate);

                    if (apiService.isPresent()) {
                        boolean remove = false;
                        for (ApiService ap : chat.getApiServices()) {
                            if (ap.getName().equals(apiService.get().getName())) {
                                chat.getApiServices().remove(ap);
                                chatRepository.save(chat);
                                sendMessage(chatId, callDate + " Service was removed.");
                                remove = true;
                                break;
                            }
                        }
                        if (!remove) {
                            sendMessage(chatId, callDate + " Service is already removed.");
                        }
                    }
                }
            }
        }
    }

    private void startCommand(long chatId, String name) {
        String answer = "Hello, " + name + ", nice to meet you! " +
                "I'm gonna send you message if there is any fail URI. " +
                "If you want to stop me sending you messages, enter /stop command. " +
                "Enter /help in order to get methods instruction.";

        sendMessage(chatId, answer);

        chatService.createChat(chatId);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.info("Could not send message due to TelegramApiException");
        }
    }

    private void sendAddServiceMessage(long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        Chat chat = chatService.getChatByChatId(chatId);

        List<Long> apiServiceIds = apiServiceRepository.getApiServiceIdsNotByChatId(chat.getId());

        if (apiServiceIds.isEmpty()) {
            addButtonsToInlineKeyboard(message, apiServiceRepository.findAllByOrderByName());
        } else {
            addButtonsToInlineKeyboard(message, apiServiceRepository.findAllByIds(apiServiceIds));
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.info("Could not send message due to TelegramApiException");
        }
    }

    private void sendRemoveServiceMessage(long chatId, String text) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        Chat chat = chatService.getChatByChatId(chatId);

        addButtonsToInlineKeyboard(message, chat.getApiServices());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            LOGGER.info("Could not send message due to TelegramApiException");
        }
    }

    private void addButtonsToInlineKeyboard(SendMessage message, List<ApiService> apiServices) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (int i = 0; i < apiServices.size(); i += 3) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            for (int k = 0; k < 3; k++) {
                if ((i + k) >= apiServices.size()) {
                    break;
                }
                InlineKeyboardButton button = new InlineKeyboardButton(apiServices.get(i + k).getName());
                button.setCallbackData(apiServices.get(i + k).getName());
                rowInline.add(button);
            }

            rowsInline.add(rowInline);
        }

        List<InlineKeyboardButton> stopRowLine = new ArrayList<>();

        InlineKeyboardButton stopButton = new InlineKeyboardButton("STOP");
        stopButton.setCallbackData("STOP");
        stopRowLine.add(stopButton);

        rowsInline.add(stopRowLine);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
    }

    public void sendExceptionMessage(ApiService apiService, String uri, int statusCode) {
        String text = String.format("Test for: %s service was failed;\nURI: %s;\n" +
                        "Status code: %d;\nDate and Time: %s.",
                apiService.getName(), uri, statusCode, DATE_TIME_FORMATTER.format(LocalDateTime.now()));

        sendGeneralExceptionMessage(apiService, text);
    }

    public void sendExceptionMessage(ApiService apiService, String uri) {
        String text = String.format("Test for: %s service was failed;\nURI: %s;\n" +
                        "Date and Time: %s.",
                apiService.getName(), uri, DATE_TIME_FORMATTER.format(LocalDateTime.now()));

        sendGeneralExceptionMessage(apiService, text);
    }

    private void sendGeneralExceptionMessage(ApiService apiService, String text) {
        for (Chat chat : chatRepository.getChatIdsByApiService(apiService.getId())) {
            if (!chatIdAdding.get(chat.getChatId()) && !chatIdRemoving.get(chat.getChatId())
                    && !chatIdTwoDatesTop.get(chat.getChatId()) && !chatIdTwoDatesAll.get(chat.getChatId())
                    && !chatIdTwoDatesReport.get(chat.getChatId())) {
                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chat.getChatId()));
                message.setText(text);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    LOGGER.info("Could not send exception message due to TelegramApiException");
                }
            }
        }
    }

    private void stopAddingRemoving(Long chatId) {
        if (chatIdAdding.containsKey(chatId)) {
            if (chatIdAdding.get(chatId)) {
                chatIdAdding.put(chatId, false);
                sendMessage(chatId, "Add Service process was stopped.");
            } else if (chatIdRemoving.get(chatId)) {
                chatIdRemoving.put(chatId, false);
                sendMessage(chatId, "Remove Service process was stopped.");
            }
        } else {
            chatIdAdding.put(chatId, false);
            chatIdRemoving.put(chatId, false);
        }
    }

    private void checkAddService(Long chatId) {
        if (chatIdRemoving.get(chatId)) {
            chatIdRemoving.put(chatId, false);
        }
        if (!chatIdAdding.containsKey(chatId)) {
            chatIdAdding.put(chatId, true);
        } else {
            chatIdAdding.put(chatId, true);
        }
    }

    private void checkRemoveService(Long chatId) {
        if (chatIdAdding.get(chatId)) {
            chatIdAdding.put(chatId, false);
        }
        if (!chatIdRemoving.containsKey(chatId)) {
            chatIdRemoving.put(chatId, true);
        } else {
            chatIdRemoving.put(chatId, true);
        }
    }

    private void checkTwoDatesTop(Long chatId) {
        if (chatIdTwoDatesAll.get(chatId)) {
            chatIdTwoDatesAll.put(chatId, false);
        } else if (chatIdTwoDatesReport.get(chatId)) {
            chatIdTwoDatesReport.put(chatId, false);
        }
        if (!chatIdTwoDatesTop.containsKey(chatId)) {
            chatIdTwoDatesTop.put(chatId, true);
        } else {
            chatIdTwoDatesTop.put(chatId, true);
        }
        chatIdLimit.put(chatId, null);
    }

    private void checkTwoDatesAll(Long chatId) {
        if (chatIdTwoDatesTop.get(chatId)) {
            chatIdTwoDatesTop.put(chatId, false);
        } else if (chatIdTwoDatesReport.get(chatId)) {
            chatIdTwoDatesReport.put(chatId, false);
        }
        if (!chatIdTwoDatesAll.containsKey(chatId)) {
            chatIdTwoDatesAll.put(chatId, true);
        } else {
            chatIdTwoDatesAll.put(chatId, true);
        }
    }

    private void checkTwoDatesReport(Long chatId) {
        if (chatIdTwoDatesTop.get(chatId)) {
            chatIdTwoDatesTop.put(chatId, false);
        } else if (chatIdTwoDatesAll.get(chatId)) {
            chatIdTwoDatesAll.put(chatId, false);
        }
        if (!chatIdTwoDatesReport.containsKey(chatId)) {
            chatIdTwoDatesReport.put(chatId, true);
        } else {
            chatIdTwoDatesReport.put(chatId, true);
        }
    }

    private void stopDatesTopAllReport(Long chatId) {
        if (chatIdTwoDatesTop.containsKey(chatId)) {
            if (chatIdTwoDatesTop.get(chatId)) {
                chatIdLimit.put(chatId, null);
                chatIdTwoDatesTop.put(chatId, false);
                sendMessage(chatId, "Get Top N of error messages between two dates process was stopped.");
            } else if (chatIdTwoDatesAll.get(chatId)) {
                chatIdTwoDatesAll.put(chatId, false);
                sendMessage(chatId, "Get All of error messages between two dates process was stopped.");
            } else if (chatIdTwoDatesReport.get(chatId)) {
                chatIdTwoDatesReport.put(chatId, false);
                sendMessage(chatId, "Get Report of error messages between two dates process was stopped.");
            }

            if (chatIdDateFrom.get(chatId) != null) {
                chatIdDateFrom.put(chatId, null);
                sendMessage(chatId, "Date From values were cleared.");
            }
            if (chatIdDateTill.get(chatId) != null) {
                chatIdDateTill.put(chatId, null);
                sendMessage(chatId, "Date Till values were cleared.");
            }
        } else {
            chatIdTwoDatesTop.put(chatId, false);
            chatIdLimit.put(chatId, null);
            chatIdTwoDatesAll.put(chatId, false);
            chatIdTwoDatesReport.put(chatId, false);
            chatIdDateFrom.put(chatId, null);
            chatIdDateTill.put(chatId, null);
        }
    }

    private void addDate(Long chatId, String messageDate) {
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("d-M-yyyy");

        try {
            LocalDate date = LocalDate.parse(messageDate, dateTimeFormatter);

            if (chatIdDateFrom.get(chatId) == null) {
                chatIdDateFrom.put(chatId, date);

                sendMessage(chatId, "The first date(from) was added. Please, " +
                        "enter the second(till) date.");
            } else if (chatIdDateTill.get(chatId) == null) {
                chatIdDateTill.put(chatId, date);
                
                LocalDate from = chatIdDateFrom.get(chatId);
                LocalDate till = chatIdDateTill.get(chatId);

                if (from.isAfter(till)) {
                    LocalDate t = till;
                    till = from;
                    from = t;
                }

                Optional<Chat> chat = chatRepository.findByChatId(chatId);

                if (chat.isPresent()) {
                    if (chatIdTwoDatesTop.get(chatId)) {
                        Response response = errorMessageService.findAllByApiServiceUrlDateFromTillLimit(
                                chatId, from, till, chatIdLimit.get(chatId)
                        );

                        sendGeneralErrorMessages(chatId, response);
                    } else if(chatIdTwoDatesAll.get(chatId)) {
                        Response response = errorMessageService.findAllByApiServiceUrlDateFromTill(
                                chatId, from, till
                        );

                        sendGeneralErrorMessages(chatId, response);
                    } else if(chatIdTwoDatesReport.get(chatId)) {
                        Response response = errorMessageService.findAllByApiServiceUrlDateFromTillLimit(
                                chatId, from, till, chatIdLimit.get(chatId)
                        );

                        createReport(chatId, response);
                    }
                }
                stopDatesTopAllReport(chatId);
            }
        } catch (DateTimeParseException e) {
            sendMessage(chatId, "Message could not be parsed. Please, " +
                    "enter the date in the (dd-MM-yyyy) format. For example, " +
                    "29-07-2001 or 2-1-2020.");
        }
    }

    private void sendGeneralErrorMessages(Long chatId, Response response) {
        sendMessage(chatId, response.getMessage());
        for(int i = response.getErrorMessageResponses().size() - 1; i >= 0; i--) {
            Response errorMsgResponse = response.getErrorMessageResponses().get(i);

            String text = String.format("Test for: %s service was failed;\nURI: %s;\n" +
                            "Status code: %s;\nDate and Time: %s.",
                    errorMsgResponse.getServiceName(), errorMsgResponse.getUrlAddress(),
                    errorMsgResponse.getStatusCode() == Integer.MIN_VALUE ? "UNDEFINED"
                            : String.valueOf(errorMsgResponse.getStatusCode()),
                    errorMsgResponse.getLocalDateTime());

            sendMessage(chatId, text);
        }

        sendMessage(chatId, "This is all, you can call another method.");
    }

    private void addLimit(Long chatId, String message) {
        try {
            chatIdLimit.put(chatId, Integer.parseInt(message.trim()));
            sendMessage(chatId, "Limit number was added. Please, enter the dates.");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Please, enter the limit number of N newest error messages" +
                    " without any words, characters and floats.");
        }
    }

    private void createReport(Long chatId, Response response) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Report");

        int rowCount = 2;

        fillReportTemplate(sheet, response.getMessage());

        for(int i = response.getErrorMessageResponses().size() - 1; i >= 0; i--) {
            Response r = response.getErrorMessageResponses().get(i);

            Row row = sheet.createRow(rowCount);
            String[] fields = {r.getServiceName(), r.getUrlAddress(), r.getLocalDateTime()};
            for(int k = 0; k < 3; k++) {
                Cell cell = row.createCell(k);
                cell.setCellValue(fields[k]);
            }
            rowCount++;
        }

        try {
            String excelFileName = chatId + "_report_" + DATE_TIME_FILE_SAVE.format(LocalDateTime.now()) + ".xlsx";

            FileOutputStream outputStream = new FileOutputStream(excelFileName);

            workbook.write(outputStream);

            SendDocument sendDocument = new SendDocument();

            sendDocument.setChatId(String.valueOf(chatId));
            sendDocument.setDocument(new InputFile(new File(excelFileName)));

            execute(sendDocument);
        } catch (IOException e) {
            sendMessage(chatId, "Could not generate report due to server error.");
        } catch (TelegramApiException e) {
            LOGGER.info("Could not send message due to TelegramApiException");
        }
    }

    private void fillReportTemplate(XSSFSheet sheet, String msg) {
        int rowCount = 0;

        Row row = sheet.createRow(rowCount);
        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(msg);
        rowCount++;
        Row fieldsRow = sheet.createRow(rowCount);
        String[] fields = {"Service", "Address", "Date and Time"};
        for(int i = 0; i < fields.length; i++) {
            Cell fieldCell = fieldsRow.createCell(i, CellType.STRING);
            fieldCell.setCellValue(fields[i]);
        }
    }

    @Bean
    public void launchMaps() {
        List<Chat> chats = chatRepository.findAll();

        for(Chat chat: chats) {
            stopAddingRemoving(chat.getChatId());
            stopDatesTopAllReport(chat.getChatId());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
