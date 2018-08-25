package framework;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import objects.Card;
import objects.CardType;
import hub.Hub;

public class CustomTable extends JTable{
	private static HashMap<String,Card> cardlist = Hub.getCardlist();

	public CustomTable(DefaultTableModel model,ArrayList<JDialog> dialogsToDispose) {
		super(model);
		defineColumnSize();
		setCellEditorForThirdColumn();
		centerDataInAllCells();
		makeSureScrollableContainsAllElements();
		addListenerToRevealCardsOnDoubleClick(dialogsToDispose);
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	/**
	 * Defining the default size of columns, which helps them having different sizes according to the content they wrap.
	 */
	private void defineColumnSize() {
		getColumnModel().getColumn(0).setMinWidth(150);
		getColumnModel().getColumn(0).setMaxWidth(150);
		getColumnModel().getColumn(1).setMinWidth(70);
		getColumnModel().getColumn(1).setMaxWidth(70);
		getColumnModel().getColumn(2).setMinWidth(200);
		getColumnModel().getColumn(2).setMaxWidth(200);
		getColumnModel().getColumn(3).setMinWidth(50);
		getColumnModel().getColumn(3).setMaxWidth(50);
	}
	
	/**
	 * Making the last column be editable through a JComboBox
	 */
	private void setCellEditorForThirdColumn() {
		getColumnModel().getColumn(3).setCellEditor(new CustomCellEditor(this));
	}

	/**
	 * Make all Cells center their data.
	 */
	private void centerDataInAllCells() {
		((DefaultTableCellRenderer)getDefaultRenderer(String.class)).setHorizontalAlignment(SwingConstants.CENTER);
	}


	/**
	 * Make sure the scrollable size will contain all objects by setting a preferred size.
	 */
	private void makeSureScrollableContainsAllElements() {
		setRowHeight(20);
		setFillsViewportHeight(true);
		getTableHeader().setReorderingAllowed(false);
		setPreferredScrollableViewportSize(new Dimension (470,500));
	}

	/**
	 * Add double-click action to reveal the card relative to the card name clicked.
	 * All the dialogs open belong to an ArrayList of Dialogs that will be disposed once the table is closed.
	 */
	private void addListenerToRevealCardsOnDoubleClick(ArrayList<JDialog> dialogsToDispose) {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					if(column == 0) {
						String cardname = (String)getValueAt(row, column);
						JDialog disposableImage = new JDialog();
						ImageIcon cardDisplay = new ImageIcon(Hub.getCardlist().get(cardname).getPathToImage());
						disposableImage.add(new JLabel(cardDisplay));
						disposableImage.pack();
						disposableImage.setResizable(false);
						disposableImage.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						disposableImage.setLocationRelativeTo(CollectionManager.getInstance());
						int x = CollectionManager.getInstance().getLocation().x-disposableImage.getWidth();
						int y = CollectionManager.getInstance().getLocation().y;
						disposableImage.setLocation(x, y);
						disposableImage.setVisible(true);
						dialogsToDispose.add(disposableImage);
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
	}

	/**
	 * Guarantees that the second column will be read as ImageIcon instead of String.
	 */
	@Override
	public Class getColumnClass(int column) {
		if(column==1) {
			return Icon.class;
		}
		return Object.class;
	}

	/**
	 * Only the last column is editable, with the CustomCellEditor (JComboBox).
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		if(column==3) return true;
		return false;
	}




	private class CustomCellEditor extends AbstractCellEditor implements TableCellEditor{
		private DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>();
		private JComboBox ncopies = new JComboBox(model);
		private CustomTable collection;

		public CustomCellEditor(CustomTable TABLE){
			collection=TABLE;
		}

		@Override
		public Object getCellEditorValue() {
			int copiesValue = (int) ncopies.getSelectedItem();
			return copiesValue;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if(cardlist.containsKey(collection.getValueAt(row,0))) {
				model.removeAllElements();
				if(cardlist.get(collection.getValueAt(row,0)).getType().equals(CardType.UNIQUELEGENDARY)) {
					model.addElement(0);
					model.addElement(1);
				}else {
					model.addElement(0);
					model.addElement(1);
					model.addElement(2);
					model.addElement(3);
				}
			}
			return ncopies;
		}
	}
}
