package org.sim.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
	final private Integer id;
	private int openOrders;
	private int completedOrders;

	public Client(final int id) {
		this.id = id;
		this.openOrders = 0;
		this.completedOrders = 0;
	}

	public void addOpenOrder() {
		openOrders++;
	}

	public void addCompletedOrder() {
		completedOrders++;
	}

	public boolean isReady() {
		return openOrders == completedOrders;
	}
}
