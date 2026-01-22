package org.sim.model.client;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor(onConstructor_ = @Inject)
public class ClientRecord {
	private Map<Integer, Client> clientMap;

	public void openClientOrder(final int clientId) throws NoSuchElementException {
		clientMap.putIfAbsent(clientId, new Client(clientId)); // Already handles existing
		getClient(clientId).addOpenOrder();
	}

	public void completeClientOrder(final int clientId) throws NoSuchElementException {
		getClient(clientId).addCompletedOrder();
	}

	public boolean isClientReady(final int clientId) throws NoSuchElementException {
		return getClient(clientId).isReady();
	}

	public int getAmount() {
		return clientMap.size();
	}

	private Client getClient(final int clientId) throws NoSuchElementException {
		final Client client = clientMap.get(clientId);
		if (client == null) {
			throw new NoSuchElementException();
		}
		return client;
	}

}
