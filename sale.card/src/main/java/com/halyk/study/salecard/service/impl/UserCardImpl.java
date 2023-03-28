package com.halyk.study.salecard.service.impl;

import com.halyk.study.salecard.dto.Objects;
import com.halyk.study.salecard.dto.Request;
import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.dto.enums.Status;
import com.halyk.study.salecard.entity.Product;
import com.halyk.study.salecard.entity.User;
import com.halyk.study.salecard.entity.UserCard;
import com.halyk.study.salecard.mappers.ResponseMapper;
import com.halyk.study.salecard.mappers.UserCardMapper;
import com.halyk.study.salecard.repository.ProductRepository;
import com.halyk.study.salecard.repository.UserCardRepository;
import com.halyk.study.salecard.repository.UserRepository;
import com.halyk.study.salecard.service.UserCardService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCardImpl implements UserCardService {

    private final UserCardRepository userCardRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final Font BARCODE_TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);


    @Override
    public Response getUserCards(Request request) {
        List<Object> userCards = userCardRepository
                .findAllByUserIdOrderByProductNumberAsc(request.getUserId())
                .stream()
                .map(UserCardMapper::mapToUserCardResponse)
                .collect(Collectors.toList());

        Objects objects = new Objects(userCards);

        return ResponseMapper.response("user_cards_returned", Status.SUCCESS, objects);
    }

    @Override
    public Response getUserCardsSortedByName(Request request) {
        List<Object> userCards = userCardRepository
                .findAllByUserIdOrderByProductName(request.getUserId())
                .stream()
                .map(UserCardMapper::mapToUserCardResponse)
                .collect(Collectors.toList());

        Objects objects = new Objects(userCards);

        return ResponseMapper.response("user_cards_returned", Status.SUCCESS, objects);
    }

    @Override
    public Response getUserCardsSortedByDate(Request request) {
        List<Object> userCards = userCardRepository
                .findAllByUserIdOrderByCreatedAtDesc(request.getUserId())
                .stream()
                .map(UserCardMapper::mapToUserCardResponse)
                .collect(Collectors.toList());

        Objects objects = new Objects(userCards);

        return ResponseMapper.response("user_cards_returned", Status.SUCCESS, objects);
    }

    @SneakyThrows
    @Override
    public BufferedImage getBarcode(Request request) {
        Barcode barcode;

        Optional<UserCard> optionalUserCard = userCardRepository.findById(request.getUserCardId());

        if (optionalUserCard.isPresent()) {
            barcode = BarcodeFactory.createEAN13(optionalUserCard.get().getBarcode());
        } else {
            barcode = BarcodeFactory.createEAN13("000000000000");
        }

        barcode.setBarHeight(600);
        barcode.setBarWidth(400);
        barcode.setFont(BARCODE_TEXT_FONT);

        return BarcodeImageHandler.getImage(barcode);
    }

    @Override
    public Response createUserCard(Request request) {
        Optional<Product> card = productRepository.findById(request.getProductId());
        Optional<User> user = userRepository.findById(request.getUserId());

        if (!card.isPresent() || !user.isPresent()) {
            return ResponseMapper.response("card_user_not_found", Status.ERROR, false);
        }

        Optional<UserCard> optionalUserCard = userCardRepository
                .findByUserIdAndProductId(user.get().getId(), card.get().getId());

        if (optionalUserCard.isPresent()) {
            return ResponseMapper.response("user_already_has_card", Status.ERROR, false);
        }

        if (request.getBarcode() == null) {
            return ResponseMapper.response("no_barcode", Status.ERROR, false);
        } else if (request.getBarcode().length() != 12) {
            return ResponseMapper.response("barcode_length", Status.ERROR, false);
        }

        UserCard userCard = UserCard.builder()
                .user(user.get())
                .product(card.get())
                .barcode(request.getBarcode())
                .build();

        userCardRepository.save(userCard);

        return ResponseMapper.response("user_card_added", Status.SUCCESS, true);
    }

    @Override
    public Response updateUserCard(Request request) {
        Optional<UserCard> optionalUserCard = userCardRepository.findById(request.getUserCardId());

        if (optionalUserCard.isPresent()) {
            if (request.getBarcode() == null) {
                return ResponseMapper.response("no_barcode", Status.ERROR, false);
            }

            optionalUserCard.get().setBarcode(request.getBarcode());

            userCardRepository.save(optionalUserCard.get());

            return ResponseMapper.response("user_card_updated", Status.SUCCESS, true);
        }

        return ResponseMapper.response("user_card_not_found", Status.ERROR, false);
    }

    @Override
    public Response deleteUserCard(Request request) {
        Optional<UserCard> optionalUserCard = userCardRepository.findById(request.getUserCardId());

        if (optionalUserCard.isPresent()) {
            userCardRepository.delete(optionalUserCard.get());

            return ResponseMapper.response("user_card_deleted", Status.SUCCESS, true);
        }

        return ResponseMapper.response("user_card_not_found", Status.ERROR, false);
    }

    @Bean
    public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
