# Assignment V: Queue System Simulation for a Fast Food Restaurant
## Workflow Simulation

## 1. Introduction

This document describes the specification for a queue system simulation in a fast food restaurant. The system models a workflow with multiple service stages, where customers do not necessarily go through all phases, but rather each stage is activated with a specific probability based on the customer's order. The main objective of the simulation is to evaluate and minimize the average waiting time of customers in the system, modeled as an open queueing network with probabilistic routing.

Additionally, an alternative server configuration is proposed to minimize the variance of waiting time, maintaining a focus on workflow efficiency. The simulation will be implemented using discrete event simulation techniques, assuming Poisson customer arrivals and service times with specific distributions per stage.

The system consists of the following stages (service stations):

- **Cashiers (Cajas)**: Order reception and payment.
- **Drinks (Refrescos)**: Beverage preparation.
- **Fryer (Freidora)**: Preparation of fried products (e.g., fries).
- **Desserts (Postres)**: Dessert preparation.
- **Chicken (Pollo)**: Chicken item preparation.

Each customer begins at the cashiers and is then probabilistically routed to subsequent stages, according to their order needs. The system is a series-parallel queueing network with branching probabilities.

## 2. Queue Model

The system is modeled as an open Jackson queueing network, where:

- Customer arrivals follow a Poisson process with rate **λ = 3** (adjustable in the simulation).
- Each station *i* (for *i* ∈ {cashiers, drinks, fryer, desserts, chicken}) has *c<sub>i</sub>* parallel servers, with independent service times.
- Service times at each station follow specific distributions (exponential for simplicity, capturing variability in fast food operations):

### Service Time Distributions:

- **Cashiers**: Exponential distribution with mean **μ<sup>-1</sup><sub>cashiers</sub> = 2.5 minutes**.
- **Drinks**: Exponential distribution with mean **μ<sup>-1</sup><sub>drinks</sub> = 0.75 minutes**.
- **Fryer**: Discrete normal distribution with mean **μ<sup>-1</sup><sub>fryer</sub> = 3 minutes** (rate μ<sub>fryer</sub> ≈ 0.333 per minute).
- **Desserts**: Binomial distribution with mean **μ<sup>-1</sup><sub>desserts</sub>**, n = 5 and p = 0.6 minutes (rate μ<sub>desserts</sub> ≈ 0.667 per minute).
- **Chicken**: Geometric distribution with p = 0.1 and x being the minutes.

### Queue Discipline:

- FCFS (First-Come, First-Served) discipline is assumed at each queue, with infinite capacity to avoid rejections.
- The total waiting time of a customer is the sum of waiting times in queue and service at the visited stations.

For a queueing network with probabilistic routing, the average waiting time **W** in the system is calculated as:

```
W = Σᵢ pᵢ [ρᵢ / (μᵢ(1 - ρᵢ)) + 1/μᵢ]
```

where:
- **ρᵢ = λᵢ/(cᵢμᵢ)** is the utilization of station *i*
- **λᵢ** is the effective arrival rate to *i* (adjusted by routing probabilities)
- **pᵢ** is the probability of visiting *i*
- The first term represents the time in queue (M/M/c approximation)

### 2.1 Number of Orders per Server

Each customer will have a number of orders at the servers, which follows a binomial distribution with **n = 5** and **p = 2/5**.

## 3. Workflow and Routing Probabilities

Customers do not go through all phases; instead, after the cashiers (which all visit with probability **p<sub>cashiers</sub> = 1**), they are probabilistically routed to subsequent stages. The probabilities are based on typical fast food ordering patterns (e.g., not everyone orders chicken or desserts). The following conditional visit probabilities are defined (independent between stages for simplicity):

### Routing Probabilities:

- **Probability of needing drinks after cashiers**: p<sub>drinks</sub> = 0.9 (most order beverages).
- **Probability of needing fryer** (e.g., fries, nuggets, etc.): p<sub>fryer</sub> = 0.7.
- **Probability of needing desserts**: p<sub>desserts</sub> = 0.25.
- **Probability of needing chicken**: p<sub>chicken</sub> = 0.3.

