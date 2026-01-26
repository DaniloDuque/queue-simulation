package org.sim.graph;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sim.stat.multiple.TestResult;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class SimpleGraphGenerator {

	private static final String OUTPUT_DIR = "graphs";

	public static void generateGraphs(@NonNull final String scenarioName,
			@NonNull final List<TestResult> results) {
		new File(OUTPUT_DIR).mkdirs();

		if (!results.isEmpty()) {
			// Only use the best result (first one, since they're sorted by wait time)
			final TestResult bestResult = results.get(0);
			final String configName = scenarioName + "_Best_Config";
			generateFrequencyGraphs(configName, bestResult);
		}
	}

	private static void generateFrequencyGraphs(@NonNull final String configName,
			@NonNull final TestResult result) {
		final Map<Integer, Integer> frequencies = new HashMap<>();
		result.allWaitTimes().forEach(time -> {
			final int rounded = (int) Math.round(time);
			frequencies.merge(rounded, 1, Integer::sum);
		});

		// Absolute frequency
		final DefaultCategoryDataset absDataset = new DefaultCategoryDataset();
		frequencies.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEach(entry -> absDataset.addValue(entry.getValue(), "Count", entry.getKey() + "s"));

		final JFreeChart absChart = ChartFactory.createBarChart(
				"Wait Time Distribution - " + configName,
				"Wait Time (seconds)", "Frequency", absDataset,
				PlotOrientation.VERTICAL, false, true, false);

		// Add statistics as subtitle
        absChart.addSubtitle(new org.jfree.chart.title.TextTitle(
                String.format("Mean: %.1fs | Median: %.1fs | Mode: %ds | Range: %.1fs | Variance: %.1f",
						result.averageWaitTime(), result.median(), result.mode(),
						result.maxWaitTime() - result.minWaitTime(), result.variance())));

		absChart.addSubtitle(new org.jfree.chart.title.TextTitle(
				String.format("Q1: %.1fs | Q3: %.1fs | P90: %.1fs | P95: %.1fs | P99: %.1fs",
						result.q1(), result.q3(), result.p90(), result.p95(), result.p99())));

		// Style the chart
		absChart.setBackgroundPaint(java.awt.Color.WHITE);
		absChart.getPlot().setBackgroundPaint(new java.awt.Color(245, 245, 245));

		saveChart(absChart, configName + "_absolute_freq.png");

		// Relative frequency
		final DefaultCategoryDataset relDataset = new DefaultCategoryDataset();
		final int total = result.allWaitTimes().size();
		frequencies.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.forEach(entry -> relDataset.addValue((double) entry.getValue() / total, "Proportion",
						entry.getKey() + "s"));

		final JFreeChart relChart = ChartFactory.createBarChart(
				"Wait Time Distribution (Relative) - " + configName,
				"Wait Time (seconds)", "Relative Frequency", relDataset,
				PlotOrientation.VERTICAL, false, true, false);

		// Add same statistics
		relChart.addSubtitle(new org.jfree.chart.title.TextTitle(
                String.format("Mean: %.1fs | Median: %.1fs | Mode: %ds | Range: %.1fs | Variance: %.1f",
						result.averageWaitTime(), result.median(), result.mode(),
						result.maxWaitTime() - result.minWaitTime(), result.variance())));

		relChart.addSubtitle(new org.jfree.chart.title.TextTitle(
				String.format("Q1: %.1fs | Q3: %.1fs | P90: %.1fs | P95: %.1fs | P99: %.1fs",
						result.q1(), result.q3(), result.p90(), result.p95(), result.p99())));

		// Style the chart
		relChart.setBackgroundPaint(java.awt.Color.WHITE);
		relChart.getPlot().setBackgroundPaint(new java.awt.Color(245, 245, 245));

		saveChart(relChart, configName + "_relative_freq.png");

		// Covariances
		final DefaultCategoryDataset covDataset = new DefaultCategoryDataset();
		result.stationCovariances()
				.forEach((pair, cov) -> covDataset.addValue(cov, "Covariance", pair.replace("-", " - ")));

		final JFreeChart covChart = ChartFactory.createBarChart(
				"Station Covariances - " + configName, "Station Pairs", "Covariance",
				covDataset, PlotOrientation.VERTICAL, false, true, false);

		covChart.setBackgroundPaint(java.awt.Color.WHITE);
		covChart.getPlot().setBackgroundPaint(new java.awt.Color(245, 245, 245));

		saveChart(covChart, configName + "_covariances.png");
	}

	private static void saveChart(@NonNull final JFreeChart chart, @NonNull final String filename) {
		try {
			ChartUtils.saveChartAsPNG(new File(OUTPUT_DIR, filename), chart, 800, 600);
			log.info("Generated: {}", filename);
		} catch (IOException e) {
			log.error("Failed to save chart: {}", filename, e);
		}
	}
}
