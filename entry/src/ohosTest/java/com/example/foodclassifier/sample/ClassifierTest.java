package com.example.foodclassifier.sample;

import org.junit.Test;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.app.Context;
import com.example.foodclassifier.classifier.Classifier;

import static org.junit.Assert.assertEquals;

public class ClassifierTest {
	
    private static String imgPath = "entry/resources/rawfile/image_3_thumb.jpg";
    private static String imgName = "image_3_thumb.jpg";

    @Test
    public void test() {

	Context mContext = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();

	Classifier mtestclassifier = new Classifier(imgPath, imgName, mContext.getResourceManager(), mContext.getCacheDir());
    	
        String output = mtestclassifier.getOutput();
    	
        assertEquals("Ramen", output);
    }
}
