package org.sim.model;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor(onConstructor_ = @Inject)
public class Clients {
	private Map<Integer, Client> clientMap;

	public void openClientOrder(final int clientId) throws NoSuchElementException {
		getClient(clientId).addOpenOrder();
	}

	public void completeClientOrder(final int clientId) throws NoSuchElementException {
		getClient(clientId).addCompletedOrder();
	}

	public boolean isClientReady(final int clientId) throws NoSuchElementException {
		return getClient(clientId).isReady();
	}

	private Client getClient(final int clientId) throws NoSuchElementException {
		final Client client = clientMap.get(clientId);
		if (client == null) {
			throw new NoSuchElementException();
		}
		return client;
	}

}
