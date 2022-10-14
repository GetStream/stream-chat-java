package io.getstream.chat.java.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class FilterCondition {
  @SafeVarargs
  public static Map<String, Object> and(@NotNull Map<String, Object>... filters) {
    return Collections.singletonMap("$and", Arrays.asList(filters));
  }

  @SafeVarargs
  public static Map<String, Object> or(@NotNull Map<String, Object>... filters) {
    return Collections.singletonMap("$or", Arrays.asList(filters));
  }

  @SafeVarargs
  public static Map<String, Object> nor(@NotNull Map<String, Object>... filters) {
    return Collections.singletonMap("$nor", Arrays.asList(filters));
  }

  public static Map<String, Object> autocomplete(
      @NotNull String fieldName, @NotNull String inputString) {
    return Collections.singletonMap(
        fieldName, Collections.singletonMap("$autocomplete", inputString));
  }

  public static Map<String, Object> contains(
      @NotNull String fieldName, @NotNull String inputString) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$contains", inputString));
  }

  public static Map<String, Object> eq(@NotNull String fieldName, @NotNull Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$eq", fieldValue));
  }

  public static Map<String, Object> greaterThan(
      @NotNull String fieldName, @NotNull Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$gt", fieldValue));
  }

  public static Map<String, Object> greaterThanEquals(
      @NotNull String fieldName, @NotNull Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$gte", fieldValue));
  }

  public static Map<String, Object> lessThan(
      @NotNull String fieldName, @NotNull Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$lt", fieldValue));
  }

  public static Map<String, Object> lessThanEquals(
      @NotNull String fieldName, @NotNull Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$lte", fieldValue));
  }

  public static Map<String, Object> ne(@NotNull String fieldName, @NotNull Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$ne", fieldValue));
  }

  public static Map<String, Object> in(
      @NotNull String fieldName, @NotNull Collection<Object> fieldValues) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$in", fieldValues));
  }

  public static Map<String, Object> in(@NotNull String fieldName, @NotNull Object... fieldValues) {
    return in(fieldName, Arrays.asList(fieldValues));
  }

  public static Map<String, Object> nin(
      @NotNull String fieldName, @NotNull Collection<Object> fieldValues) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$nin", fieldValues));
  }

  public static Map<String, Object> nin(@NotNull String fieldName, @NotNull Object... fieldValues) {
    return nin(fieldName, Arrays.asList(fieldValues));
  }

  public static Map<String, Object> exists() {
    return Collections.singletonMap("$exists", true);
  }

  public static Map<String, Object> notExists(@NotNull String fieldName) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$exists", false));
  }

  public static Map<String, Object> distinct(
      @NotNull String fieldName, @NotNull Collection<String> memberIds) {
    Map<String, Object> map = new HashMap<>();
    map.put("$distinct", true);
    map.put("$members", memberIds);
    return Collections.singletonMap(fieldName, map);
  }

  public static Map<String, Object> distinct(
      @NotNull String fieldName, @NotNull String... memberIds) {
    return distinct(fieldName, memberIds);
  }
}
