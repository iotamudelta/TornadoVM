/*
 * This file is part of Tornado: A heterogeneous programming framework:
 * https://github.com/beehive-lab/tornadovm
 *
 * Copyright (c) 2013-2020, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Authors: James Clarkson
 *
 */
package uk.ac.manchester.tornado.runtime.sketcher;

import org.graalvm.compiler.graph.Graph;

import uk.ac.manchester.tornado.api.common.Access;

public class Sketch {

    private final Graph graph;

    /**
     * Argument accesses of the {@link #graph}. If arguments escape to callees, it
     * will contain the updated accesses based on what the non-inlined methods do.
     */
    private final Access[] argumentsAccess;

    Sketch(Graph graph, Access[] argumentAccesses) {
        this.graph = graph;
        this.argumentsAccess = argumentAccesses;
    }

    public Graph getGraph() {
        return graph;
    }

    public Access[] getArgumentsAccess() {
        return argumentsAccess;
    }

}
