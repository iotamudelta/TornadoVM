package uk.ac.manchester.tornado.drivers.spirv.graal.compiler;

import org.graalvm.compiler.loop.phases.ReassociateInvariantPhase;
import org.graalvm.compiler.nodes.spi.LoweringTool;
import org.graalvm.compiler.options.OptionValues;
import org.graalvm.compiler.phases.common.*;
import uk.ac.manchester.tornado.drivers.opencl.graal.phases.BoundCheckEliminationPhase;
import uk.ac.manchester.tornado.drivers.opencl.graal.phases.TornadoFloatingReadReplacement;
import uk.ac.manchester.tornado.runtime.graal.compiler.TornadoMidTier;
import uk.ac.manchester.tornado.runtime.graal.phases.ExceptionCheckingElimination;
import uk.ac.manchester.tornado.runtime.graal.phases.TornadoMemoryPhiElimination;

import static org.graalvm.compiler.core.common.GraalOptions.*;
import static org.graalvm.compiler.core.common.GraalOptions.ReassociateInvariants;

public class SPIRVMidTier extends TornadoMidTier {

    public SPIRVMidTier(OptionValues options) {
        appendPhase(new ExceptionCheckingElimination());

        CanonicalizerPhase canonicalizer;
        if (ImmutableCode.getValue(options)) {
            canonicalizer = CanonicalizerPhase.createWithoutReadCanonicalization();
        } else {
            canonicalizer = CanonicalizerPhase.create();
        }

        appendPhase(canonicalizer);

        appendPhase((new BoundCheckEliminationPhase()));
        appendPhase(new ExceptionCheckingElimination());

        if (OptFloatingReads.getValue(options)) {
            appendPhase(new IncrementalCanonicalizerPhase<>(canonicalizer, new TornadoFloatingReadReplacement()));
        }

        appendPhase(new TornadoMemoryPhiElimination());
        appendPhase(new RemoveValueProxyPhase());

        appendPhase(canonicalizer);

        if (ConditionalElimination.getValue(options)) {
            appendPhase(new IterativeConditionalEliminationPhase(canonicalizer, true));
        }

        appendPhase(new GuardLoweringPhase());

        appendPhase(canonicalizer);

        appendPhase(new LoweringPhase(canonicalizer, LoweringTool.StandardLoweringStage.MID_TIER));

        appendPhase(new FrameStateAssignmentPhase());

        if (ReassociateInvariants.getValue(options)) {
            appendPhase(new ReassociateInvariantPhase());
        }

        appendPhase(canonicalizer);
    }
}
