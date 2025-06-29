package org.solstice.euclidsElements.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DateUtils {

	public static boolean isToday(LocalDate date) {
		return today().equals(date);
	}

	public static LocalDate today() {
		return ZonedDateTime.now().withYear(0).toLocalDate();
	}

}
