package stutor;

import java.awt.*;
import javax.swing.*;

public class VerticalFlowLayout implements LayoutManager {
    private int verticalGap;

    public VerticalFlowLayout(int verticalGap) {
        this.verticalGap = verticalGap;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {}

    @Override
    public void removeLayoutComponent(Component comp) {}

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = verticalGap; // Initial gap before the first component
        for (Component comp : parent.getComponents()) {
            Dimension d = comp.getPreferredSize();
            width = Math.max(width, d.width);
            height += d.height + verticalGap;
        }
        Insets insets = parent.getInsets();
        return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int y = insets.top + verticalGap; // Start position with initial gap
        int parentWidth = parent.getWidth();

        for (Component comp : parent.getComponents()) {
            Dimension d = comp.getPreferredSize();
            int x;

            // Check alignment (left or right)
            if (((JPanel) comp).getComponentCount() > 0) {
                Component bubbleContent = ((JPanel) comp).getComponent(0);
                if (bubbleContent instanceof JPanel) {
                    if (((JPanel) bubbleContent).getComponentOrientation().isLeftToRight()) {
                        x = insets.left; // Align left
                    } else {
                        x = parentWidth - d.width - insets.right; // Align right
                    }
                } else {
                    x = insets.left; // Fallback to left
                }
            } else {
                x = insets.left; // Default to left
            }

            comp.setBounds(x, y, d.width, d.height);
            y += d.height + verticalGap; // Add gap after each component
        }
    }
}
