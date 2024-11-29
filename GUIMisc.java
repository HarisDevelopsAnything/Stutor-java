package stutor;

import java.awt.Adjustable;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
public class GUIMisc {
	public static void scrollToBottom(JScrollPane scrollPane) {
	    JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
	    AdjustmentListener downScroller = new AdjustmentListener() {
	        @Override
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	            Adjustable adjustable = e.getAdjustable();
	            adjustable.setValue(adjustable.getMaximum());
	            verticalBar.removeAdjustmentListener(this);
	        }
	    };
	    verticalBar.addAdjustmentListener(downScroller);
	}
	//to res imageicons
	public static ImageIcon imageResize(ImageIcon src, int width, int height) {
		ImageIcon out= new ImageIcon(src.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		return out;
	}
}
