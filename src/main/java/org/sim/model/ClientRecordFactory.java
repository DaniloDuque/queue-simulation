package org.sim.model;

import java.util.HashMap;

public class ClientRecordFactory {
	public static ClientRecord create() {
		return new ClientRecord(new HashMap<>());
	}
}
