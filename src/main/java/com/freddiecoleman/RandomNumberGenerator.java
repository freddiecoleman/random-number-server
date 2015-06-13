package com.freddiecoleman;

import java.util.Random;

import com.pushtechnology.diffusion.api.APIException;
import com.pushtechnology.diffusion.api.Logs;
import com.pushtechnology.diffusion.api.data.TopicDataFactory;
import com.pushtechnology.diffusion.api.data.metadata.MDataType;
import com.pushtechnology.diffusion.api.data.record.RecordTopicData;
import com.pushtechnology.diffusion.api.data.single.SingleValueTopicData;
import com.pushtechnology.diffusion.api.topic.Topic;

public class RandomNumberGenerator implements Runnable {
	
	private RandomNumberPublisher publisher;
	private Random randomGenerator;
	
	public RandomNumberGenerator(RandomNumberPublisher publisher){
		this.publisher = publisher;
		this.randomGenerator = new Random();
	}

	public void run() {
		Logs.finest("Generating random number...");
		updateNumber();
	}
	
	private void updateNumber(){
		Topic numbersTopic = publisher.getTopic("numbers");
		
		if(numbersTopic == null) {
            try {
            	SingleValueTopicData newRandomNumberData = TopicDataFactory.newSingleValueData(MDataType.INTEGER_STRING);
            	newRandomNumberData.initialise(randomGenerator.nextInt(1000));
                numbersTopic = publisher.addTopic("numbers", newRandomNumberData);
            }
            catch(APIException ex) {
                Logs.warning("Failed to create a new numbers topic", ex);
            }
        } else {
        	SingleValueTopicData newRandomNumberData = (SingleValueTopicData) numbersTopic.getData();
        	try {
            	newRandomNumberData.startUpdate();
            	newRandomNumberData.update(randomGenerator.nextInt(1000));
                if(newRandomNumberData.hasChanges()) {
                	newRandomNumberData.publishMessage(newRandomNumberData.generateDeltaMessage());
                }
            }
            catch(APIException ex) {
                Logs.warning("Failed to update a random number", ex);
                newRandomNumberData.abortUpdate();
            }
            finally {
            	newRandomNumberData.endUpdate();
            }
        }
	}

}