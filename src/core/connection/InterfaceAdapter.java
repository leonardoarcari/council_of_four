package core.connection;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Interface adapter for serializing/de-serializing abstract classes or interfaces. <p>
 * Code courtesy of Maciek Makowski: <a href="http://stackoverflow.com/a/9550086">source</a>.
 */
public final class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    /**
     * Called when you call gson.fromJson(object,yourClass.class)
     */
    @Override
    public T deserialize(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject wrapper = (JsonObject) elem;
        final JsonElement typeName = get(wrapper, "type");
        final JsonElement data = get(wrapper, "data");
        final Type actualType = typeForName(typeName);
        return context.deserialize(data, actualType);
    }

    /**
     * Called when you call gson.toJson(jsonObject)
     */
    @Override
    public JsonElement serialize(T object, Type type, JsonSerializationContext context) {
        final JsonObject wrapper = new JsonObject();
        wrapper.addProperty("type", object.getClass().getName());
        wrapper.add("data", context.serialize(object));
        return wrapper;
    }

    /**
     * @param typeElem
     * @return the Type of the object
     */
    private Type typeForName(final JsonElement typeElem) {
        try {
            return Class.forName(typeElem.getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    /**
     * @return a member of json object
     */
    private JsonElement get(final JsonObject wrapper, String memberName) {
        final JsonElement elem = wrapper.get(memberName);
        if (elem == null) throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
        return elem;
    }
}
