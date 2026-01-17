package org.sim.module;

public class Constants {

    // routing probabilities
    public static final double CASHIER_PROB = 1;
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

    // number of workers per station
    public static final int NUMBER_OF_CASHIER_WORKERS = 2;
    public static final int NUMBER_OF_DRINKS_WORKERS = 2;
    public static final int NUMBER_OF_FRIER_WORKERS = 2;
    public static final int NUMBER_OF_DESERT_WORKERS = 1;
    public static final int NUMBER_OF_CHICKEN_WORKERS = 1;

    // Workflow parameters
    public static final long MAX_CAPACITY = 10;
    public static final long COLLABORATORS = 12;

    // Station distribution parameters
    public static final double CASHIER_STATION_MEAN = 2.5 * 60.0;
    public static final double DRINKS_STATION_MEAN = 0.75 * 60.0;
    public static final double FRIER_STATION_MEAN = 3.0 * 60.0;
    public static final double FRIER_STATION_STD = 1.0 * 60.0;
    public static final int DESERT_STATION_N = 5;
    public static final double DESERT_STATION_P = 0.6;
    public static final double CHICKEN_STATION_P = 0.1;

    // Client arrival rate lambda
    public static final double ARRIVAL_LAMBDA = 0.01;
}
