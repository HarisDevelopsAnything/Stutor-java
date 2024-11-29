package stutor;
import javax.swing.*;
import java.awt.*;
public class LoadingWindow extends JFrame{
	LoadingWindow(){
		setLayout(new BorderLayout());
	}
	public static void main(String args[]) {
		JFrame lw= new JFrame("Please wait");
		lw.setBounds(460, 40, 500,500);
		lw.setLayout(new BorderLayout());
		lw.add(new JLabel("Please wait while data is loaded..."), BorderLayout.NORTH);
		JPanel centre= new JPanel();
		ImageIcon load= new ImageIcon("icons/loading.gif");
		JLabel loading= new JLabel(load);
		centre.add(loading);
		lw.add(centre, BorderLayout.CENTER);
		lw.setVisible(true);
	}
}
