package selva.oss.lang;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GsonUtils {

    public static class FieldDoesNotExistException extends RuntimeException {

        public FieldDoesNotExistException(String key) {
            super(key);
        }

    }

    public static boolean doesFieldExist(String key, JsonObject jsonObject) {
        return jsonObject.get(key) != null;
    }

    public static class JsonArrayExpectedException extends RuntimeException {
    }

    public static class JsonObjectExpectedException extends RuntimeException {
    }

    public static Stream<JsonObject> fetchJsonObjects(String key, JsonObject jsonObject) {
        if (!doesFieldExist(key, jsonObject) || !jsonObject.get(key).isJsonArray()) {
            throw new JsonArrayExpectedException();
        }

        Iterable<JsonElement> iterable = () -> jsonObject.get(key).getAsJsonArray().iterator();
        return StreamSupport.stream(iterable.spliterator(), false).map(jsonElement -> {
            if (!jsonElement.isJsonObject()) {
                throw new JsonObjectExpectedException();
            }
            return jsonElement.getAsJsonObject();
        });
    }

    public static JsonObject fetchJsonObject(String key, JsonObject jsonObject) {
        if (!doesFieldExist(key, jsonObject) || !jsonObject.get(key).isJsonObject()) {
            throw new JsonObjectExpectedException();
        }

        return jsonObject.get(key).getAsJsonObject();
    }

}
