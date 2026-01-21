# Queue Simulation - Fast Food Restaurant

A discrete event simulation system for optimizing worker allocation in a fast food restaurant with multiple service stations.

## Overview

This Java-based simulation models a fast food restaurant with 5 service stations:
- **Cashier** (entry point - all customers)
- **Drinks** (90% probability)
- **Frier** (70% probability) 
- **Desert** (25% probability)
- **Chicken** (30% probability)

The system tests all possible worker configurations (12 total workers) to find the optimal allocation that minimizes customer wait times.

## Architecture

### Core Components
- **Event-Driven Simulation**: Priority queue-based discrete event processing
- **Service Stations**: Each with configurable workers and service time distributions
- **Order Routing**: Probabilistic routing with order splitting/merging
- **Statistics Tracking**: Comprehensive metrics collection and analysis
- **Parallel Processing**: Multi-threaded configuration testing

### Key Classes
- `SimulationEngine`: Core event processing engine
- `ServiceStation`: Individual station with queues and workers
- `CombinationTester`: Tests all worker allocation combinations
- `EventGenerator`: Generates customer arrivals (Poisson process)
- `Order`: Represents customer orders with routing workflows

## Configuration

### Main Parameters (`Constants.java`)
```java
// Simulation duration
SIMULATION_TIME_IN_SECONDS = 8 * 60 * 60  // 8 hours

// Customer arrival rate
CLIENT_ARRIVAL_RATE_PER_MINUTE = 3.0

// Station routing probabilities
DRINKS_PROB = 0.9
FRIER_PROB = 0.7
DESERT_PROB = 0.25
CHICKEN_PROB = 0.3

// Service time distributions
CASHIER_STATION_MEAN = 2.5        // Exponential
DRINKS_STATION_MEAN = 0.75        // Exponential
FRIER_STATION_MEAN = 3.0          // Normal (σ=0.5)
DESERT_STATION_N = 5, P = 0.6     // Binomial
CHICKEN_STATION_P = 0.1           // Geometric

// Testing parameters
NUMBER_OF_SIMULATIONS_PER_COMBINATION = 10
TOTAL_NUMBER_OF_WORKERS = 12
```

### Service Time Distributions
- **Cashier**: Exponential (mean=2.5 min)
- **Drinks**: Exponential (mean=0.75 min)
- **Frier**: Normal (mean=3.0, std=0.5 min)
- **Desert**: Binomial (n=5, p=0.6)
- **Chicken**: Geometric (p=0.1)

## How to Run

### Prerequisites
- Java 22+
- Gradle 8.14+

### Quick Start
```bash
# Run with build script (recommended)
./build-format.sh

# Or run directly
./gradlew run

# Clean build only
./gradlew clean build
```

### Build Script Features
The `build-format.sh` script performs:
1. Clean build artifacts
2. Apply code formatting (Spotless)
3. Build project
4. Run simulation

## Output

The simulation tests all valid worker configurations and outputs:

```
=== BEST CONFIGURATION ===
Configuration: {CASHIER=4, DRINKS=1, FRIER=2, DESERT=1, CHICKEN=4}
Average served clients: 150.2
Average wait time: 8.45
Min wait time: 6.12
Max wait time: 12.34
Wait time standard deviation: 2.15
```

### Metrics Tracked
- **Wait Time**: Time customers spend in queues
- **Served Clients**: Total customers processed
- **Station Utilization**: Worker efficiency per station
- **Statistical Confidence**: Multiple runs with std deviation

## Project Structure

```
src/main/java/org/sim/
├── Main.java                    # Application entry point
├── assignment/                  # Worker allocation logic
├── distribution/               # Service time distributions
├── engine/                     # Simulation engine core
├── event/                      # Event system (arrivals, departures)
├── model/                      # Domain models (Order, Client)
├── module/                     # Dependency injection config
├── stat/                       # Statistics collection
├── station/                    # Service station logic
└── tester/                     # Configuration testing
```

## Dependencies

- **Google Guice**: Dependency injection
- **Apache Commons Math**: Statistical distributions
- **SLF4J + Logback**: Logging
- **Lombok**: Code generation
- **JUnit 5**: Testing

## Customization

### Modify Station Parameters
Edit `Constants.java` to change:
- Service time distributions
- Routing probabilities  
- Arrival rates
- Simulation duration

### Add New Stations
1. Add station to `StationName` enum
2. Configure in `SimulationModule`
3. Update routing probabilities
4. Adjust total worker count

### Change Optimization Criteria
Modify `ConfigurationResult.compareTo()` to optimize for different metrics (utilization, throughput, etc.)

## Performance

- **Parallel Execution**: Uses thread pool for configuration testing
- **Memory Efficient**: Streams-based processing
- **Scalable**: Handles large parameter spaces efficiently

Current setup tests ~100 configurations with 10 runs each (1000 total simulations).
