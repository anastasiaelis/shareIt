package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated

public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut create(@RequestHeader(ItemController.USER_HEADER) Long userId,
                                @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание нового бронирования вещи от пользователя c id: {} ", userId);
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateStatus(@RequestHeader(ItemController.USER_HEADER) Long userId,
                                      @PathVariable("bookingId")
                                      Long bookingId,
                                      @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH запрос на обновление статуса бронирования вещи от владельца с id: {}", userId);
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut findBookingById(@RequestHeader(ItemController.USER_HEADER) Long userId,
                                         @PathVariable("bookingId")
                                         Long bookingId) {
        log.info("GET запрос на получение данных о  бронировании от пользователя с id: {}", userId);
        return bookingService.findBookingByUserId(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> findAll(@RequestHeader(ItemController.USER_HEADER) Long userId,
                                       @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                       @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                       @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего пользователя с id: {} и статусом {}", userId, bookingState);
        return bookingService.findAll(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader(ItemController.USER_HEADER) Long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                           @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего владельца с id: {} и статусом {}", ownerId, bookingState);
        return bookingService.findAllOwner(ownerId, bookingState, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut setApproved(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return bookingService.setApproved(userId, bookingId, approved);
    }
}