package io.stream.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FilterCondition {
  public static Map<String, Object> and(Map<String, Object> filter1, Map<String, Object> filter2) {
    return Collections.singletonMap("$and", Arrays.asList(filter1, filter2));
  }

  public static Map<String, Object> or(Map<String, Object> filter1, Map<String, Object> filter2) {
    return Collections.singletonMap("$or", Arrays.asList(filter1, filter2));
  }

  public static Map<String, Object> nor(Map<String, Object> filter1, Map<String, Object> filter2) {
    return Collections.singletonMap("$nor", Arrays.asList(filter1, filter2));
  }

  public static Map<String, Object> autocomplete(String inputString) {
    return Collections.singletonMap("$autocomplete", inputString);
  }

  public static Map<String, Object> contains(String inputString) {
    return Collections.singletonMap("$contains", inputString);
  }

  public static Map<String, Object> eq(Object fieldValue) {
    return Collections.singletonMap("$eq", fieldValue);
  }

  public static Map<String, Object> greaterThan(Object fieldValue) {
    return Collections.singletonMap("$gt", fieldValue);
  }

  public static Map<String, Object> greaterThanEquals(Object fieldValue) {
    return Collections.singletonMap("$gte", fieldValue);
  }

  public static Map<String, Object> lessThan(Object fieldValue) {
    return Collections.singletonMap("lt", fieldValue);
  }

  public static Map<String, Object> lessThanEquals(Object fieldValue) {
    return Collections.singletonMap("$lte", fieldValue);
  }

  public static Map<String, Object> ne(Object fieldValue) {
    return Collections.singletonMap("$ne", fieldValue);
  }

  public static Map<String, Object> in(Collection<Object> fieldValues) {
    return Collections.singletonMap("$in", fieldValues);
  }

  public static Map<String, Object> in(Object... fieldValues) {
    return in(Arrays.asList(fieldValues));
  }

  public static Map<String, Object> nin(Collection<Object> fieldValues) {
    return Collections.singletonMap("$nin", fieldValues);
  }

  public static Map<String, Object> nin(Object... fieldValues) {
    return nin(Arrays.asList(fieldValues));
  }

  public static Map<String, Object> exists() {
    return Collections.singletonMap("$exists", true);
  }

  public static Map<String, Object> notExists(String fieldName) {
    return Collections.singletonMap("$exists", false);
  }

  public static Map<String, Object> distinct(Collection<String> memberIds) {
    Map<String, Object> result = new HashMap<>();
    result.put("$distinct", true);
    result.put("$members", memberIds);
    return result;
  }

  public static Map<String, Object> distinct(String... memberIds) {
    return distinct(memberIds);
  }
}
