package org.sim.module;

public class Constants {

	public static final double SECONDS_IN_MINUTE = 60;

	// routing probabilities
	public static final double DRINKS_PROB = 0.9;
	public static final double FRIER_PROB = 0.7;
	public static final double DESERT_PROB = 0.25;
	public static final double CHICKEN_PROB = 0.3;

	// station names
	public static final String CASHIER_STATION_NAME = "Cashier";
	public static final String DRINKS_STATION_NAME = "Drinks";
	public static final String FRIER_STATION_NAME = "Frier";
	public static final String DESERT_STATION_NAME = "Desert";
	public static final String CHICKEN_STATION_NAME = "Chicken";

	// number of workers per station (total must be 12)
	public static final int NUMBER_OF_CASHIER_WORKERS = 3;
	public static final int NUMBER_OF_DRINKS_WORKERS = 2;
	public static final int NUMBER_OF_FRIER_WORKERS = 2;
	public static final int NUMBER_OF_DESERT_WORKERS = 1;
	public static final int NUMBER_OF_CHICKEN_WORKERS = 5;

	// Station distribution parameters
	public static final double CASHIER_STATION_MEAN = 2.5 * SECONDS_IN_MINUTE;
	public static final double DRINKS_STATION_MEAN = 0.75 * SECONDS_IN_MINUTE;
	public static final double FRIER_STATION_MEAN = 3.0 * SECONDS_IN_MINUTE;
	public static final double FRIER_STATION_STD = 0.5 * SECONDS_IN_MINUTE;
	public static final int DESERT_STATION_N = 5;
	public static final double DESERT_STATION_P = 0.6;
	public static final double CHICKEN_STATION_P = 0.1;

	// Client arrival rate lambda
	public static final double CLIENT_ARRIVAL_RATE_PER_SECOND = 3.0 / SECONDS_IN_MINUTE;

	// Client number of order per station parameters
	public static final int CLIENT_NUMBER_OF_ORDER_PER_STATION_N = 5;
	public static final double CLIENT_NUMBER_OF_ORDER_PER_STATION_P = 2.0 / 5.0;
}
