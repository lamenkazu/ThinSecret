package com.daedrii.bodyapp.controller.fatsecret;

import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.Serving;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public static List<FoodDetails> parseFoodSearchResults(String xmlData) {
        List<FoodDetails> searchResults = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            Element rootElement = document.getDocumentElement();

            NodeList foodNodes = rootElement.getElementsByTagName("food");
            for (int i = 0; i < foodNodes.getLength(); i++) {
                Element foodElement = (Element) foodNodes.item(i);

                String foodId = getElementValue(foodElement, "food_id");
                String foodName = getElementValue(foodElement, "food_name");
                String brandName = getElementValue(foodElement, "brand_name");
                String foodType = getElementValue(foodElement, "food_type");
                String foodUrl = getElementValue(foodElement, "food_url");

                // Parse servings
                List<Serving> servings = parseServings(foodElement);

                FoodDetails foodDetails = new FoodDetails(foodId, foodName, brandName, foodType, foodUrl, servings);
                searchResults.add(foodDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    public static FoodDetails parseFoodDetails(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            Element rootElement = document.getDocumentElement();

            String foodId = getElementValue(rootElement, "food_id");
            String foodName = getElementValue(rootElement, "food_name");
            String brandName = getElementValue(rootElement, "brand_name");
            String foodType = getElementValue(rootElement, "food_type");
            String foodUrl = getElementValue(rootElement, "food_url");

            // Parse servings
            List<Serving> servings = parseServings(rootElement);

            return new FoodDetails(foodId, foodName, brandName, foodType, foodUrl, servings);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getElementValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private static List<Serving> parseServings(Element foodElement) {
        List<Serving> servings = new ArrayList<>();

        NodeList servingNodes = foodElement.getElementsByTagName("serving");
        for (int i = 0; i < servingNodes.getLength(); i++) {
            Element servingElement = (Element) servingNodes.item(i);

            String servingId = getElementValue(servingElement, "serving_id");
            String servingDescription = getElementValue(servingElement, "serving_description");
            String servingUrl = getElementValue(servingElement, "serving_url");

            // Verificar e converter os valores numéricos
            double metricServingAmount = parseDoubleValue(servingElement, "metric_serving_amount");
            String metricServingUnit = getElementValue(servingElement, "metric_serving_unit");
            double numberOfUnits = parseDoubleValue(servingElement, "number_of_units");
            String measurementDescription = getElementValue(servingElement, "measurement_description");
            int calories = parseIntValue(servingElement, "calories");
            double carbohydrate = parseDoubleValue(servingElement, "carbohydrate");
            double protein = parseDoubleValue(servingElement, "protein");
            double fat = parseDoubleValue(servingElement, "fat");
            double saturatedFat = parseDoubleValue(servingElement, "saturated_fat");
            double monounsaturatedFat = parseDoubleValue(servingElement, "monounsaturated_fat");
            double transFat = parseDoubleValue(servingElement, "trans_fat");
            int cholesterol = parseIntValue(servingElement, "cholesterol");
            int sodium = parseIntValue(servingElement, "sodium");
            int potassium = parseIntValue(servingElement, "potassium");
            double fiber = parseDoubleValue(servingElement, "fiber");
            double sugar = parseDoubleValue(servingElement, "sugar");
            int calcium = parseIntValue(servingElement, "calcium");
            int iron = parseIntValue(servingElement, "iron");

            Serving serving = new Serving(
                    servingId, servingDescription, servingUrl, metricServingAmount, metricServingUnit,
                    numberOfUnits, measurementDescription, calories, carbohydrate, protein, fat,
                    saturatedFat, monounsaturatedFat, transFat, cholesterol, sodium, potassium,
                    fiber, sugar, calcium, iron
            );

            servings.add(serving);
        }

        return servings;
    }

    private static double parseDoubleValue(Element element, String tagName) {
        String value = getElementValue(element, tagName);
        if (!value.isEmpty()) {
            return Double.parseDouble(value);
        }
        return 0.0;
    }

    private static int parseIntValue(Element element, String tagName) {
        String value = getElementValue(element, tagName);
        if (!value.isEmpty()) {
            return Integer.parseInt(value);
        }
        return 0;
    }

}
