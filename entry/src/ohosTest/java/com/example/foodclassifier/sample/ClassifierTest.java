package com.example.foodclassifier.sample;

import org.junit.Test;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.app.Context;
import com.example.foodclassifier.classifier.Classifier;

import static org.junit.Assert.assertEquals;

public class ClassifierTest {
	
	private static String img_path = "entry/resources/rawfile/image_3_thumb.jpg";
	private static String img_name = "image_3_thumb.jpg";
	private Context mContext;
	private Classifier mtestclassifier;
	
    @Test
    public void test() {
    	
    	mContext = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
    	
    	mtestclassifier = new Classifier(img_path, img_name, mContext.getResourceManager(), mContext.getCacheDir());
    	
    	String output = mtestclassifier.get_output();
    	
        assertEquals("Ramen", output);
    }
}