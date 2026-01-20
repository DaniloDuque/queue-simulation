package org.sim.assignment;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CompositionGenerator implements Iterator<int[]> {
	private final int workers;
	private final int stations;
	private final int[] current;
	private boolean hasNext = true;

	public CompositionGenerator(final int workers, final int stations) throws IllegalArgumentException {
		if (stations <= 0 || workers < stations)
			throw new IllegalArgumentException("Workers must be >= stations >= 1");

		this.workers = workers;
		this.stations = stations;
		this.current = new int[stations];

		// Initial composition: [1,1,..., remaining]
		for (int i = 0; i < stations - 1; i++)
			current[i] = 1;
		current[stations - 1] = workers - (stations - 1);
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public int[] next() throws NoSuchElementException {
		if (!hasNext)
			throw new NoSuchElementException();

		// Return a copy of current
		int[] result = current.clone();

		// Generate next composition
		advance();

		return result;
	}

	private void advance() {
		int i = stations - 2;

		// find rightmost element that can still be increased
		while (i >= 0 && current[i] == workers - sumPrefix(i) - (stations - i - 1)) {
			i--;
		}

		// no such position → finished
		if (i < 0) {
			hasNext = false;
			return;
		}

		// increase this position
		current[i]++;

		// redistribute remaining workers to the right
		int used = sumPrefix(i + 1);
		int remaining = workers - used;

		for (int j = i + 1; j < stations - 1; j++) {
			current[j] = 1;
			remaining--;
		}

		current[stations - 1] = remaining;
	}

	private int sumPrefix(final int endExclusive) {
		int sum = 0;
		for (int i = 0; i < endExclusive; i++)
			sum += current[i];
		return sum;
	}

}