The routing is probabilistic: a customer leaves a station and independently decides whether to visit the next one with its respective probability. The effective flow **λᵢ = λ ∏<sub>j previous</sub> p<sub>j</sub>** for stations in sequence, but given the parallelism, it is simulated by visiting possible subsets.

The simulation must generate customer trajectories by sampling these probabilities to determine each one's sub-workflow (e.g., a customer could go: cashiers → drinks → fryer, without chicken or desserts).

## 4. Servers

The restaurant has **12 employees**. Your task is to indicate how many should work at each of the stations, such that the average waiting time and average variance are minimized.

## 5. Simulation Optimization Objectives

The simulation will run for a time horizon of **T = 8 hours** (one shift), replicating many independent runs to estimate averages and variances.

### Key Metrics:

- **Average waiting time per customer**: W̄
- **Variance of waiting time**: Var(W)
- **Utilization per station**: ρᵢ < 0.8 for stability

### 5.1 Configuration to Minimize Average Waiting Time

To minimize W̄, optimize the number of servers *c<sub>i</sub>* by assigning more resources to bottlenecks (stations with high probabilities and long times, such as chicken and fryer).

#### Example Configuration:

- **c<sub>cashiers</sub> = 3** (high arrival rate)
- **c<sub>drinks</sub> = 2** (high probability, short time)
- **c<sub>fryer</sub> = 2** (medium probability, medium time)
- **c<sub>desserts</sub> = 1** (low probability)
- **c<sub>chicken</sub> = 5** (long time, medium probability)

**Note**: Total must equal 12 servers: 3 + 2 + 2 + 1 + 4 = 12

In the simulation, this configuration reduces W̄ to approximately **5-7 minutes** (analytical estimate: solve the flow equation system and use M/M/c formulas for each queue, weighted by probabilities).

## 6. Simulation Evaluation

This section describes the evaluation process for the fast food restaurant queueing system simulation. The objective is to establish a clear and quantifiable criterion to assign points to each probabilistic data generation based on its alignment with the system specification. The evaluation focuses on how each configuration and simulation execution correlates with the obtained average waiting time and its comparability with other similar approaches.

### 6.1 Evaluation Criteria

The evaluation will be based on the following criteria:

1. **Alignment with Specification**: Each probabilistic data generation must comply with the requirements established in the system specification, including routing probabilities and workflow structure.

2. **Average Waiting Time**: The average waiting time per customer obtained in the simulation will be measured. This average must be verifiable with executions of other configurations or simulations operating under similar conditions.

3. **Result Comparability**: Results are expected to be comparable with other simulations addressing the same problem. This implies that waiting times must be in a reasonable range compared to results from other approaches.

### 6.2 Point Assignment

The evaluation is performed on a scale of **0 to 100 points**, where:

#### Alignment with Specification (40 points):
- **0 points** if criteria are not met
- **20 points** if there is partial compliance (some probabilities or stages are not implemented correctly)
- **40 points** if all specifications are met

#### Average Waiting Time (50 points):
Points will be assigned based on the average waiting time compared to the minimum expected average. The score will be calculated with the following formula:

```
Score = 50 × [1 - (W̄ - W̄min) / (W̄max - W̄min)]
```

where:
- **W̄** is the average waiting time obtained in the simulation
- **W̄min** is the minimum achievable average
- **W̄max** is the maximum average observed in comparable simulations

#### Result Comparability (10 points):
- **0 points** if results are not comparable
- **5 points** if results are partially comparable (similarity in trends, but not in magnitudes)
- **10 points** if results are totally comparable with other similar approaches

### 6.3 Final Score Calculation

The final score of each simulation execution will be calculated by summing the points obtained in each criterion:

```
Total Score = Alignment Points + Average Waiting Time Points + Comparability Points
```

The objective is that each simulation achieves a total score close to **100**, which would indicate that a verifiable minimum average waiting time has been achieved and all criteria established in the specification have been met.

This evaluation will allow an effective comparison between different configurations and approaches, providing a solid foundation for continuous improvement of the queueing system in the fast food restaurant.