package edu.byu.cs.tweeter.server.Dynamodb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Database {





        private BasicAWSCredentials awsCreds;
        String tableName;
        String primaryKeyAttr;
        String sortKeyAttr;


       AmazonDynamoDB client;
       private DynamoDB dynamoDB;


        public Database(String tableName, String primaryKeyAttr, String sortKeyAttr){
            awsCreds = new BasicAWSCredentials("AKIAV5Y67N54EQPANR6B",
                    "1YsSVw/b9CCR4jXB4wYauw6v3YASlLb72U5397GP");
            this.client = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(Regions.US_WEST_2)
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();

            this.tableName = tableName;
            this.primaryKeyAttr = primaryKeyAttr;
            this.sortKeyAttr = sortKeyAttr;
            this.dynamoDB = new DynamoDB(client);

        }




        private static boolean isNonEmptyString(String value) {
            return (value != null && value.length() > 0);
        }

        /**
         * Create the "visits" table and the "visits-index" global index
         *
         * @throws DataAccessException
         */
        public void createTable() throws Exception {

            String tableName = "User";
            // Attribute definitions
            ArrayList<AttributeDefinition> tableAttributeDefinitions = new ArrayList<>();

            tableAttributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("alias")
                    .withAttributeType("S"));
            tableAttributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName("image_url")
                    .withAttributeType("S"));

            // Table key schema
            ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<>();
            tableKeySchema.add(new KeySchemaElement()
                    .withAttributeName("alias")
                    .withKeyType(KeyType.HASH));  //Partition key
            tableKeySchema.add(new KeySchemaElement()
                    .withAttributeName("image_url")
                    .withKeyType(KeyType.RANGE));  //Sort key

            // Index
            GlobalSecondaryIndex index = new GlobalSecondaryIndex()
                    .withIndexName(this.tableName + "-index")
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits((long) 1)
                            .withWriteCapacityUnits((long) 1))
                    .withProjection(new Projection().withProjectionType(ProjectionType.ALL));

            ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<>();

            indexKeySchema.add(new KeySchemaElement()
                    .withAttributeName("alias")
                    .withKeyType(KeyType.HASH));  //Partition key
            indexKeySchema.add(new KeySchemaElement()
                    .withAttributeName("image_url")
                    .withKeyType(KeyType.RANGE));  //Sort key

            index.setKeySchema(indexKeySchema);

            CreateTableRequest createTableRequest = new CreateTableRequest()
                    .withTableName(this.tableName)
                    .withProvisionedThroughput(new ProvisionedThroughput()
                            .withReadCapacityUnits((long) 1)
                            .withWriteCapacityUnits((long) 1))
                    .withAttributeDefinitions(tableAttributeDefinitions)
                    .withKeySchema(tableKeySchema)
                    .withGlobalSecondaryIndexes(index);

            Table table = dynamoDB.createTable(createTableRequest);
            table.waitForActive();
        }


        public void createStatusTable() throws Exception {
            DynamoDB dynamoDB = new DynamoDB(client);

            String tableName = "Status";
            try {
                System.out.println("Attempting to create table; please wait...");
                Table table = dynamoDB.createTable(tableName,
                        Arrays.asList(new KeySchemaElement("user_alias", KeyType.HASH), // Partition
                                // key
                                new KeySchemaElement("date", KeyType.RANGE)), // Sort key
                        Arrays.asList(new AttributeDefinition("user_alias", ScalarAttributeType.S),
                                new AttributeDefinition("date", ScalarAttributeType.S)),
                        new ProvisionedThroughput(1L, 1L));
                table.waitForActive();
                System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

            }
            catch (Exception e) {
                System.err.println("Unable to create table: ");
                System.err.println(e.getMessage());
            }
        }


        public void createLoginTable() throws Exception {
            DynamoDB dynamoDB = new DynamoDB(client);

            String tableName = "Login";
            try {
                System.out.println("Attempting to create table; please wait...");
                Table table = dynamoDB.createTable(tableName,
                        Arrays.asList(new KeySchemaElement("username", KeyType.HASH), // Partition
                                // key
                                new KeySchemaElement("password", KeyType.RANGE)), // Sort key
                        Arrays.asList(new AttributeDefinition("username", ScalarAttributeType.S),
                                new AttributeDefinition("password", ScalarAttributeType.S)),
                        new ProvisionedThroughput(1L, 1L));
                table.waitForActive();
                System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

            }
            catch (Exception e) {
                System.err.println("Unable to create table: ");
                System.err.println(e.getMessage());
            }
        }

        public void createAuthTokenTable() throws Exception {
            DynamoDB dynamoDB = new DynamoDB(client);

            String tableName = "AuthToken";
            try {
                System.out.println("Attempting to create table; please wait...");
                Table table = dynamoDB.createTable(tableName,
                        Arrays.asList(new KeySchemaElement("user_handle", KeyType.HASH), // Partition
                                // key
                                new KeySchemaElement("authToken", KeyType.RANGE)), // Sort key
                        Arrays.asList(new AttributeDefinition("user_handle", ScalarAttributeType.S),
                                new AttributeDefinition("authToken", ScalarAttributeType.S)),
                        new ProvisionedThroughput(1L, 1L));
                table.waitForActive();
                System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

            }
            catch (Exception e) {
                System.err.println("Unable to create table: ");
                System.err.println(e.getMessage());
            }
        }


        public void createFollowTable() throws Exception {
            DynamoDB dynamoDB = new DynamoDB(client);

            String tableName = "Follow";
            try {
                System.out.println("Attempting to create table; please wait...");
                Table table = dynamoDB.createTable(tableName,
                        Arrays.asList(new KeySchemaElement("follower_alias", KeyType.HASH), // Partition
                                // key
                                new KeySchemaElement("followee_alias", KeyType.RANGE)), // Sort key
                        Arrays.asList(new AttributeDefinition("follower_alias", ScalarAttributeType.S),
                                new AttributeDefinition("followee_alias", ScalarAttributeType.S)),
                        new ProvisionedThroughput(1L, 1L));
                table.waitForActive();
                System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());

            }
            catch (Exception e) {
                System.err.println("Unable to create table: ");
                System.err.println(e.getMessage());
            }
        }



    public boolean addItems(String primaryKey, String sortKey) {
        try {
            Table table = dynamoDB.getTable(tableName);
            System.out.println("in here");
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey(primaryKeyAttr, primaryKey).withString(sortKeyAttr, sortKey));

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
            return true;
        }catch (Exception e){
            System.err.println("Unable to add item: " + primaryKey + " " + sortKey);
            System.err.println(e.getMessage());
            return false;
        }
    }

        public boolean addItems(Map<String, Object> infoMap,
                                Object primaryKey, Object sortKey) throws Exception{
            try {
                Table table = dynamoDB.getTable(tableName);
                PutItemOutcome outcome = table
                        .putItem(new Item().withPrimaryKey(primaryKeyAttr, primaryKey, sortKeyAttr, sortKey).withMap("info", infoMap));

                System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
                return true;
            }catch (Exception e){
                System.err.println("Unable to add item: " + primaryKey + " " + sortKey);
                System.err.println(e.getMessage());
                return false;
            }
        }


    public Item getItem(Object primaryKey){

        Table table = dynamoDB.getTable(tableName);
        Item outcome = null;



        GetItemSpec spec = new GetItemSpec().withPrimaryKey(primaryKeyAttr, primaryKey);

        try {
            System.out.println("Attempting to read the item...");
            outcome = table.getItem(primaryKeyAttr, primaryKey);
            System.out.println("GetItem succeeded: " + outcome);

        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + primaryKey);
            System.err.println(e.getMessage());

        }

        return outcome;
    }

        public boolean deleteItems(DeleteItemSpec deleteItemSpec){
            try {
                Table table = dynamoDB.getTable(tableName);
                System.out.println("Attempting a conditional delete...");
                DeleteItemOutcome deleteItemOutcome = table.deleteItem(deleteItemSpec);
                System.out.println("DeleteItem succeeded");
                return true;
            } catch (Exception e) {
                System.err.println("Unable to delete item");
                System.err.println(e.getMessage());
                return false;
            }
        }

        /**
         * Delete the "visits" table and the "visits-index" global index
         *
         * @throws DataAccessException
         */
        public void deleteTable() throws DataAccessException {
            try {
                Table table = this.dynamoDB.getTable(tableName);
                if (table != null) {
                    table.delete();
                    table.waitForDelete();
                }
            }
            catch (Exception e) {
                throw new DataAccessException(e);
            }
        }

    /**
     * Fetch the next page of locations visited by visitor
     *
     * @param followee The visitor of interest
     * @param pageSize The maximum number of locations to include in the result
     * @param lastFollower The last location returned in the previous page of results
     * @return The next page of locations visited by visitor
     */
    public ResultsPage getFollowers(String followee, int pageSize, String lastFollower) {
        ResultsPage result = new ResultsPage();
        System.out.println("here");

        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#vis", primaryKeyAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":followee_handle", new AttributeValue().withS(followee));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(this.tableName)
                .withKeyConditionExpression("#vis = :followee_handle")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(pageSize);

        if (isNonEmptyString(lastFollower)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(primaryKeyAttr, new AttributeValue().withS(followee));
            startKey.put(sortKeyAttr, new AttributeValue().withS(lastFollower));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = this.client.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        System.out.println("here");
        if (items != null) {
            for (Map<String, AttributeValue> item : items){
                String location = item.get(sortKeyAttr).getS();
                result.addValue(location);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            result.setLastKey(lastKey.get(sortKeyAttr).getS());
        }

        return result;
    }


    /**
     * Fetch the next page of locations visited by visitor
     *
     * @param follower The visitor of interest
     * @param pageSize The maximum number of locations to include in the result
     * @param lastFollowee The last location returned in the previous page of results
     * @return The next page of locations visited by visitor
     */
    public ResultsPage getFollowees(String follower, int pageSize, String lastFollowee) {
        ResultsPage result = new ResultsPage();

        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#vis", primaryKeyAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follower_handle", new AttributeValue().withS(follower));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(this.tableName)
                .withKeyConditionExpression("#vis = :follower_handle")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(pageSize);

        if (isNonEmptyString(lastFollowee)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(primaryKeyAttr, new AttributeValue().withS(follower));
            startKey.put(sortKeyAttr, new AttributeValue().withS(lastFollowee));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = this.client.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items){
                String location = item.get(sortKeyAttr).getS();
                result.addValue(location);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            result.setLastKey(lastKey.get(sortKeyAttr).getS());
        }

        return result;
    }


    public List<Item> getBatchItems(Set<String> alias){
        List<Item> userItems = null;

        try {

			TableKeysAndAttributes forumTableKeysAndAttributes = new TableKeysAndAttributes(this.tableName);
			// Add a partition key
        String [] values = new String[alias.size()];

        Iterator<String> it = alias.iterator();
            int i = 0;
			while(it.hasNext()){
			    values[i] = it.next();
			    i++;
			}


			forumTableKeysAndAttributes.addHashOnlyPrimaryKeys(primaryKeyAttr, values);

			System.out.println("Making the request.");

			BatchGetItemOutcome outcome = dynamoDB.batchGetItem(forumTableKeysAndAttributes);

			Map<String, KeysAndAttributes> unprocessed = null;

			do {
				for (String tableName : outcome.getTableItems().keySet()) {
					System.out.println("Items in table " + tableName);
					List<Item> items = outcome.getTableItems().get(tableName);
					userItems = items;
					for (Item item : items) {
						System.out.println(item.toJSONPretty());
					}
				}

				// Check for unprocessed keys which could happen if you exceed
				// provisioned
				// throughput or reach the limit on response size.
				unprocessed = outcome.getUnprocessedKeys();

				if (unprocessed.isEmpty()) {
					System.out.println("No unprocessed keys found");
				}
				else {
					System.out.println("Retrieving the unprocessed keys");
					outcome = dynamoDB.batchGetItemUnprocessed(unprocessed);
				}

			} while (!unprocessed.isEmpty());
//
     	}
		catch (Exception e) {
			System.err.println("Failed to retrieve items.");
			System.err.println(e.getMessage());
		}

        return userItems;
    }

    public ResultsPage getItems(String primaryKey, int pageSize, String lastPrimaryKey) {
        ResultsPage result = new ResultsPage();

        Map<String, String> attrNames = new HashMap<String, String>();
        attrNames.put("#vis", primaryKeyAttr);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        String pKey = ":" + primaryKeyAttr;
        String compare = "#vis = "  + pKey;
        attrValues.put(pKey, new AttributeValue().withS(primaryKey));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(this.tableName)
                .withKeyConditionExpression(compare)
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(pageSize);

        if (isNonEmptyString(lastPrimaryKey)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(primaryKeyAttr, new AttributeValue().withS(primaryKey));
            startKey.put(sortKeyAttr, new AttributeValue().withS(primaryKey));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = this.client.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        if (items != null) {
            for (Map<String, AttributeValue> item : items){
                if(item != null) {
                    String sortVal = item.get(sortKeyAttr).getS();
                    System.out.println("in here");
                    System.out.println(sortVal);
                    result.addItems(item);
                }
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        if (lastKey != null) {
            result.setLastKey(lastKey.get(sortKeyAttr).getS());
        }

        return result;
    }


    public void writeToBatch(List<String> aliases, String date,  Map<String, Object> infoMap) {

        // Constructor for TableWriteItems takes the name of the table, which I have stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(tableName);

        // Add each user into the TableWriteItems object
        for (String alias: aliases) {
//            final Map<String, Object> infoMap = new HashMap<String, Object>();
//            infoMap.put("FirstName", user.getFirstName());
//            infoMap.put("LastName", user.getLastName());
            Item item = new Item()
                    .withPrimaryKey(primaryKeyAttr, alias, sortKeyAttr, date)
                    .withMap("info", infoMap);
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(tableName);
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }

    public Item getItemWithHash(Object primaryKey, Object hashKey){

        Table table = dynamoDB.getTable(tableName);
        Item outcome = null;



        GetItemSpec spec = new GetItemSpec().withPrimaryKey(primaryKeyAttr, primaryKey, sortKeyAttr, hashKey);

        try {
            System.out.println("Attempting to read the item...");
            outcome = table.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);

        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + primaryKey);
            System.err.println(e.getMessage());

        }

        return outcome;
    }




}
