package com.com.teacherkosal.jsonlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonConverter<T> {

    /**
     * Usage:
     * List<User> userList = new JsonConverter().toList(output, User.class);
     *
     * Model Declaration:
     * In User.java, use @SerializedName() if a field name differs from a JSON attribute.
     * Make all attributes public. No need to create setters/getters.
     * Bellow is a sample code:
     *
     * import com.google.gson.annotations.SerializedName; // include gson-2.2.4.jar in libs folder
     * public class User {
     *    @SerializedName("user_id")
     *    public int user_id;
     *    @SerializedName("username")
     *    public String username;
     *    @SerializedName("password")
     *    public String password;
     * }
     * End of Usage
     **/
    public ArrayList<T> toArrayList(String jsonString, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("dd/MM/yy HH:mm:ss");
        Gson gson = builder.create();
        Type type = new ListParameterizedType(clazz);
        ArrayList<T> list = gson.fromJson(jsonString, type);

        return list;
    }

    public List<T> toList(String jsonString, Class<T> clazz) {

        List<T> list = toArrayList(jsonString, clazz);

        return list;
    }

    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

    }
}
