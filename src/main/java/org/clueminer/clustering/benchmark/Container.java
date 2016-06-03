/*
 * Copyright (C) 2011-2016 clueminer.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.clueminer.clustering.benchmark;

import java.util.logging.Logger;
import org.clueminer.clustering.TreeDiff;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.ClusteringAlgorithm;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.utils.Props;

/**
 *
 * @author Tomas Barton
 * @param <E>
 */
public abstract class Container<E extends Instance> implements Runnable {

    private HierarchicalResult result;
    private Clustering clustering;
    private final ClusteringAlgorithm algorithm;
    private final Dataset<E> dataset;
    private Props params;
    private static final Logger logger = Logger.getLogger(Container.class.getName());

    public Container(ClusteringAlgorithm algorithm, Dataset<E> dataset) {
        this.algorithm = algorithm;
        this.dataset = dataset;
        this.params = new Props();
    }

    public Container(AgglomerativeClustering algorithm, Dataset<E> dataset, Props params) {
        this.algorithm = algorithm;
        this.dataset = dataset;
        this.params = params;
    }

    public abstract HierarchicalResult hierarchical(AgglomerativeClustering algorithm, Dataset<E> dataset, Props params);

    @Override
    public void run() {
        if (algorithm instanceof AgglomerativeClustering) {
            this.result = hierarchical((AgglomerativeClustering) algorithm, dataset, params);
        } else {
            this.clustering = cluster(algorithm, dataset, params);
        }
    }

    public Clustering cluster(ClusteringAlgorithm algorithm, Dataset<E> dataset, Props params) {
        return algorithm.cluster(dataset, params);
    }

    public boolean equals(Container other) {
        if (this.result == null || other.result == null) {
            throw new RuntimeException("got null result. this = " + result + " other = " + other);
        }
        return TreeDiff.compare(this.result, other.result);
    }

}
