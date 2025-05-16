package com.dialog.server.controller;

import com.dialog.server.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations/{reservationId}/scraps")
@RequiredArgsConstructor
public class ReservationScrapController {

    private static final Long FAKE_USER_ID = 1L;

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<Void> scrap(@PathVariable Long reservationId) {
        scrapService.create(FAKE_USER_ID, reservationId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteScrap(@PathVariable Long reservationId) {
        scrapService.delete(FAKE_USER_ID, reservationId);
        return ResponseEntity.noContent()
                .build();
    }
}
