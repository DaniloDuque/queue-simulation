package org.sim.distribution;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor(onConstructor_ = @Inject)
public class ExponentialDistribution implements ServiceTimeDistribution {

    private final double mean;

    private final Random rng = new Random();

    @Override
    public double sample() {
        return -mean * Math.log(1 - rng.nextDouble());
    }
}
