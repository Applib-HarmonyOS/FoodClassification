package com.example.foodclassifier.sample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import com.example.foodclassifier.sample.ResourceTable;

/**
 * MainClassifierSlice.
 */
public class MainClassifierSlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_button_main_ability);
        Component button = findComponentById(ResourceTable.Id_start_btn_example);
        button.setClickedListener(component -> present(new Slice1(), new Intent()));
    }

}
