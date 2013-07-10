/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 21, 2004
 */
package widgets;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class ChooserListEditor extends JPanel
{
	private JTextField	text;
	private JPanel		buttonPanel;
	private JButton		add, del, edit;
	public ChooserListEditor()
	{
		super();
		this.setLayout(new GridLayout(2, 1));
		text = new JTextField();
		buttonPanel = new JPanel();
		add = new JButton("Add");
		del = new JButton("Del");
		edit = new JButton("Edit");
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(add);
		buttonPanel.add(del);
		buttonPanel.add(edit);
		this.add(text);
		this.add(buttonPanel);
	}
	public void setName(String name)
	{
		add.setName(name + "add");
		del.setName(name + "del");
		edit.setName(name + "edit");
	}
	public void addActionListenerAdd(ActionListener al)
	{
		add.addActionListener(al);
	}
	public void addActionListenerDel(ActionListener al)
	{
		del.addActionListener(al);
	}
	public void addActionListenerEdit(ActionListener al)
	{
		edit.addActionListener(al);
	}
	public String getText()
	{
		return text.getText();
	}
	public void setText(String txt)
	{
		text.setText(txt);
	}
}