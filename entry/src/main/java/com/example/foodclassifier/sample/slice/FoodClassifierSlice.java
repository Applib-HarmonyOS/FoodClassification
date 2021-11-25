package com.example.foodclassifier.sample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import com.example.foodclassifier.classifier.Classifier;
import com.example.foodclassifier.sample.ResourceTable;

/**
 * FoodClassifierSlice.
 */

public class FoodClassifierSlice extends AbilitySlice {
    private Text resourcesText;
    private static final String MODEL_INPUT_IMAGE_PATH = "entry/resources/base/media/omellete.jpg";
    private static final String MODEL_INPUT_IMAGE_NAME = "omellete.jpg";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_output_display_ability);
        initComponents();
    }

    private void initComponents() {
        resourcesText = (Text) findComponentById(ResourceTable.Id_resources_text);
        foodclassification();
    }

    private void foodclassification() {
        String outputlabel = null;
        Classifier myclassifier = new Classifier(MODEL_INPUT_IMAGE_PATH, MODEL_INPUT_IMAGE_NAME,
                getResourceManager(), getCacheDir());
        outputlabel = myclassifier.getOutput();
        resourcesText.setText(" ");
        resourcesText.setText(
                "Predicted label : " + outputlabel + System.lineSeparator() + " Finish Prediction !!!");
    }
}
