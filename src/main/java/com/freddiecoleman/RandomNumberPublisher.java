package com.freddiecoleman;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.pushtechnology.diffusion.api.APIException;
import com.pushtechnology.diffusion.api.publisher.Publisher;

/**
 * Main RandomNumberPublisher class
 * 
 * @author Freddie Coleman
 *
 */
public class RandomNumberPublisher extends Publisher {
	
	public static final String NUMBERS_TOPIC = "numbers";

	@Override
	protected void initialLoad() throws APIException {
		addTopic(NUMBERS_TOPIC);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new RandomNumberGenerator(this), 1L, 10L, TimeUnit.SECONDS);
	}

	@Override
	protected boolean isStoppable() {
		return true;
	}

}