package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.FeatureModel;
import models.RequestModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<RequestModel> parseJsonToRequests(String jsonFilePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FeatureModel[] features = mapper.readValue(getFileFromResource("", jsonFilePath), FeatureModel[].class);
        List<RequestModel> requestModels = new ArrayList<>();
        for (FeatureModel feature : features) {
            requestModels.addAll(feature.getRequests());
        }
        return requestModels;
    }

    private static File getFileFromResource(String absolutePath, String fileName) throws Exception {

        ClassLoader classLoader = JSONParser.class.getClassLoader();
        URL resource = classLoader.getResource(absolutePath + fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}