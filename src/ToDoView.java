

	import java.awt.*;
	import java.awt.event.*;
	import java.util.*;
	import javax.swing.*;
	import javax.swing.event.ListSelectionEvent;
	import javax.swing.event.ListSelectionListener;
	//import java.beans.PropertyChangeEvent;
	//import java.beans.PropertyChangeListener;
	import java.lang.Math;
	import java.io.*;


	/*

	 * Author: Jenny Astrachan

	 *Created on: 4 May 2012

	 */



	public class ToDoView extends JFrame implements ActionListener, ListSelectionListener{

		public static final long serialVersionUID = 42L;
		
		private Container contentPane;

		private String[] actionCommands = { "New Item", "Up", "Down", "Remove", "Edit", "Quit" };
		private String[] listNames = {"School Work", "Other Work", "Other", "Tests"};

		// create map for task title to array list of elements
		private Map taskList;
		
		//for new item
		private JComboBox combo;
		private JTextField taskTitle;
		private ButtonGroup group;
		private JDialog popup;
		
		private JMenuBar menuBar;
		
		private JList schoolList, otherTaskList, testList, otherList;
		
		public ToDoView()
		{
			setupFrame();
			fileInit();
		}
		private void fileInit()
		{
			try{ 
				FileReader init = new FileReader("init.txt");
				BufferedReader br = new BufferedReader(init); 
				String s; 
				while((s = br.readLine()) != null) { 
					parseItem(s); 
				} 
				init.close(); 
			} catch (Exception e){
				System.out.println("Couldn't open init file...");
			}
			updateLists();
		}
		private void parseItem(String s) 
		{
			ToDoItem item;
			//TODO: get actual day of repeat
			// format of line: Task (str), List name (str), Repeat (str), Visible (bool), optional date if weekly (int)
			
			String task = s.substring(0, s.indexOf(','));
			s = s.substring(s.indexOf(',')+1);
			String key = s.substring(0, s.indexOf(','));	
			s = s.substring(s.indexOf(',')+1);
			String repeat = s.substring(0, s.indexOf(','));
			s = s.substring(s.indexOf(',')+1);
			
			item = new ToDoItem(task, repeat);
			
			boolean visible;
			if (s.indexOf(',') > -1) {
				s = s.substring(0, s.indexOf(','));
				visible = (new Boolean(s)).booleanValue();
				s = s.substring(s.indexOf(',')+1);
				ArrayList dateList = new ArrayList();
				dateList.add((new Integer(s)));
				item.setRepeat(dateList);
			} else {
				visible = (new Boolean(s)).booleanValue();
			}
			//System.out.println(task + "-" + repeat + "-" + visible);
			item.setVisible(visible);
			//System.out.println(item.getRepeat()+"-");
			ArrayList list = (ArrayList)(taskList.get(key));
			list.add(item);
			taskList.put(key, list);
			
		}
		private void setupFrame() 
		{
			//TODO: background color
			this.setTitle("To Do List");
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			contentPane = getContentPane();
			//create menu bar
			menuBar = new JMenuBar();
			JMenu menu = new JMenu("File");
			menuBar.add(menu);
			int[] events = {KeyEvent.VK_N, KeyEvent.VK_U, KeyEvent.VK_D, KeyEvent.VK_R, KeyEvent.VK_E, KeyEvent.VK_Q };
			for (int i = 0; i < actionCommands.length; i++) 
			{
				JMenuItem item = new JMenuItem(actionCommands[i]);
				item.setAccelerator(KeyStroke.getKeyStroke(events[i], ActionEvent.CTRL_MASK));
				item.addActionListener(this);
				menu.add(item);
			}
			
			this.rootPane.setJMenuBar(menuBar);
			//createNorthPanel();
			createCenterPanel();
			createSouthPanel();
			this.pack();
			this.setVisible(true);
			taskList = new HashMap();
			for (int i = 0; i < listNames.length; i++){
				taskList.put(listNames[i], new ArrayList());
			}
			/*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			    public void run() {
			        // what you want to do
			    	exit();
			    }
			}));*/
		}

		private void createCenterPanel() 
		{
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(2,2));
			centerPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	 
			
			// Create school work panel and variables
			JPanel northWestPanel = new JPanel();
			northWestPanel.setLayout(new BoxLayout(northWestPanel, BoxLayout.Y_AXIS));
			northWestPanel.setBorder(BorderFactory.createTitledBorder("School Work"));
			
			schoolList = new JList();
			schoolList.addListSelectionListener(this);
			//list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			JScrollPane schoolPane = new JScrollPane(schoolList);

			northWestPanel.add(schoolPane);
			northWestPanel.add(Box.createRigidArea(new Dimension(0,10)));
			
			// Create tests panel and variables
			testList = new JList();
			testList.addListSelectionListener(this);
			JScrollPane testPane = new JScrollPane(testList);
			
			JPanel southWestPanel = new JPanel();
			southWestPanel.setLayout(new BoxLayout(southWestPanel, BoxLayout.Y_AXIS));
			southWestPanel.setBorder(BorderFactory.createTitledBorder("Tests"));
			
			southWestPanel.add(testPane);
			southWestPanel.add(Box.createRigidArea(new Dimension(0,10)));
			
			
			// Create other tasks panel and variables
			JPanel northEastPanel = new JPanel();
			northEastPanel.setLayout(new BoxLayout(northEastPanel, BoxLayout.Y_AXIS));
			northEastPanel.setBorder(BorderFactory.createTitledBorder("Other Tasks"));
			
			otherTaskList = new JList();
			otherTaskList.addListSelectionListener(this);
			JScrollPane otherTaskPane = new JScrollPane(otherTaskList);
			
			northEastPanel.add(otherTaskPane);
			northEastPanel.add(Box.createRigidArea(new Dimension(0,10)));
			
			// Create other panel and variables
			otherList = new JList();
			otherList.addListSelectionListener(this);
			JScrollPane otherPane = new JScrollPane(otherList);
			
			JPanel southEastPanel = new JPanel();
			southEastPanel.setLayout(new BoxLayout(southEastPanel, BoxLayout.Y_AXIS));
			southEastPanel.setBorder(BorderFactory.createTitledBorder("Other"));
			
			southEastPanel.add(otherPane);
			southEastPanel.add(Box.createRigidArea(new Dimension(0,10)));
			

			centerPanel.add(northWestPanel);
			centerPanel.add(northEastPanel);
			centerPanel.add(southWestPanel);
			centerPanel.add(southEastPanel);
			
			contentPane.add(centerPanel, BorderLayout.CENTER);
		}



		private void createSouthPanel() 
		{
			// Create action buttons
			JPanel southPanel = new JPanel();
			southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
			southPanel.setBorder(BorderFactory.createEmptyBorder(5,10,10,10));
			
			southPanel.add(Box.createHorizontalGlue());
			for (int i = 0; i < actionCommands.length; i++) 
			{
				JButton temp = new JButton(actionCommands[i]);
				temp.setActionCommand(actionCommands[i]);
				temp.addActionListener(this);
				southPanel.add(temp);
				if (i < actionCommands.length - 1) 
				{
					southPanel.add(Box.createRigidArea(new Dimension(10, 0)));
				}
			}
			southPanel.add(Box.createHorizontalGlue());

			contentPane.add(southPanel, BorderLayout.SOUTH);
		}

		public void actionPerformed(ActionEvent arg0) 
		{
			Object source = arg0.getActionCommand();
			if (source.equals("Quit"))
			{
				int value = JOptionPane.showConfirmDialog (null, "Would You Like to Save First?","Warning",JOptionPane.YES_NO_CANCEL_OPTION);
				if	(value == 0) {
					//Save data
					generateCsvFile("init.txt");
					System.exit(0);
					System.exit( 0 ) ;
				} else if (value == 1) {
					// Exit without saving
					System.exit( 0 ) ;
				}
				// If cancel, do nothing
			}
			else if (source.equals("New Item"))
			{
				popup(new ToDoItem("", "Never"), "School Work", true);
	    	}
			else if(source.equals("Up"))
			{
	    		//change order
				Map selection = getSelection();
				if (selection.isEmpty()) {return;}
				ArrayList list = (ArrayList)(selection.get("list"));
				int index = ((Integer)(selection.get("index"))).intValue();
				String key = (String)(selection.get("key"));
				ToDoItem item;
				
				item = (ToDoItem)(list.remove(index));
				list.add(Math.max(0,index-1), item); // make sure index isn't out of bounds
				taskList.put(key, list); // replace list with newly ordered items
				updateLists();
				
				
	    	}
			else if(source.equals("Down"))
			{
	    		//change order
				Map selection = getSelection();
				if (selection.isEmpty()) {return;}
				ArrayList list = (ArrayList)(selection.get("list"));
				int index = ((Integer)(selection.get("index"))).intValue();
				String key = (String)(selection.get("key"));
				ToDoItem item;
				
				item = (ToDoItem)(list.remove(index));
				list.add(Math.min(list.size(),index+1), item); // make sure index isn't out of bounds
				taskList.put(key, list); // replace list with newly ordered items
				updateLists();
	    	}
			else if (source.equals("Edit"))
			{
				Map selection = getSelection();
				if (selection.isEmpty()) {return;}
				ArrayList list = (ArrayList)(selection.get("list"));
				int index = ((Integer)(selection.get("index"))).intValue();
				String key = (String)(selection.get("key"));
				ToDoItem item;
				
				item = (ToDoItem)(list.get(index));
				popup(item, key, false);
				
			}
			else if(source.equals("Remove"))
			{
				Object[] options = {"Remove", "Keep Repeat", "Cancel"};
				int value = JOptionPane.showOptionDialog(null, "Do you want to delete this item permanently?", "Input", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);
				//System.out.println(value);
				if (value == 0){
					//remove item					
					Map selection = getSelection();
					if (selection.isEmpty()) {return;}
					ArrayList list = (ArrayList)(selection.get("list"));
					int index = ((Integer)(selection.get("index"))).intValue();
					String key = (String)(selection.get("key"));
					ToDoItem item;
					
					item = (ToDoItem)(list.get(index));
					list.remove(item);
					taskList.put(key, list);
					updateLists();
					
				} else if (value == 1){
					//keep for repeat
					
					Map selection = getSelection();
					if (selection.isEmpty()) {return;}
					ArrayList list = (ArrayList)(selection.get("list"));
					int index = ((Integer)(selection.get("index"))).intValue();
					ToDoItem item;
					
					item = (ToDoItem)(list.get(index));
					item.setVisible(!(item.getVisible()));
					updateLists();
					System.out.println(item.getVisible());
					
				}
	    	}
		}


		public void updateLists()
		{
			//check whether visible
			JList[] lists = {schoolList, otherTaskList, otherList, testList};
			for (int i = 0; i < taskList.size(); i++)
			{
				JList jlist = lists[i];
				ArrayList list = (ArrayList)(taskList.get(listNames[i]));
				for (int j = 0; j < list.size(); j++)
				{
					if (!((ToDoItem)(list.get(j))).getVisible()) {
						list.remove(j);
						j--;
					}
				}
				jlist.setListData(list.toArray());
			}
		}
		private Map getSelection()
		{
			Map result = new HashMap();
			ArrayList list;
			int index;
			String key;
			if (schoolList.getSelectedIndex() > -1){					
				list = (ArrayList)(taskList.get("School Work"));
				index = schoolList.getSelectedIndex();
				key = "School Work";
			} else if (otherTaskList.getSelectedIndex() > -1){	
				list = ((ArrayList)(taskList.get("Other Work")));
				index = otherTaskList.getSelectedIndex();	
				key = "Other Work";
			} else if (otherList.getSelectedIndex() > -1){
				list = (ArrayList)(taskList.get("Other"));
				index = otherList.getSelectedIndex();
				key = "Other";
			} else if (testList.getSelectedIndex() > -1){
				list = (ArrayList)(taskList.get("Tests"));
				index = testList.getSelectedIndex();
				key = "Tests";
			} else {return result;}
			result.put("key", key);
			result.put("list", list);
			result.put("index", new Integer(index));
			
			return result;
			
		}
		public void valueChanged(ListSelectionEvent arg0) 
		{
			Object source = (arg0.getSource());
			if (source.equals(schoolList)){
				otherTaskList.clearSelection();
				otherList.clearSelection();
				testList.clearSelection();
				
			}else if (source.equals(otherTaskList)){
				schoolList.clearSelection();
				otherList.clearSelection();
				testList.clearSelection();
			} else if (source.equals(otherList)){
				otherTaskList.clearSelection();
				schoolList.clearSelection();
				testList.clearSelection();
			} else if (source.equals(testList)){
				otherTaskList.clearSelection();
				otherList.clearSelection();
				schoolList.clearSelection();
			}
		}
		public void popup(ToDoItem item, String list, final boolean add){
			String task = item.toString();
			popup = new JDialog();
			Container dialogPane = popup.getContentPane();
			JPanel center = new JPanel();
			center.setBorder(BorderFactory.createEmptyBorder(5,10,10,10));
			center.setLayout(new GridLayout(6,1));
			popup.setTitle("Add New Item");
			String[] comboList = {"School Work", "Other Work","Tests", "Other"};
			combo = new JComboBox(comboList);
			combo.setSelectedItem(list);
			center.add(new JLabel("Select List"));
			center.add(combo);
			center.add(new JLabel("Task Title"));
			taskTitle = new JTextField(task);
			center.add(taskTitle);
			center.add(new JLabel("Repeat"));
			JPanel radioPanel = new JPanel();
			group = new ButtonGroup();
			String[] radioList = {"Never", "Daily", "Weekly"};
			for (int i = 0; i < radioList.length; i++)
			{
				JRadioButton button = new JRadioButton(radioList[i]);
				button.setActionCommand(radioList[i]);
				group.add(button);
				radioPanel.add(button);
				if (radioList[i].equals(item.getRepeat())) {
					button.setSelected(true);
				}
			}
			center.add(radioPanel);
			JPanel buttonPanel = new JPanel();
			JButton addButton = new JButton("Save");
			JButton cancelButton = new JButton("Cancel");
			addButton.setActionCommand("Save");
			cancelButton.setActionCommand("Cancel");
			addButton.addActionListener(new ActionListener() {  
	            public void actionPerformed(ActionEvent e)
	            {
	            	String repeat = group.getSelection().getActionCommand();
					String text = taskTitle.getText();
					if (!(text.equals(""))){
						String listTitle = combo.getSelectedItem().toString();
						ToDoItem newItem = new ToDoItem(text, repeat);
						ArrayList itemList = new ArrayList();
						itemList.add(newItem);
						if (taskList.containsKey(listTitle)){
							itemList.addAll(((ArrayList)(taskList.get(listTitle))));
						}
						if (add) // new item
							taskList.put(listTitle, itemList);
						else {
							Map selection = getSelection();
							if (selection.isEmpty()) {return;}
							ArrayList list = (ArrayList)(selection.get("list"));
							int index = ((Integer)(selection.get("index"))).intValue();
							String key = (String)(selection.get("key"));
							ToDoItem item = new ToDoItem(text, repeat);
							if (listTitle.equals(key)){
								list.set(index, item);
								taskList.put(key, list);
							}
							else { //switching to another list
								list.remove(index); //remove from old list
								taskList.put(listTitle, itemList);
								taskList.put(key, list); // updated old and new list
							}
						}
						updateLists();
					}				
					popup.setVisible(false);
	            	
	            }});
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					popup.setVisible(false);
				}});
			buttonPanel.add(addButton);
			buttonPanel.add(cancelButton);
			center.add(buttonPanel);
			
			dialogPane.add(center);
			popup.pack();
			popup.setContentPane(dialogPane);
			
			popup.setVisible(true);
		}
		private void generateCsvFile(String sFileName)
		{
			try{
			    FileWriter writer = new FileWriter(sFileName);
			    
			    for (int i = 0; i < listNames.length; i++) {
			    	ArrayList list = (ArrayList)(taskList.get(listNames[i]));
			    	for (int j = 0; j < list.size(); j++) {
			    		ToDoItem item = (ToDoItem)(list.get(j));
			    		writer.append(item.toString());
			    		writer.append(',');
			    		writer.append(listNames[i]);
			    		writer.append(',');
			    		writer.append(item.getRepeat());
			    		writer.append(',');
			    		writer.append(""+ item.getVisible());
			    		if (item.getRepeat().equals("Weekly")) {
			    			writer.append(',');
			    			writer.append(item.getDateList().get(0).toString());
			    		}
			    		writer.append('\n');
			    	}
			    }
		
			    writer.flush();
			    writer.close();
			} catch(IOException e){
				System.out.println("File write failed...\n");
			    e.printStackTrace();
			} 
		}
		public void timeUpdate()
		{
			// Called every day to check whether items should become visible
			// TODO: update items at timeout
			System.out.println("Update method called");
			for (int i = 0; i < listNames.length; i++) {
				ArrayList list = (ArrayList)(taskList.get(listNames[i]));
				for (int j = 0; j < list.size(); j++) {
					ToDoItem item = (ToDoItem)(list.get(j));
					if (!item.getVisible()) {
						ArrayList dateList = item.getDateList();
						for (int k = 0; k < dateList.size(); k++) {							
							if (((Integer)(dateList.get(k))).intValue() == Calendar.getInstance().DAY_OF_WEEK) {
								item.setVisible(true);
							}
						}
					}
				}
			}
			updateLists();
		}
		
	}
