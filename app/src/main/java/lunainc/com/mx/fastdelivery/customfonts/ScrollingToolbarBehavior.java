package lunainc.com.mx.fastdelivery.customfonts;

import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.NotNull;


class ScrollingToolbarBehavior extends CoordinatorLayout.Behavior<Toolbar> {

    public ScrollingToolbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NotNull CoordinatorLayout parent, @NotNull Toolbar child, @NotNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NotNull CoordinatorLayout parent, @NotNull Toolbar child, @NotNull View dependency) {
        if (dependency instanceof AppBarLayout) {

            int distanceToScroll = child.getHeight();

            int bottomToolbarHeight = child.getHeight();//TODO replace this with bottom toolbar height.

            float ratio = dependency.getY() / (float) bottomToolbarHeight;
            child.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}
