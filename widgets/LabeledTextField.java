/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 21, 2004
 */
package widgets;

import java.awt.BorderLayout;
import java.awt.event.KeyListener;

import javax.swing.*;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class LabeledTextField extends JPanel
{
	private JTextField			text;
	private JLabel				label;
	public static final String	NORTH	= BorderLayout.NORTH;
	public static final String	EAST	= BorderLayout.EAST;
	public static final String	SOUTH	= BorderLayout.SOUTH;
	public static final String	WEST	= BorderLayout.WEST;
	public LabeledTextField(String labelText, String orientation)
	{
		super();
		this.setLayout(new BorderLayout());
		text = new JTextField();
		label = new JLabel(labelText);
		this.add(text, BorderLayout.CENTER);
		this.add(label, orientation);
	}
	public String getText()
	{
		return text.getText();
	}
	public void setText(String str)
	{
		text.setText(str);
	}
	public void addKeyListener(KeyListener kl){
		text.addKeyListener(kl);
	}

}