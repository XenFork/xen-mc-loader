package union.xenfork.loader;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper;

public class LaunchWrapperMixinService extends MixinServiceLaunchWrapper {
    @Override
    public MixinEnvironment.CompatibilityLevel getMaxCompatibilityLevel() {
        return MixinEnvironment.CompatibilityLevel.JAVA_17;
    }
}
