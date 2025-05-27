package org.solstice.euclidsElements.splashText.api;

import com.google.common.collect.Iterables;
import org.solstice.euclidsElements.splashText.api.loader.SplashTextLoader;
import org.solstice.euclidsElements.splashText.api.manager.SplashTextManager;
import org.solstice.euclidsElements.splashText.api.type.SimpleSplashText;
import org.solstice.euclidsElements.splashText.api.type.SplashText;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.client.session.Session;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EuclidsSplashTextResourceSupplier extends SplashTextResourceSupplier {

    public EuclidsSplashTextResourceSupplier(Session session) {
        super(session);
    }

    public SplashText getRandomSplash() {
        List<SplashText> splashes = SplashTextManager.INSTANCE.getSplashTexts();
        if (splashes.isEmpty()) return new SimpleSplashText("");

        int poolWeight = SplashTextManager.INSTANCE.getMaxWeight();
        int weightMul = (splashes.size() / 100) + 1;
        Collections.shuffle(splashes);
        Iterator<SplashText> iterator = Iterables.cycle(splashes).iterator();

        while (true) {
            SplashText splash = iterator.next();
            poolWeight -= splash.getWeight() * weightMul;
            if (poolWeight > 0) continue;
            return splash;
        }
    }

    @Override
    public SplashTextRenderer get() {
        return getRandomSplash().getRenderer();
    }

}
