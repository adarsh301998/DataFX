package org.datafx.controller.flow.container;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.FlowContainer;

import java.util.List;
import java.util.function.Function;

/**
 * A {@link FlowContainer} that supports animation for the view change.
 */
public class AnimatedFlowContainer implements FlowContainer<StackPane> {

    private StackPane root;
    private Duration duration;
    private Function<AnimatedFlowContainer, List<KeyFrame>> animationProducer;
    private Timeline animation;
    private ImageView placeholder;

    /**
     * Defaults constructor that creates a container with a fade animation that last 320 ms.
     */
    public AnimatedFlowContainer() {
        this(Duration.millis(320));
    }

    /**
     *  Creates a container with a fade animation and the given duration
     *
     * @param duration the duration of the animation
     */
    public AnimatedFlowContainer(Duration duration) {
        this(duration, ContainerAnimations.FADE);
    }

    /**
     *  Creates a container with the given animation type and  duration
     *
     * @param duration the duration of the animation
     * @param animation the animation type
     */
    public AnimatedFlowContainer(Duration duration, ContainerAnimations animation) {
        this(duration, animation.getAnimationProducer());
    }

    /**
     *    Creates a container with the given animation type and duration
     * @param duration  the duration of the animation
     * @param animationProducer   the {@link KeyFrame} instances that define the animation
     */
    public AnimatedFlowContainer(Duration duration, Function<AnimatedFlowContainer, List<KeyFrame>> animationProducer) {
        this.root = new StackPane();
        this.duration = duration;
        this.animationProducer = animationProducer;
        placeholder = new ImageView();
        placeholder.setPreserveRatio(true);
        placeholder.setSmooth(true);
    }

    @Override
    public <U> void setViewContext(ViewContext<U> context) {
        updatePlaceholder(context.getRootNode());

        if (animation != null) {
            animation.stop();
        }

        animation = new Timeline();
        animation.getKeyFrames().addAll(animationProducer.apply(this));
        animation.getKeyFrames().add(new KeyFrame(duration, (e) -> clearPlaceholder()));

        animation.play();
    }

    /**
     * Returns the {@link ImageView} instance that is used as a placeholder for the old view in each navigation animation.
     * @return
     */
    public ImageView getPlaceholder() {
        return placeholder;
    }

    /**
     * Returns the duration for the animation
     * @return the duration for the animation
     */
    public Duration getDuration() {
        return duration;
    }

    @Override
    public StackPane getView() {
        return root;
    }

    private void clearPlaceholder() {
        placeholder.setImage(null);
        placeholder.setVisible(false);
    }

    private void updatePlaceholder(Node newView) {
        if (root.getWidth() > 0 && root.getHeight() > 0) {
            Image placeholderImage = root.snapshot(null, new WritableImage((int) root.getWidth(), (int) root.getHeight()));
            placeholder.setImage(placeholderImage);
            placeholder.setFitWidth(placeholderImage.getWidth());
            placeholder.setFitHeight(placeholderImage.getHeight());
        } else {
            placeholder.setImage(null);
        }
        placeholder.setVisible(true);
        placeholder.setOpacity(1.0);
        root.getChildren().setAll(placeholder);
        root.getChildren().add(newView);
        placeholder.toFront();

    }
}
