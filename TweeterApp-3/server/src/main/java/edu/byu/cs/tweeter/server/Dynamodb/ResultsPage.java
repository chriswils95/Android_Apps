package edu.byu.cs.tweeter.server.Dynamodb;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsPage {


        /**
         * The data values returned in this page of results
         */
        private List<String> values;
        private Map<String, Object> mapValues;

        private List<Map<String, AttributeValue>> items;

    public List<Map<String, AttributeValue>> getItem() {
        return  items;
    }

    public void addItems(Map<String, AttributeValue> item){
        this.items.add(item);
    }

    public void setItem(List<Map<String, AttributeValue>> items) {
        this.items = items;
    }

    public Map<String, Object> getMapValues() {
            return mapValues;
        }

        /**
         * The last value returned in this page of results
         * This value is typically included in the query for the next page of results
         */
        private String lastKey;

        public ResultsPage() {
            values = new ArrayList<>();
            mapValues = new HashMap<String, Object>();
            items = new ArrayList<Map<String, AttributeValue>>();
            lastKey = null;
        }

        // Values property

        public void addValue(String v) {
            values.add(v);
        }

        public void addMapValue(String k, Object V){
            mapValues.put(k, V);
        }

        public boolean hasValues() {
            return (values != null && values.size() > 0);
        }

        public List<String> getValues() {
            return values;
        }

        // LastKey property

        public void setLastKey(String value) {
            lastKey = value;
        }

        public String getLastKey() {
            return lastKey;
        }

        public boolean hasLastKey() {
            return (lastKey != null && lastKey.length() > 0);
        }
}