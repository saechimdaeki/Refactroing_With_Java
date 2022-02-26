package me.saechimdaeki.refactoring._18_middle_man._40_replace_subclass_with_delegate;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    @Test
    void basePrice() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);

        Booking booking = Booking.createBook(lionKing, weekday);
        assertEquals(120, booking.basePrice());

        Booking premium = Booking.createPremiumBook(lionKing, weekday, new PremiumExtra(List.of(), 50));
        assertEquals(170, premium.basePrice());
    }

    @Test
    void basePrice_on_peakDay() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        Booking booking = Booking.createBook(lionKing, weekend);
        assertEquals(138, booking.basePrice());

        Booking premium = Booking.createPremiumBook(lionKing, weekend, new PremiumExtra(List.of(), 50));
        assertEquals(188, premium.basePrice());
    }

    @Test
    void talkback() {
        Show lionKing = new Show(List.of(), 120);
        Show aladin = new Show(List.of("talkback"), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        assertFalse(Booking.createBook(lionKing, weekday).hasTalkback());
        assertTrue(Booking.createBook(aladin, weekday).hasTalkback());
        assertFalse(Booking.createBook(aladin, weekend).hasTalkback());

        PremiumExtra premiumExtra = new PremiumExtra(List.of(), 50);
        assertTrue(Booking.createPremiumBook(aladin, weekend, premiumExtra).hasTalkback());
        assertFalse(Booking.createPremiumBook(lionKing, weekend, premiumExtra).hasTalkback());
    }

    @Test
    void hasDinner() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);
        PremiumExtra premiumExtra = new PremiumExtra(List.of("dinner"), 50);

        assertTrue(Booking.createPremiumBook(lionKing, weekday, premiumExtra).hasDinner());
        assertFalse(Booking.createPremiumBook(lionKing, weekend, premiumExtra).hasDinner());
        assertFalse(Booking.createBook(lionKing, weekend).hasDinner());

    }
}