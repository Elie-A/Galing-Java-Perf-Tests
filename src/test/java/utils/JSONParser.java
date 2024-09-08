package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.FeatureModel;
import models.RequestModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static List<RequestModel> parseJsonToRequests(String jsonFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FeatureModel[] features = mapper.readValue(new File(jsonFilePath), FeatureModel[].class);
        List<RequestModel> requestModels = new ArrayList<>();
        for (FeatureModel feature : features) {
            requestModels.addAll(feature.getRequests());
        }
        return requestModels;
    }
}