package com.pratap.accounts.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface AccountsUtil {

    public static <T> List<T> jsonToJava(String fileName, Class<T> classType){

        List<T> t = new ArrayList<>();
        File file = new File("src/main/resources/"+fileName);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            t = mapper.readValue(file, new TypeReference<List<T>>(){});
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return t;
    }
}
