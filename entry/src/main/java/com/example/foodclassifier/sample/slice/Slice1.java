package com.example.foodclassifier.sample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import com.example.foodclassifier.sample.ResourceTable;

/**
 * Slice1.
 */
public class Slice1 extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        Image img = new Image(this);
        img.setPixelMap(ResourceTable.Media_omellete);
        img.setHeight(800);
        img.setWidth(1500);
        DirectionalLayout layout = new DirectionalLayout(getContext());
        layout.addComponent(img);
        img.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new FoodClassifierSlice(), new Intent());
            }
        });

        Image img1 = new Image(this);
        img1.setPixelMap(ResourceTable.Media_idli_img);
        img1.setHeight(500);
        img1.setWidth(1500);
        layout.addComponent(img1);
        img1.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new FoodClassifierSlice1(), new Intent());
            }
        });

        Image img2 = new Image(this);
        img2.setPixelMap(ResourceTable.Media_sandwich_img);
        img2.setHeight(800);
        img2.setWidth(1500);
        layout.addComponent(img2);
        img2.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new FoodClassifierSlice2(), new Intent());
            }
        });

        Text text = new Text(getContext());
        text.setText("Click on the image you want to Classify");
        text.setTextSize(80);
        text.setId(20);
        text.setHeight(1000);
        text.setTextColor(Color.WHITE);
        ShapeElement background = new ShapeElement();
        background.setRgbColor(new RgbColor(0, 0, 255));
        text.setBackground(background);

        DirectionalLayout.LayoutConfig layoutConfig = new DirectionalLayout.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_CONTENT,
                ComponentContainer.LayoutConfig.MATCH_CONTENT);
        layoutConfig.alignment = LayoutAlignment.HORIZONTAL_CENTER;
        text.setLayoutConfig(layoutConfig);

        layout.addComponent(text);
        super.setUIContent(layout);

    }
}
