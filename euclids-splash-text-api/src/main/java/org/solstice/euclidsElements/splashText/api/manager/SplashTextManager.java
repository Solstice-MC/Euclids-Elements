package org.solstice.euclidsElements.splashText.api.manager;

import org.solstice.euclidsElements.splashText.api.type.SplashText;

import java.util.ArrayList;
import java.util.List;

public class SplashTextManager {

	public static final SplashTextManager INSTANCE = new SplashTextManager();

	private final List<SplashText> splashTexts = new ArrayList<>();
	private int maxWeight = 0;

	private SplashTextManager() {}

	public void addSplashText(SplashText splash) {
//		if (splash instanceof AdvancedSplashText) this.splashTexts.addFirst(splash);
//		else this.splashTexts.add(splash);
		this.splashTexts.add(splash);
		this.maxWeight += splash.getWeight();
	}

	public List<SplashText> getSplashTexts() {
		return this.splashTexts;
	}

	public int getMaxWeight() {
		return this.maxWeight;
	}

}
