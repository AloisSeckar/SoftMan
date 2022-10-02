/**
 * Copyright (C) 2014 uphy.jp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* source https://gist.github.com/uphy/2678dc1e6b9715c284e5 */

package elrh.softman.utils.ext;


import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * @author Yuhi Ishikura
 */
public final class ProgressIndicatorUtil {

    private ProgressIndicatorUtil() {
    }

    public static ProgressIndicatorAccessor replaceRootWithProgressIndicatorPane(Scene scene) {
        return ProgressIndicatorRoot.get(scene);
    }

    public interface ProgressIndicatorAccessor {
        void show(String message);
        void hide();
    }

    private static final class ProgressIndicatorRoot extends StackPane implements ProgressIndicatorAccessor {

        private static final String PROGRESS_INDICATOR_ROOT_ID = "progressIndicatorRoot";
        private final Label label;
        private final ProgressIndicator progressIndicator;
        private final Parent progressIndicatorPane;
        private final Animation showAnimation;
        private final Animation hideAnimation;

        static ProgressIndicatorRoot get(Scene scene) {
            final Parent root = scene.getRoot();
            if (root instanceof StackPane && PROGRESS_INDICATOR_ROOT_ID.equals(root.getId())) {
                return (ProgressIndicatorRoot) root;
            } else {
                ProgressIndicatorRoot newRoot = new ProgressIndicatorRoot(root);
                scene.setRoot(newRoot);
                return newRoot;
            }
        }

        private ProgressIndicatorRoot(Parent originalRoot) {
            setId(PROGRESS_INDICATOR_ROOT_ID);

            this.progressIndicator = new ProgressIndicator();
            this.progressIndicator.setMaxWidth(100);
            this.progressIndicator.setMaxHeight(100);
            this.label = new Label();
            this.label.setStyle("-fx-text-fill:white");

            final BorderPane pane = new BorderPane();
            pane.setStyle("-fx-background-color:black");
            pane.setCenter(this.progressIndicator);

            final StackPane progressIndicatorPane = new StackPane();
            progressIndicatorPane.setVisible(false);
            StackPane.setAlignment(this.label, Pos.CENTER);

            progressIndicatorPane.getChildren().addAll(pane, this.label);
            getChildren().addAll(originalRoot, progressIndicatorPane);
            this.progressIndicatorPane = progressIndicatorPane;

            final double maxOpacity = 0.5;
            this.showAnimation = createFadeTransition(Duration.seconds(1), pane, 0, maxOpacity);
            this.hideAnimation = createFadeTransition(Duration.seconds(0.3), pane, maxOpacity, 0);
            this.hideAnimation.setOnFinished(e -> this.progressIndicatorPane.setVisible(false));
        }

        private static Animation createFadeTransition(Duration duration, Parent parent, double from, double to) {
            final FadeTransition fade = new FadeTransition(duration, parent);
            fade.setFromValue(from);
            fade.setToValue(to);
            return fade;
        }

        @Override
        public void show(String message) {
            this.label.setText(message);
            this.showAnimation.playFromStart();
            this.progressIndicatorPane.setVisible(true);
            this.progressIndicator.setProgress(0);
            this.progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        }

        @Override
        public void hide() {
            this.hideAnimation.playFromStart();
        }
    }

}
