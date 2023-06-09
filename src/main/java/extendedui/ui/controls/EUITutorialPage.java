package extendedui.ui.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import extendedui.ui.EUIBase;

public class EUITutorialPage extends EUIBase {
    public String title;
    public String description;

    public EUITutorialPage(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public boolean isHovered() {
        return false;
    }

    @Override
    public void renderImpl(SpriteBatch sb) {

    }

    @Override
    public void updateImpl() {

    }
}
