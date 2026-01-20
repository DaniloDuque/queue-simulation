package org.sim.model;

public class Client {
	final private Integer id;
	private int openOrders;
	private int completedOrders;

	public Client(int id) {
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
