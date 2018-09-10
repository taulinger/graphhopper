/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.graphhopper.swl;

import com.graphhopper.GHRequest;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;

public class DefaultSpeedCalculator implements SpeedCalculator {
    private final FlagEncoder encoder;

    public DefaultSpeedCalculator(FlagEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public double getSpeed(EdgeIteratorState edgeState, boolean reverse, int durationSeconds, String streetMode, GHRequest req) {
        long flags = edgeState.getFlags();
        if (reverse && !encoder.isBackward(flags)
                || !reverse && !encoder.isForward(flags))
            throw new IllegalStateException("Calculating time should not require to read speed from edge in wrong direction. "
                    + "Reverse:" + reverse + ", fwd:" + encoder.isForward(flags) + ", bwd:" + encoder.isBackward(flags));

        return reverse ? encoder.getReverseSpeed(flags) : encoder.getSpeed(flags);
    }
}