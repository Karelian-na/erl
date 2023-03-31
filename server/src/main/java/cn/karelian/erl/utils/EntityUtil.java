package cn.karelian.erl.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import cn.karelian.erl.StatusCode;
import cn.karelian.erl.errors.FieldError;

public final class EntityUtil {

	public static Map<String, Object> ToMap(Serializable entity) {
		if (entity == null)
			return null;

		Map<String, Object> map = new HashMap<>();

		for (Field field : entity.getClass().getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			Boolean access = field.canAccess(entity);
			if (!access)
				field.setAccessible(true);

			Object value;
			try {
				value = field.get(entity);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				continue;
			}
			if (value != null) {
				map.put(field.getName(), value);
			}

			if (!access) {
				field.setAccessible(access);
			}
		}

		return map;
	}

	public static int CheckStringField(String value, int len, boolean noEmpty) {
		if (value == null) {
			return noEmpty ? FieldError.EMPTY : StatusCode.ERROR_SUCCESS;
		}

		if (value.length() != len) {
			return FieldError.FORMAT;
		}

		return StatusCode.ERROR_SUCCESS;
	}

	public static int CheckStringField(String value, int minLen, int maxLen, boolean noEmpty) {
		if (value == null) {
			return noEmpty ? FieldError.EMPTY : StatusCode.ERROR_SUCCESS;
		}

		if (value.length() > maxLen) {
			return FieldError.TOO_LONG;
		}

		if (value.length() < minLen) {
			return FieldError.TOO_SHORT;
		}

		return StatusCode.ERROR_SUCCESS;
	}

	public static <T extends Comparable<T>> int CheckNumberField(T value, T min, T max, boolean noEmpty) {
		int code = StatusCode.ERROR_SUCCESS;
		if (value == null) {
			if (noEmpty) {
				code = FieldError.EMPTY;
			}
		} else if (max != null && value.compareTo(max) > 0) {
			code = FieldError.TOO_LARGE;
		} else if (min != null && value.compareTo(min) < 0) {
			code = FieldError.TOO_SMALL;
		}

		return code;
	}
}
