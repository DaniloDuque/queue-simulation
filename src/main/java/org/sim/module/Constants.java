package org.sim.module;

public class Constants {
	public static final double SECONDS_IN_MINUTE = 60;
	public static final double SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;

	// routing probabilities
	public static final double CASHIER_PROB = 1.0;
	public static final double DRINKS_PROB = 0.9;
	public static final double FRYER_PROB = 0.7;
	public static final double CHICKEN_PROB = 0.3;

	// number of workers per station (total must be 12)
	public static final int TOTAL_NUMBER_OF_WORKERS = 12;

	// number of stations
	public static final int NUMBER_OF_STATIONS = 4;

	// Station distribution parameters
	public static final double CASHIER_STATION_MEAN = 2.5;
	public static final double DRINKS_STATION_MEAN = 0.75;
	public static final double FRYER_STATION_MEAN = 3.0;
	public static final double FRYER_STATION_STD = 0.5;
	public static final double CHICKEN_STATION_P = 0.1;

	// Price per worker on stations
	public static final double CASHIER_WORKER_PRICE = 500;
	public static final double DRINKS_WORKER_PRICE = 750;
	public static final double FRYER_WORKER_PRICE = 200;
	public static final double CHICKEN_WORKER_PRICE = 100;

	// Client arrival rate lambda
	public static final double CLIENT_ARRIVAL_RATE_PER_MINUTE = 2.0 / 10;
	public static final double CLIENT_ARRIVAL_RATE_PER_SECOND = CLIENT_ARRIVAL_RATE_PER_MINUTE / SECONDS_IN_MINUTE;

	// Client number of order per station parameters
	public static final int CLIENT_NUMBER_OF_ORDER_PER_STATION_N = 5;
	public static final double CLIENT_NUMBER_OF_ORDER_PER_STATION_P = 2.0 / 5.0;

	// Simulation Parameters
	public static final int THREAD_POOL_SIZE = 50;
	public static final int NUMBER_OF_SIMULATIONS_PER_COMBINATION = 5;
	public static final double SIMULATION_TIME_IN_SECONDS = 8.0 * SECONDS_IN_HOUR;
	public static final double BUDGET = 2000.0;
	public static final double MAX_TIME = 3.0 * SECONDS_IN_MINUTE;
}
