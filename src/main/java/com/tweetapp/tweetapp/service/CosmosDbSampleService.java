package com.tweetapp.tweetapp.service;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.PartitionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CosmosDbSampleService {

    private final CosmosClient cosmosClient;
    private final String databaseName = "tweetapp-db"; // Change as needed
    private final String containerName = "sample-container"; // Change as needed

    @Autowired
    public CosmosDbSampleService(CosmosClient cosmosClient) {
        this.cosmosClient = cosmosClient;
    }

    public void createSampleItem(Object item, String partitionKeyValue) {
        CosmosDatabase database = cosmosClient.getDatabase(databaseName);
        CosmosContainer container = database.getContainer(containerName);
        CosmosItemResponse<Object> response = container.createItem(item, new PartitionKey(partitionKeyValue), null);
        System.out.println("Item created with status code: " + response.getStatusCode());
    }
}
