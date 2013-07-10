/*
 * Sean T. McBeth
 * sm8236@ship.edu
 * Created on Jul 21, 2004
 */
package widgets;

import java.awt.Container;
import java.io.Serializable;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

/**
 * Taking a basic Swing JList and making it an actual, useable object.
 * 
 * @author capn_midnight <br>
 *         Sean T. McBeth <br>
 *         sm8236@ship.edu
 */
public class ChooserList extends JPanel implements Serializable
{
	private JList				list;
	private DefaultListModel	listModel;
	private JScrollPane			scroller;

	public ChooserList()
	{
		super();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		scroller = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scroller);
	}
	public void addItem(String item)
	{
		listModel.addElement(item);
	}
	public void removeItem(int i)
	{
		if (i >= 0 && i < listModel.size())
			listModel.remove(i);
	}
	public void clear()
	{
		list.clearSelection();
		listModel.clear();
	}
	public void changeItem(int i, String str)
	{
		listModel.set(i, str);
	}
	public Object getSelectedItem()
	{
		int i = getSelectedIndex();
		if (i >= 0)
			return listModel.get(i);
		return null;
	}
	public String getSelectedText()
	{
		int i = getSelectedIndex();
		if (i >= 0)
			return listModel.get(i).toString();
		return null;
	}
	public int getSelectedIndex()
	{
		return list.getSelectedIndex();
	}
	public void setSelectedIndex(int i)
	{
		if (i >= 0 && i < listModel.size())
			list.setSelectedIndex(i);
	}
	public void addListSelectionListener(ListSelectionListener lsl)
	{
		list.addListSelectionListener(lsl);
	}
	public Container getScroller()
	{
		return scroller;
	}
	public void sort()
	{
		Object[] arr = listModel.toArray();
		Arrays.sort(arr);
		listModel.removeAllElements();
		for (int i = 0; i < arr.length; i++)
		{
			listModel.add(i, arr[i]);
		}
	}
	public int getListSize()
	{
		return listModel.size();
	}
	public String getItem(int i)
	{
		if (i >= 0 && i < listModel.getSize())
			return (String) listModel.get(i);
		return null;
	}
}