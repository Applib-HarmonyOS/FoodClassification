package com.example.foodclassifier.sample;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import com.example.foodclassifier.sample.slice.MainClassifierSlice;

/**
 * MainAbility.
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainClassifierSlice.class.getName());
    }
}
