package com.habitron.engine.model;

import java.util.List;

/**
 * The core logic for calculating decision outcomes.
 * Performs weighted sum calculations and handles normalization.
 */
public class ScoringEngine {

    /**
     * Calculate base scores for all options in a session.
     * returns an array of scores corresponding to the options list.
     */
    public double[] calculateBaseScores(DecisionSession session) {
        List<Option> options = session.getOptions();
        List<Criterion> criteria = session.getCriteria();
        double[][] scores = session.getScores();

        double[] finalScores = new double[options.size()];

        for (int i = 0; i < options.size(); i++) {
            double total = 0.0;
            for (int j = 0; j < criteria.size(); j++) {
                // Weighted sum: score * weight
                // scores are 0-10, weights sum to 1.0.
                // Final score will be 0-10.
                total += scores[i][j] * criteria.get(j).getWeight();
            }
            finalScores[i] = total;
        }

        return finalScores;
    }

    /**
     * Find the index of the highest scoring option.
     */
    public int getRecommendedIndex(double[] finalScores) {
        if (finalScores == null || finalScores.length == 0) return -1;
        
        int bestIdx = 0;
        double bestVal = finalScores[0];
        
        for (int i = 1; i < finalScores.length; i++) {
            if (finalScores[i] > bestVal) {
                bestVal = finalScores[i];
                bestIdx = i;
            }
        }
        
        return bestIdx;
    }
}
