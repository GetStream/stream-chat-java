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

  public static Map<String, Object> autocomplete(String fieldName, String inputString) {
    return Collections.singletonMap(
        fieldName, Collections.singletonMap("$autocomplete", inputString));
  }

  public static Map<String, Object> contains(String fieldName, String inputString) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$contains", inputString));
  }

  public static Map<String, Object> eq(String fieldName, Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$eq", fieldValue));
  }

  public static Map<String, Object> greaterThan(String fieldName, Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$gt", fieldValue));
  }

  public static Map<String, Object> greaterThanEquals(String fieldName, Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$gte", fieldValue));
  }

  public static Map<String, Object> lessThan(String fieldName, Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("lt", fieldValue));
  }

  public static Map<String, Object> lessThanEquals(String fieldName, Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$lte", fieldValue));
  }

  public static Map<String, Object> ne(String fieldName, Object fieldValue) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$ne", fieldValue));
  }

  public static Map<String, Object> in(String fieldName, Collection<Object> fieldValues) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$in", fieldValues));
  }

  public static Map<String, Object> in(String fieldName, Object... fieldValues) {
    return in(fieldName, Arrays.asList(fieldValues));
  }

  public static Map<String, Object> nin(String fieldName, Collection<Object> fieldValues) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$nin", fieldValues));
  }

  public static Map<String, Object> nin(String fieldName, Object... fieldValues) {
    return in(fieldName, Arrays.asList(fieldValues));
  }

  public static Map<String, Object> exists(String fieldName) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$exists", true));
  }

  public static Map<String, Object> notExists(String fieldName) {
    return Collections.singletonMap(fieldName, Collections.singletonMap("$exists", false));
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
