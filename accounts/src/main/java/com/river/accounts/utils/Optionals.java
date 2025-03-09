package com.river.accounts.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Optionals {
    public static <T> void ifPresent(Optional<T> optional, Consumer<T> consumer) {
        if (optional != null && optional.isPresent()) {
            consumer.accept(optional.get());
        }
    }

    public static <T> void ifUndefined(Optional<T> optional, Runnable runnable) {
        if (optional == null) {
            runnable.run();
        }
    }

    public static <T> void ifDefined(Optional<T> optional, Consumer<T> consumer) {
        if (optional != null) {
            consumer.accept(optional.isPresent() ? optional.get() : null);
        }
    }

    public static <T> void isUndefinedOrEmpty(Optional<T> optional, Runnable runnable) {
        if (optional == null || !optional.isPresent()) {
            runnable.run();
        }
    }

    public static <T> void isDefinedAndEmpty(Optional<T> optional, Runnable runnable) {
        if (optional != null && !optional.isPresent()) {
            runnable.run();
        }
    }

    public static <T> boolean isDefined(Optional<T> optional) {
        return optional != null;
    }

    public static <T> boolean isPresent(Optional<T> optional) {
        return isDefined(optional) && optional.isPresent();
    }

    public static <T> boolean isNotPresent(Optional<T> optional) {
        return !isPresent(optional);
    }

    public static <T> boolean isUndefined(Optional<T> optional) {
        return !isDefined(optional);
    }

    public static <T> boolean matches(Optional<T> optional, Predicate predicate) {
        return isDefined(optional) && optional.filter(predicate).isPresent();
    }

    public static <T> T get(Optional<T> optional) {
        return get(optional, null, null);
    }

    public static <T> T get(Optional<T> optional, T unDefinedDefault, T emptyDefault) {
        return isUndefined(optional) ? unDefinedDefault : optional.orElse(emptyDefault);
    }

    public static <T> T get(Optional<T> optional, T defaultValue) {
        return get(optional, defaultValue, defaultValue);
    }

    public static <T> T orElseGet(Optional<T> optional, Supplier<T> supplier) {
        return isPresent(optional) ? get(optional) : supplier.get();
    }

    public static <T> boolean isEqual(Optional<T> optional, T value) {
        return isPresent(optional) && optional.get().equals(value);
    }

    public static <T extends BigDecimal> boolean isEqual(Optional<T> optional, T value) {
        return isPresent(optional) && optional.get().compareTo(value) == 0;
    }

    public static <T> boolean isGreaterThan(Optional<? extends Comparable<T>> optional, T max) {
        return isPresent(optional) && ((Comparable) optional.get()).compareTo(max) > 0;
    }

    public static <T> boolean isGreaterThanOrEqual(Optional<? extends Comparable<T>> optional, T max) {
        return isPresent(optional) && ((Comparable) optional.get()).compareTo(max) >= 0;
    }

    public static <T> boolean isLessThan(Optional<? extends Comparable<T>> optional, T max) {
        return isPresent(optional) && ((Comparable) optional.get()).compareTo(max) < 0;
    }

    public static <T> boolean isLessThanOrEqual(Optional<? extends Comparable<T>> optional, T max) {
        return isPresent(optional) && ((Comparable) optional.get()).compareTo(max) >= 0;
    }

    public static <T> boolean isBetween(Optional<? extends Comparable<T>> optional, T min, T max) {
        return isPresent(optional)
                && ((Comparable) optional.get()).compareTo(min) >= 0
                && ((Comparable) optional.get()).compareTo(max) <= 0;
    }

    public static boolean isFuture(Optional<LocalDate> date) {
        return isPresent(date) && ((LocalDate) date.get()).isAfter(LocalDate.now());
    }

    public static boolean isPast(Optional<LocalDate> date) {
        return isPresent(date) && ((LocalDate) date.get()).isBefore(LocalDate.now());
    }

    public static boolean isToDayOrPast(Optional<LocalDate> date) {
        return isPresent(date) && ((LocalDate) date.get()).compareTo(LocalDate.now()) <= 0;
    }

    public static boolean isToDayOrFuture(Optional<LocalDate> date) {
        return isPresent(date) && ((LocalDate) date.get()).compareTo(LocalDate.now()) >= 0;
    }

    public static boolean isToDay(Optional<LocalDate> date) {
        return isPresent(date) && ((LocalDate) date.get()).isEqual(LocalDate.now());
    }

    public static boolean isGreaterThanZero(Optional<? extends Number> number) {
        return isPresent(number) && ((BigDecimal) number.get()).compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isGreaterThanZeroOrEqualToZero(Optional<? extends Number> number) {
        return isPresent(number) && ((BigDecimal) number.get()).compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean isEqualToZero(Optional<? extends Number> number) {
        return isPresent(number) && ((BigDecimal) number.get()).compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isLessThanZero(Optional<? extends Number> number) {
        return isPresent(number) && ((BigDecimal) number.get()).compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isLessThanOrEqualToZero(Optional<? extends Number> number) {
        return isPresent(number) && ((BigDecimal) number.get()).compareTo(BigDecimal.ZERO) <= 0;
    }
}
