package io.datafx.samples.featuretoggle;

import io.datafx.controller.ViewController;
import io.datafx.featuretoggle.DisabledByFeature;
import io.datafx.featuretoggle.HideByFeature;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@ViewController("featureView.fxml")
public class FeatureController {

    @FXML
    @HideByFeature("PLAY_FEATURE")
    private Button playButton;

    @FXML
    @DisabledByFeature("FEATURE2")
    private Button button2;

}
