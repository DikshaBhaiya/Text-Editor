//Text Editor
//Name: Diksha Bhaiya
//Roll Number: 2010110231
// Graded Lab Assignment

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.event.*;

public class TextEditor extends JFrame {

    String[] fonts = {"Arial", "Calibri", "Cambria", "Courier New", "Comic Sans MS", "Dialog", "Georgia", "Helevetica", "Lucida Sans", "Monospaced","SignPainter", "Tahoma", "Times New Roman", "Verdana"};
    String[] fontSizes = {"2","4","6","8","10","12","16","18","20","22","24","26","28","30","32","34","36","38","40","42","44","46","48","50","52","54","56","58","60"};
    Font newFont = new Font("Arial",Font.PLAIN,20);

    int previousSize = 20;
    String previousFont = "Arial";

    String fn="",dir="",fileName="New Document";

    Font startingFont = new Font(previousFont,Font.PLAIN,previousSize);

    static int findNextPos = 0;
    static int oldFindNextPos = 0;
    static int numberOfWordsFound = 0;
    int _count=0;

    int currentFileSaved = 0;


    String copiedText = " ";
    StyledDocument document;
    Element element;
    AttributeSet attribute;

    String previousSearchedText = "";

    File openedFile;
    RTFEditorKit editorKit;
    JFileChooser fileChooser = new JFileChooser();

    Highlighter.HighlightPainter myHighlightPainter = new MyHighlighterPainter(Color.yellow);
    Highlighter.HighlightPainter removeTheHighlight = new MyHighlighterPainter(Color.white);


    int removeHighlights = 0;

    //initializing all the panels to create the base containers for the components
     JPanel container_panel = new JPanel();
     JPanel main_panel = new JPanel();
     JPanel word_panel = new JPanel();
     JPanel left_panel = new JPanel();
     JPanel format_panel = new JPanel();
     JPanel search_panel = new JPanel();
     JPanel text_panel = new JPanel();
     JPanel right_panel = new JPanel();
     JPanel shapes_panel = new JPanel();
     DrawingPanel drawingpanel = new DrawingPanel();
    JTextPane text_Area = new JTextPane();

     JMenuBar jmb = new JMenuBar(); //creating the menu bar with File, Edit, Review and Help
     JMenu file = new JMenu("File");
     JMenu edit = new JMenu("Edit");
     JMenu review = new JMenu("Review");
     JMenu help = new JMenu("Help");
     JMenu _case = new JMenu("Case");

    JComboBox FontsBox = new JComboBox(); //Box to display available fonts
    JComboBox fontSizeBox = new JComboBox(); //Box to display available font sizes

    JLabel wordcnt = new JLabel("Word Count: ");
    JLabel word_Count = new JLabel("");
    JLabel charcnt = new JLabel("Character Count: ");
    JLabel characterCount = new JLabel("");
    JLabel informationDisplay = new JLabel("");

    //Creating the components that go inside each menu 
     JMenuItem _new = new JMenuItem("New");
     JMenuItem open = new JMenuItem("Open");
     JMenuItem save = new JMenuItem("Save");
     JMenuItem saveas = new JMenuItem("Save as...");
     JMenuItem exit = new JMenuItem("Exit");
     JMenuItem cut = new JMenuItem("Cut");
     JMenuItem copy = new JMenuItem("Copy");
     JMenuItem paste = new JMenuItem("Paste");
     JMenuItem uppercase = new JMenuItem("UPPERCASE");
     JMenuItem lowercase = new JMenuItem("lowercase");
     JMenuItem aboutus = new JMenuItem("About Us");
     
     //Components to enter text to find and replace with
    JTextField findText = new JTextField();
    JTextField replaceWithText = new JTextField();
    JLabel jLabel1 = new JLabel("Find");
    JLabel jLabel2 = new JLabel("Replace");
    JLabel foundInPlaces = new JLabel("");
    
    //Buttons to press to find all, find next, replace all and replace just the highlighted part
    JButton jbt_Replace = new JButton("Replace");
    JButton jbt_ReplaceAll = new JButton("Replace All");
    JButton jbt_FindNext = new JButton("Find Next");
    JButton jbt_FindAll = new JButton("Find All");
    //Buttons for the different shapes in the SketchPad 
     JButton jbt_Rect = new JButton("Rectangle");
     JButton jbt_Oval = new JButton("Oval");
     JButton jbt_line = new JButton("Line");
     JButton jbt_Triangle = new JButton("Triangle");
     JButton jbt_Pentagon = new JButton("Pentagon");
     JButton jbt_CLEAR = new JButton("CLEAR");


    TextEditor() {
        text_Area.setFont(newFont);

        FontsBox.removeAllItems();


        for(String s :fonts){
            FontsBox.addItem(s);
        }

        for(String s:fontSizes){
            fontSizeBox.addItem(s);
        }

        this.setTitle(fileName + ".txt");

        //Adding dimensions to the four menu's display at the top right corner
        file.setPreferredSize(new Dimension(50,25));
        edit.setPreferredSize(new Dimension(50,25));
        review.setPreferredSize(new Dimension(70,25));
        help.setPreferredSize(new Dimension(50,25));

        //adding the menu's to the menu bar
        jmb.add(file);
        jmb.add(edit);
        jmb.add(review);
        jmb.add(help);

        //Setting the shortcut keys for each menu item
        _new.setMnemonic(KeyEvent.VK_N);
        open.setMnemonic(KeyEvent.VK_O);
        save.setMnemonic(KeyEvent.VK_S);
        saveas.setMnemonic(KeyEvent.VK_A);
        exit.setMnemonic(KeyEvent.VK_X);
        copy.setMnemonic(KeyEvent.VK_C);
        cut.setMnemonic(KeyEvent.VK_T);
        paste.setMnemonic(KeyEvent.VK_P);
        _case.setMnemonic(KeyEvent.VK_C);
        aboutus.setMnemonic(KeyEvent.VK_B);


        _new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        //handling the events for each menu item

        cut.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cutEditActionPerformed(e);
            }
        }));

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyEditActionPerformed(e);
            }
        });

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasteActionPerformed(e);
            }
        });

        _new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFileActionPerformed(e);
            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    openFileActionPerformed(e);
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileActionPerformed(e);
            }
        });

        saveas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAsActionPerformed(e);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        FontsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FontsBoxActionPerformed(e);
            }
        });

        fontSizeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fontSizeBoxActionPerformed(e);
            }
        });

        uppercase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toUpperActionPerformed(e);
            }
        });

        lowercase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toLowerActionPerformed(e);
            }
        });



        jbt_FindAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbt_FindAllActionPerformed(e);
            }
        });

        jbt_ReplaceAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbt_ReplaceAllActionPerformed(e);
            }
        });

        jbt_FindNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbt_FindNextActionPerformed(e);
            }
        });

        jbt_Replace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbt_ReplaceActionPerformed(e);
            }
        });

        //Handling key typed, pressed and released events for the notepad area
        text_Area.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                word_Count();
            }
        });

        //Handling all the mouse events for the notepad area
        text_Area.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                word_Count();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });


        //Adding motion listeners for functions like word count of the highlighted part
        text_Area.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                word_Count();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                
            }
        });

        //adding the new, open, save, save as and exit options to the file menu
        file.add(_new);
        file.addSeparator();
        file.add(open);
        file.addSeparator();
        file.add(save);
        file.addSeparator();
        file.add(saveas);
        file.addSeparator();
        file.add(exit);
        //adding the cut,copy and paste options to the edit menu
        edit.add(cut);
        edit.addSeparator();
        edit.add(copy);
        edit.addSeparator();
        edit.add(paste);
        //adding uppercase and lowercase options to the case submenu
        _case.add(uppercase);
        _case.addSeparator();
        _case.add(lowercase);
        //adding case to the review menu
        review.add(_case);
        //adding about us option to the help menu
        help.addSeparator();
        help.add(aboutus);

        //Creating buttons for Bold, Italics, Underline and Strikethrough and assigning icons to them
        ImageIcon bold = new ImageIcon(new ImageIcon("Boldicon.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        JToggleButton jbt_Bold = new JToggleButton(bold);
        ImageIcon italics = new ImageIcon(new ImageIcon("italic-text.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        JToggleButton jbt_Italics = new JToggleButton(italics);
        ImageIcon underline = new ImageIcon(new ImageIcon("underline.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        JToggleButton jbt_Underline = new JToggleButton(underline);
        ImageIcon strikethrough = new ImageIcon(new ImageIcon("strikethrough.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
        JToggleButton jbt_Strikethrough = new JToggleButton(strikethrough);

        //Setting the layout and adding the buttons to the panel
        format_panel.setLayout(new BoxLayout(format_panel,BoxLayout.LINE_AXIS));
        format_panel.add(jbt_Bold);
        format_panel.add(jbt_Italics);
        format_panel.add(jbt_Underline);
        format_panel.add(jbt_Strikethrough);
        format_panel.add(Box.createRigidArea(new Dimension(5,0)));
        format_panel.add(FontsBox);
        format_panel.add(fontSizeBox);

        //Handling the events/ actions for these buttons
        jbt_Bold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeTextBold(e);
            }
        });

        jbt_Italics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeTextItalics(e);
            }
        });

        jbt_Underline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeTextUnderlined(e);
            }
        });

        jbt_Strikethrough.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strikeThroughActionPerformed(e);
            }
        });

        aboutus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutUsActionPerformed(e);
            }
        });
        //Creating the layout for the base panels
        main_panel.setLayout(new BoxLayout(main_panel,BoxLayout.LINE_AXIS));
        left_panel.setLayout(new BoxLayout(left_panel,BoxLayout.PAGE_AXIS));
        right_panel.setLayout(new BoxLayout(right_panel,BoxLayout.PAGE_AXIS));
        container_panel.setLayout(new BoxLayout(container_panel,BoxLayout.PAGE_AXIS));
        //setting the alightment for the panels
        format_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        search_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        text_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        left_panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel findReplaceTextEntryPanel = new JPanel();
        JPanel findReplaceButtonsPanel = new JPanel();
        //setting the size for the JTextFields 
        findText.setPreferredSize(new Dimension(300,15));
        replaceWithText.setPreferredSize(new Dimension(300,15));
        search_panel.setLayout(new BoxLayout(search_panel,BoxLayout.PAGE_AXIS));
        findReplaceTextEntryPanel.setLayout(new BoxLayout(findReplaceTextEntryPanel, BoxLayout.PAGE_AXIS));
        //Adding the Find, Replace labels and boxes to enter text onto the panel
        findReplaceTextEntryPanel.add(jLabel1);
        findReplaceTextEntryPanel.add(findText);
        findReplaceTextEntryPanel.add(jLabel2);
        findReplaceTextEntryPanel.add(replaceWithText);
        search_panel.add(findReplaceTextEntryPanel); //adding the panel for the find,replace buttons
        //adding the buttons
        findReplaceButtonsPanel.setLayout(new BoxLayout(findReplaceButtonsPanel, BoxLayout.LINE_AXIS));
        findReplaceButtonsPanel.add(jbt_FindAll);
        findReplaceButtonsPanel.add(Box.createRigidArea(new Dimension(10,0)));
        findReplaceButtonsPanel.add(jbt_FindNext);
        findReplaceButtonsPanel.add(Box.createRigidArea(new Dimension(10,0)));
        findReplaceButtonsPanel.add(jbt_Replace);
        findReplaceButtonsPanel.add(Box.createRigidArea(new Dimension(10,0)));
        findReplaceButtonsPanel.add(jbt_ReplaceAll);
        //adding to the base panel
        search_panel.add(findReplaceButtonsPanel);
        search_panel.add(foundInPlaces);
        //adding the text area 
        text_Area.setPreferredSize(new Dimension(500,300));
        text_panel.setLayout(new BoxLayout(text_panel,BoxLayout.LINE_AXIS));
        text_panel.add(text_Area);
        //adding the lower panel for word count and character count
        word_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        word_panel.add(wordcnt);
        word_panel.add(word_Count);
        word_panel.add(charcnt);
        word_panel.add(characterCount);
        word_panel.add(informationDisplay);
        word_panel.setBackground(Color.LIGHT_GRAY);
        //adding all the panels to a base panel to create the left side of the text editor
        left_panel.add(Box.createRigidArea(new Dimension(0,10)));
        left_panel.add(format_panel);
        left_panel.add(Box.createRigidArea(new Dimension(0,10)));
        left_panel.add(search_panel);
        left_panel.add(Box.createRigidArea(new Dimension(0,10)));
        left_panel.add(text_panel);
        left_panel.setBorder(BorderFactory.createLineBorder(Color.black));
        //creating the panel to add the buttons of the shapes
        shapes_panel.setLayout(new BoxLayout(shapes_panel,BoxLayout.LINE_AXIS));
        shapes_panel.add(jbt_Rect);

        //adding the event handlers for each shape button and the clear button
        jbt_Rect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingpanel.rectangle();
            }
        });

        shapes_panel.add(jbt_Oval);
        jbt_Oval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingpanel.oval();
            }
        });

        shapes_panel.add(jbt_line);
        jbt_line.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingpanel.line();
            }
        });

        shapes_panel.add(jbt_Triangle);
        jbt_Triangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingpanel.triangle();
            }
        });

        shapes_panel.add(jbt_Pentagon);
        jbt_Pentagon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingpanel.pentagon();
            }
        });

        shapes_panel.add(Box.createRigidArea(new Dimension(5,0)));

        shapes_panel.add(jbt_CLEAR);
        jbt_CLEAR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingpanel.clear();
            }
        });

        //Creating a drawing area
        drawingpanel.setSize(200,300);
        drawingpanel.setBackground(Color.LIGHT_GRAY);
        
        //Creating the SketchPad on the right side
        right_panel.add(Box.createRigidArea(new Dimension(0,10)));
        right_panel.add(new JLabel("SketchPad"));
        right_panel.add(Box.createRigidArea(new Dimension(0,10)));
        right_panel.add(shapes_panel);
        right_panel.add(Box.createRigidArea(new Dimension(0,10)));
        right_panel.add(drawingpanel);
        right_panel.setBorder(BorderFactory.createLineBorder(Color.black));

        // Adding the Text Editor and SketchPad to create the base layout
        main_panel.add(left_panel);
        main_panel.add(Box.createRigidArea(new Dimension(10,0)));
        main_panel.add(right_panel);
        //adding the menubar to the frame
        this.add(jmb,BorderLayout.NORTH);
        //adding the overall editor and using BoxLayout and then adding the word count panel
        container_panel.add(main_panel);
        container_panel.add(Box.createVerticalGlue());
        container_panel.add(word_panel);
        //adding this entire panel setup to the frame
        this.add(container_panel);
    }

    //function to save a file
     void saveFileActionPerformed(java.awt.event.ActionEvent evt) {

        int flag = fileChooser.showSaveDialog(null); //shows the dialog box to save a file
        if (flag == JFileChooser.APPROVE_OPTION) {
            File saveFiles = fileChooser.getSelectedFile(); //getting the chosen file
            StyledDocument documents = text_Area.getStyledDocument(); //Getting the text typed on the editor
            RTFEditorKit editorKits = new RTFEditorKit();
            //saving the file
            BufferedOutputStream outputStream;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(saveFiles));
                editorKits.write(outputStream, documents, documents.getStartPosition().getOffset(), documents.getLength());
                outputStream.flush();
                outputStream.close();
            } catch (BadLocationException e1) {
                e1.getCause();
            } catch (IOException e1) {
                e1.getSuppressed();
            }

            fileName = fileChooser.getName(saveFiles); //Getting the chosen file name
            this.setTitle(fileName + ".rtf"); //setting the entered file name for the file
            currentFileSaved=1; //updating the variable

        }


    }

    //function to open a file
     void openFileActionPerformed(java.awt.event.ActionEvent evt) {

        JFileChooser fileChooser = new JFileChooser();
        text_Area.setText(" ");
        fileChooser.showOpenDialog(null); //opens dialog box to choose a file
        openedFile = fileChooser.getSelectedFile(); //saves the file selected
        RTFEditorKit editorKit = new RTFEditorKit();
        //opens the contents of the file
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(openedFile);
            editorKit.read(inputStream,text_Area.getStyledDocument(), 0);
            inputStream.close();
        } catch (BadLocationException e1) {
            e1.getCause();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }
    // function to handle new file
     void newFileActionPerformed(java.awt.event.ActionEvent evt) {
        //if file currently used is not saved, it performs the save function
        if(currentFileSaved==0){
            this.saveFileActionPerformed(evt);

        }else{
            //Removes everything from the file to make it look new
            this.setTitle("New Document");
            text_Area.setText(" ");
            text_Area.setFont(newFont);
            currentFileSaved=0;
        }

    }
    //Function to handle Save As
     void saveAsActionPerformed(java.awt.event.ActionEvent evt) {


        int flag = fileChooser.showSaveDialog(null); // opens the dialog box to save the file and is similar to save
        if (flag == JFileChooser.APPROVE_OPTION) {

            File saveFiles = fileChooser.getSelectedFile();
            StyledDocument documents = text_Area.getStyledDocument();
            RTFEditorKit editorKits = new RTFEditorKit();
            BufferedOutputStream outputStream;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(saveFiles));
                editorKits.write(outputStream, documents, documents.getStartPosition().getOffset(), documents.getLength());
                outputStream.flush();
                outputStream.close();
            } catch (BadLocationException e1) {
                e1.getCause();
            } catch (IOException e1) {
                e1.getSuppressed();
            }

            fileName = fileChooser.getName(saveFiles);
            this.setTitle(fileName + ".txt");
            currentFileSaved=1;

        }

    }

    //Function to handle the Cut option
     void cutEditActionPerformed(java.awt.event.ActionEvent evt) {


        document = (StyledDocument) text_Area.getDocument(); //gets the text typed in the editor
        int selectionStart = text_Area.getSelectionStart(); //returns the selection areas start index

        element = document.getCharacterElement(selectionStart); //gets the element that represents this start index in the document 
        attribute = element.getAttributes();

        copiedText = text_Area.getSelectedText(); //saves the String of characters that is currently selected 

        try {
            document.remove(selectionStart, copiedText.length()); // removes this text
        } catch (BadLocationException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    //Function to handle the Copy option
     void copyEditActionPerformed(java.awt.event.ActionEvent evt) {

        document = (StyledDocument) text_Area.getDocument();//gets the text typed in the editor
        int selectionStart = text_Area.getSelectionStart();//returns the selection areas start index

        element = document.getCharacterElement(selectionStart); //gets the element that represents this start index in the document 
        attribute = element.getAttributes(); 

        copiedText = text_Area.getSelectedText(); //saves the String of characters that is currently selected 

    }

    //Function to handle the Paste option
     void pasteActionPerformed(java.awt.event.ActionEvent evt) {

        try {
            document.insertString(text_Area.getCaretPosition(), " " + copiedText + " ", attribute); //adds the copied text at the current position
        } catch (BadLocationException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    //function to make text Bold
     void makeTextBold(java.awt.event.ActionEvent evt) {

        StyledDocument doc = (StyledDocument) text_Area.getDocument();//gets the text typed in the editor
        int selectionEnd = text_Area.getSelectionEnd();//returns the selection areas end index
        int selectionStart = text_Area.getSelectionStart();//returns the selection areas start index

        Element element = doc.getCharacterElement(selectionStart); //gets the element that represents this start index in the document 
        AttributeSet as =  element.getAttributes();

        if(selectionEnd == selectionStart){ //returns if nothing is selected

            return;
        }

        MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes()); //Makes the attributes changeable
        StyleConstants.setBold(asNew, !StyleConstants.isBold(as)); //Makes the attributes bold
        doc.setCharacterAttributes(selectionStart, text_Area.getSelectedText()
                .length(), asNew, true); //replaces the attributes on the text area

    }

    //function to make text Italics
     void makeTextItalics(java.awt.event.ActionEvent evt) {

        StyledDocument doc = (StyledDocument) text_Area.getDocument();//gets the text typed in the editor
        int selectionEnd = text_Area.getSelectionEnd();//returns the selection areas end index
        int selectionStart = text_Area.getSelectionStart();//returns the selection areas start index

        Element element = doc.getCharacterElement(selectionStart);//gets the element that represents this start index in the document 
        AttributeSet as =  element.getAttributes();

        if(selectionEnd == selectionStart){//returns if nothing is selected

            return;
        }

        MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());//Makes the attributes changeable
        StyleConstants.setItalic(asNew, !StyleConstants.isItalic(as));//Makes the attributes italics
        doc.setCharacterAttributes(selectionStart, text_Area.getSelectedText()
                .length(), asNew, true);//replaces the attributes on the text area


    }

    //function to make text Underlined
     void makeTextUnderlined(java.awt.event.ActionEvent evt) {

        StyledDocument doc = (StyledDocument) text_Area.getDocument();//gets the text typed in the editor
        int selectionEnd = text_Area.getSelectionEnd();//returns the selection areas end index
        int selectionStart = text_Area.getSelectionStart();//returns the selection areas start index

        Element element = doc.getCharacterElement(selectionStart);
        AttributeSet as =  element.getAttributes();

        if(selectionEnd == selectionStart){//returns if nothing is selected

            return;
        }

        MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());//Makes the attributes changeable
        StyleConstants.setUnderline(asNew, !StyleConstants.isUnderline(as));//Makes the attributes underlined
        doc.setCharacterAttributes(selectionStart, text_Area.getSelectedText()
                .length(), asNew, true);//replaces the attributes on the text area

    }
    //function to make text strikenthrough
     void strikeThroughActionPerformed(java.awt.event.ActionEvent evt) {

        StyledDocument doc = (StyledDocument) text_Area.getDocument();//gets the text typed in the editor
        int selectionEnd = text_Area.getSelectionEnd();//returns the selection areas end index
        int selectionStart = text_Area.getSelectionStart();//returns the selection areas start index

        Element element = doc.getCharacterElement(selectionStart);
        AttributeSet as =  element.getAttributes();

        if(selectionEnd == selectionStart){//returns if nothing is selected

            return;
        }

        MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());//Makes the attributes changeable
        StyleConstants.setStrikeThrough(asNew, !StyleConstants.isStrikeThrough(as));//Makes the attributes strikenthrough
        doc.setCharacterAttributes(selectionStart, text_Area.getSelectedText()
                .length(), asNew, true);//replaces the attributes on the text area

    }

    //function to cound words and characters
    public void word_Count(){

        String s;
        int counter=0;

        if(text_Area.getSelectedText()==null){ //if nothing is selected, it gives the word count of the entire document otherwise only the selected text
            s = text_Area.getText();
        }else{
            s = text_Area.getSelectedText();
            counter=1;
        }
        int words = 0;
        int characters = 0;

        int flag=0;
        if(s.isEmpty()){ //if nothing is selected or typed then it displays 0
            words=0;
            characters=0;
        }else{

            String[] array = s.split(" "); //splits the text into words

            for(int i=0;i<array.length;i++){

                String[] newLine = array[i].split("\n"); //splits based on newline
                if(newLine.length != 1){
                    flag=1;
                    for(String line:newLine){ //counts the words
                        if(!line.equals("")){
                            words++; 
                        }
                        characters = characters + line.length(); // counts the characters
                    }

                }

                if(flag==0){
                    words++;
                    characters = characters + array[i].length();
                }

                flag=0;


            }


        }

        characterCount.setText(String.valueOf(characters)); //sets the character count
        word_Count.setText(String.valueOf(words)); //sets the word count
        if(counter==1)
            informationDisplay.setText("WORD COUNT OF SELECTED TEXT ONLY");
        else
            informationDisplay.setText("WORD COUNT OF ENTIRE DOCUMENT");

    }

    //Function for selecting Font
     void FontsBoxActionPerformed(java.awt.event.ActionEvent evt) {


        String selectedFont = (String)FontsBox.getSelectedItem(); //gets the selected font
        Font f = new Font(selectedFont,Font.PLAIN,previousSize); //initially defines as the base font chosen

        if(text_Area.getSelectedText()==null){ //if no text is selected, changes the font of the entire document
            text_Area.setFont(f);
        }else{

            StyledDocument doc = (StyledDocument) text_Area.getDocument(); //gets the text typed in the editor
            int selectionStart = text_Area.getSelectionStart(); //gets the start index of the selected area

            Element elements = doc.getCharacterElement(selectionStart); 
            AttributeSet as =  elements.getAttributes();

            MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes()); //makes attributes changeable
            StyleConstants.setFontFamily(asNew, selectedFont); //sets the attributes to the selected font
            doc.setCharacterAttributes(selectionStart, text_Area.getSelectedText()
                    .length(), asNew, true); //replaces it on the editor


        }

        previousFont = selectedFont;
    }
    //Function for selecting Font Size
     void fontSizeBoxActionPerformed(java.awt.event.ActionEvent evt) {

        String s = fontSizeBox.getSelectedItem().toString(); //gets the selected size
        int selectedSize = Integer.valueOf(s);

        StyledDocument doc = (StyledDocument) text_Area.getDocument(); //gets the text typed in the editor
        int selectionEnd = text_Area.getSelectionEnd();//gets the end index of the selected area
        int selectionStart = text_Area.getSelectionStart(); //gets the start index of the selected area


        Element element = doc.getCharacterElement(selectionStart);
        AttributeSet as =  element.getAttributes();

        if(selectionEnd == selectionStart){ //if nothing is selected, leaves it as the original
            Font newFonts = new Font(previousFont,Font.PLAIN,selectedSize);
            text_Area.setFont(newFonts);
            return;
        }

        MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
        StyleConstants.setFontSize(asNew, selectedSize);//sets the attributes to the selected font size
        doc.setCharacterAttributes(selectionStart, text_Area.getSelectedText()
                .length(), asNew, true); //replaces it on the editor

        previousSize = selectedSize; //updates the font variable

    }


    //Function to highlight
    public void highlight(JTextComponent textComponent, String s, Highlighter.HighlightPainter colourChoice){

        try{

            Highlighter h = textComponent.getHighlighter();
            Document doc = textComponent.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;

            while((pos = text.toUpperCase().indexOf(s.toUpperCase(),pos))>=0){ //adds highlight to selected text

                h.addHighlight(pos, pos+s.length(), colourChoice); 
                pos += s.length();
            }


        }catch(BadLocationException e){

            System.out.println(e.getMessage());

        }

    }
    //Highlighted the area of the next found value
    public static void findNextHighlight(JTextComponent textComponent, String s, Highlighter.HighlightPainter colourChoice){


        try{

            Highlighter h = textComponent.getHighlighter();
            Document doc = textComponent.getDocument();
            String text = doc.getText(0, doc.getLength());

            if((findNextPos = text.toUpperCase().indexOf(s.toUpperCase(),findNextPos))>=0){
                h.addHighlight(findNextPos, findNextPos+s.length(), colourChoice);
                findNextPos += s.length();
            }


        }catch(BadLocationException e){

            System.out.println(e.getMessage());

        }

    }
    //removing the highlighter
    public void removeHighlighter(JTextComponent textField, int remove){


        Highlighter highlight = text_Area.getHighlighter();
        Highlighter.Highlight[] highlights = highlight.getHighlights();

        if(remove==1){
            for (int i = 0; i < highlights.length; i++) {
                if (highlights[i].getPainter() instanceof MyHighlighterPainter) {
                    highlight.removeHighlight(highlights[i]);
                }
            }
        }
    }

    //function to handle Find All
         void jbt_FindAllActionPerformed(java.awt.event.ActionEvent evt) {

            String s = text_Area.getText(); //getting the text from the editor
            if(_count>0){
                _count=0;
                removeHighlighter(text_Area,1);
            }

            if(s.isEmpty()){ //if no word is typed to be found, dialog box appears
                JOptionPane.showMessageDialog(null, "Please enter the word first");
            }else{
                String[] array = s.split(" "); //splitting into words
                for(String arrayOne:array){
                    if(arrayOne.contains("\n")){
                        String[] newLineArray = arrayOne.split("\n");//if there is a newline,splitting based on newline
                        for(String newLineSearch: newLineArray){
                            if(newLineSearch.contains(findText.getText())){
                                _count++; //counting number of times the text appeared
                            }
                        }

                    }else{

                        if(arrayOne.contains(findText.getText())){
                            _count++;
                        }

                    }


                }

            }

            foundInPlaces.setText("Found in " + _count + " places"); //displaying number of times its present 

            highlight(text_Area,findText.getText(),myHighlightPainter); //highlighting the area

        }

        //Function to handle replace all
        void jbt_ReplaceAllActionPerformed(java.awt.event.ActionEvent evt){
            removeHighlighter(text_Area,1);
            StyledDocument doc = (StyledDocument) text_Area.getDocument();


            int pos=0;
            try {

                while((pos = text_Area.getText().toUpperCase().indexOf(findText.getText().toUpperCase(), pos))>=0){ //while the text to be replaced is present in the document


                    Element element = doc.getCharacterElement(pos);
                    AttributeSet as =  element.getAttributes();
                    doc.remove(pos, findText.getText().length()); //removes the text
                    doc.insertString(pos, replaceWithText.getText(),as); //adds the text to be replaced at that position

                    pos+= replaceWithText.getText().length(); //finds the text to be replaced in the area after replaced text

                }
            } catch (BadLocationException ex) {
                Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Function to handle Find Next
         void jbt_FindNextActionPerformed(java.awt.event.ActionEvent evt) {

            if (findText.getText().equals(previousSearchedText)) { //if it is still searching for the same text
                previousSearchedText = findText.getText();
                findNextPos = 0;
            }

            if (findNextPos == 0) {
                findNextHighlight(text_Area, findText.getText(), myHighlightPainter); //highlighting the text
            } else {

                try {


                    Document doc = text_Area.getDocument();
                    String text = doc.getText(0, doc.getLength());


                    removeHighlighter(text_Area, 1); //removes highlighter from current find
                    findNextHighlight(text_Area, findText.getText(), myHighlightPainter); //highlights next instance


                    oldFindNextPos = findNextPos; //updates the position 

                } catch (BadLocationException e) {

                    System.out.println(e.getMessage());

                }


            }
        }

        //Function to handle Replace
        void jbt_ReplaceActionPerformed(java.awt.event.ActionEvent evt) {

            StyledDocument doc = (StyledDocument) text_Area.getDocument();


            int pos = findNextPos - findText.getText().length(); 

            try {


                Element element = doc.getCharacterElement(pos);
                AttributeSet as =  element.getAttributes();
                doc.remove(pos, findText.getText().length()); //removing the text
                doc.insertString(pos, replaceWithText.getText(), as); //inserting the text

            } catch (BadLocationException ex) {
                Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

        //Function to handle About Us
     void aboutUsActionPerformed(java.awt.event.ActionEvent evt) {

        JOptionPane.showMessageDialog(null, "Created by Diksha Bhaiya for CSD213- Graded Lab Assignment");

    }

    //Function to handle Lowercase
     void toLowerActionPerformed(java.awt.event.ActionEvent evt) {


        String s = text_Area.getSelectedText().toUpperCase();  //changes selected text to uppercase

        StyledDocument doc = (StyledDocument) text_Area.getStyledDocument(); //getting the text from the editor
        int selectionEnd = text_Area.getSelectionEnd(); //gets the end index of the selected area
        int selectionStart = text_Area.getSelectionStart();//gets the start index of the selected area

        Element elements = doc.getCharacterElement(selectionStart);
        AttributeSet as =  elements.getAttributes();

        if(selectionEnd == selectionStart){ //if nothing is selected, it is returned
            return;
        }

        try {
            doc.remove(selectionStart, s.length()); //removes the selected text
            doc.insertString(selectionStart, s.toLowerCase(), as); //inserts the lowercase text
        } catch (BadLocationException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

     void toUpperActionPerformed(java.awt.event.ActionEvent evt) {

        String s = text_Area.getSelectedText().toUpperCase();//changes selected text to uppercase

        StyledDocument doc = (StyledDocument) text_Area.getStyledDocument();//getting the text from the editor
        int selectionStart = text_Area.getSelectionStart();//gets the start index of the selected area

        Element elements = doc.getCharacterElement(selectionStart);
        AttributeSet as =  elements.getAttributes();

        try {
            doc.remove(selectionStart, s.length());//removes the selected text
            doc.insertString(selectionStart, s.toUpperCase(), as); //inserts the uppercase text
        } catch (BadLocationException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }


    }


            public static class DrawingPanel extends JPanel
    {
        final int X=5;      
         boolean rect=false, oval=false, line=false, triangle=false, pentagon=false;  
        int rect_x1,rect_y1,rect_width,rect_height;                                                              
        int line_x1,line_x2,line_y1,line_y2;                                                            
        int triangle_x1, triangle_y1, triangle_x2, triangle_y2, triangle_x3, triangle_y3;                                            
        int pentagon_x1, pentagon_x2, pentagon_x3, pentagon_x4, pentagon_x5, pentagon_y1, pentagon_y2, pentagon_y3, pentagon_y4, pentagon_y5;                     
        //Adding rectangle to sketchPad
        public void rectangle() {
            rect_x1 = 100;
            rect_y1 = 150;
            rect_width = 150;
            rect_height = 100;
            rect = true;
            oval = false;
            line = false;
            triangle = false;
            pentagon = false;
            repaint();
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    dragmouse(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });
        }
        //Adding oval to sketchPad
        public void oval()
        {
            rect_x1=200;
            rect_y1=150;
            rect_width=100;
            rect_height=200;
            rect=false;
            line = false;
            oval=true;
            pentagon=false;
            triangle=false;
            repaint();
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    dragmouse(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });
        }
        //Adding line to sketchPad
        public void line()
        {
            line_x1=100;
            line_y1=200;
            line_x2=200;
            line_y2=150;
            rect=false;
            line = true;
            oval=false;
            pentagon=false;
            triangle=false;
            repaint();
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    dragmouse(e);
                }
                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });
        }
        //Adding triangle to sketchPad
        public void triangle()
        {
            triangle_x1=110;
            triangle_y1=110;
            triangle_x2=100;
            triangle_y2=200;
            triangle_x3=200;
            triangle_y3=190;
            rect=false;
            line = false;
            oval=false;
            pentagon=false;
            triangle=true;
            repaint();
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    dragmouse(e);
                }
                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });
        }
        //Adding pentagon to sketchPad
        public void pentagon()
        {
            pentagon_x1=150;
            pentagon_y1=100;
            pentagon_x2=100;
            pentagon_x3=120;
            pentagon_x4=180;
            pentagon_x5=200;
            pentagon_y2=140;
            pentagon_y5=140;
            pentagon_y3=180;
            pentagon_y4=180;
            rect=false;
            line = false;
            oval=false;
            pentagon=true;
            triangle=false;
            repaint();
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    dragmouse(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });
        }

        //Adding clear to sketchPad
        public void clear()
        {
            rect=false;
            line = false;
            oval=false;
            pentagon=false;
            triangle=false;
            repaint();
        }

        //adding the paint component
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(rect==true) { //if rectangle is needed to be added
                g.fillRect(rect_x1, rect_y1, rect_width, rect_height); 
                g.setColor(Color.white);
                //adding the corner points to change the size and center point to move it around
                g.fillOval(rect_x1 - X, rect_y1 - X, 2*X, 2*X);
                g.fillOval(rect_x1 +rect_width - X, rect_y1 - X, 2*X, 2*X);
                g.fillOval(rect_x1 - X, rect_y1 +rect_height - X, 2*X, 2*X);
                g.fillOval(rect_x1 +rect_width- X, rect_y1 +rect_height- X, 2*X, 2*X);
                g.setColor(Color.white);
                g.fillOval(rect_x1+rect_width/2-X,rect_y1+rect_height/2-X,2*X,2*X);
                
            }
            else if(oval==true)
            {//if oval is needed to be added
                g.fillOval(rect_x1,rect_y1,rect_width,rect_height);
                g.setColor(Color.white);
                //adding the corner points to change the size and center point to move it around
                g.fillOval(rect_x1 - X, rect_y1 - X, 2*X, 2*X);
                g.fillOval(rect_x1 +rect_width - X, rect_y1 - X, 2*X, 2*X);
                g.fillOval(rect_x1 - X, rect_y1 +rect_height - X, 2*X, 2*X);
                g.fillOval(rect_x1 +rect_width- X, rect_y1 +rect_height- X, 2*X, 2*X);
                g.setColor(Color.white);
                g.fillOval(rect_x1+rect_width/2-X,rect_y1+rect_height/2-X,2*X,2*X);
            }

            else if(line==true)
            {//if line is needed to be added
                Graphics2D g2 = (Graphics2D) g;
                ((Graphics2D) g).setStroke(new BasicStroke(X));
                g.drawLine(line_x1,line_y1,line_x2,line_y2);
                g.setColor(Color.white);
                //adding the corner points to change the size and center point to move it around
                g.fillOval(line_x1 - X, line_y1 - X, 2*X, 2*X);
                g.fillOval(line_x2 - X, line_y2 - X, 2*X, 2*X);
                g.setColor(Color.white);
                g.fillOval((line_x1+line_x2)/2-X,(line_y1+line_y2)/2-X,2*X,2*X);
            }

            else if(triangle==true)
            {//if triangle is needed to be added
                int[] xpts = {triangle_x1,triangle_x2,triangle_x3};
                int[] ypts = {triangle_y1,triangle_y2,triangle_y3};
                g.fillPolygon(xpts,ypts,3);
                g.setColor(Color.white);
                //adding the corner points to change the size and center point to move it around
                g.fillOval((triangle_x1+triangle_x2+triangle_x3)/3-X,(triangle_y1+triangle_y2+triangle_y3)/3-X,2*X,2*X);
                g.fillOval(triangle_x1-X, triangle_y1-X,2*X,2*X);
                g.fillOval(triangle_x2-X, triangle_y2-X,2*X,2*X);
                g.fillOval(triangle_x3-X, triangle_y3-X,2*X,2*X);
            }

            else if(pentagon==true)
            {//if pentagon is needed to be added
                int[] xpts = {pentagon_x1,pentagon_x2,pentagon_x3,pentagon_x4,pentagon_x5};
                int[] ypts = {pentagon_y1,pentagon_y2,pentagon_y3,pentagon_y4,pentagon_y5};
                g.fillPolygon(xpts,ypts,5);
                g.setColor(Color.white);
                //adding the corner points to change the size and center point to move it around
                g.fillOval(pentagon_x1-X, pentagon_y1-X,2*X,2*X);
                g.fillOval(pentagon_x2-X, pentagon_y2-X,2*X,2*X);
                g.fillOval(pentagon_x3-X, pentagon_y3-X,2*X,2*X);
                g.fillOval(pentagon_x4-X, pentagon_y4-X,2*X,2*X);
                g.fillOval(pentagon_x5-X, pentagon_y5-X,2*X,2*X);
                g.setColor(Color.white);
                g.fillOval((pentagon_x1+pentagon_x2+pentagon_x3+pentagon_x4+pentagon_x5)/5-X,(pentagon_y1+pentagon_y2+pentagon_y3+pentagon_y4+pentagon_y5)/5-X, 2*X, 2*X);
            }
        }

        public void dragmouse(MouseEvent e)
        { //changing the dimensions
            if(rect==true||oval==true) {
                if (e.getX() <= rect_x1+rect_width/2 + X && e.getX() >= rect_x1+rect_width/2 - X && e.getY() <= rect_y1+rect_height/2 + X && e.getY() >= rect_y1+rect_height/2 - X) {
                    rect_x1 = e.getX() - rect_width / 2;
                    rect_y1 = e.getY() - rect_height / 2;
                    repaint();
                }

                if (e.getX() <= rect_x1 + rect_width + X && e.getX() >= rect_x1 + rect_width - X && e.getY() <= rect_y1 + rect_height + X && e.getY() >= rect_y1 + rect_height - X) {
                    rect_width = e.getX() - rect_x1;
                    rect_height = e.getY() - rect_y1;
                    if (rect_width < 4 * X)
                        rect_width = 4 * X;
                    if (rect_height < 4 * X)
                        rect_height = 4 * X;
                    repaint();
                }

                if (e.getX() <= rect_x1 + X && e.getX() >= rect_x1 - X && e.getY() <= rect_y1 + X && e.getY() >= rect_y1 - X) {
                    final int rx2 = rect_x1 + rect_width;
                    final int ry2 = rect_y1 + rect_height;
                    rect_x1 = e.getX();
                    rect_y1 = e.getY();
                    rect_width = rx2 - rect_x1;
                    rect_height = ry2 - rect_y1;
                    if (rect_width < 4 * X)
                        rect_width = 4 * X;
                    if (rect_height < 4 * X)
                        rect_height = 4 * X;
                    repaint();
                }

                if (e.getX() <= rect_x1 + rect_width + X && e.getX() >= rect_x1 + rect_width - X && e.getY() <= rect_y1 + X && e.getY() >= rect_y1 - X) {
                    final int ry2 = rect_y1 + rect_height;
                    rect_width = e.getX() - rect_x1;
                    rect_y1 = e.getY();
                    rect_height = ry2 - rect_y1;
                    if (rect_width < 4 * X)
                        rect_width = 4 * X;
                    if (rect_height < 4 * X)
                        rect_height = 4 * X;
                    repaint();
                }

                if (e.getX() <= rect_x1 + X && e.getX() >= rect_x1 - X && e.getY() <= rect_y1 + rect_height + X && e.getY() >= rect_y1 + rect_height - X) {
                    final int rx2 = rect_x1 + rect_width;
                    rect_height = e.getY() - rect_y1;
                    rect_x1 = e.getX();
                    rect_width = rx2 - rect_x1;
                    if (rect_width < 4 * X)
                        rect_width = 4 * X;
                    if (rect_height < 4 * X)
                        rect_height = 4 * X;
                    repaint();
                }
            }

            else if(line==true)
            {
        
                if(e.getX()<=(line_x1+line_x2)/2+X&&e.getX()>=(line_x1+line_x2)/2-X&&e.getY()<=(line_y1+line_y2)/2+X&&e.getY()>=(line_y1+line_y2)/2-X)
                {
                    final int x = (line_x1+line_x2)/2;
                    final int y = (line_y1+line_y2)/2;
                    line_x1 += e.getX()-x;
                    line_y1 += e.getY()-y;
                    line_x2 += e.getX()-x;
                    line_y2 += e.getY()-y;
                    repaint();
                }

                else if(e.getX()<=line_x1+X&&e.getX()>=line_x1-X&&e.getY()<=line_y1+X&&e.getY()>=line_y1-X)
                {
                    line_x1 = e.getX();
                    line_y1 = e.getY();
                    repaint();
                }

                else if(e.getX()<=line_x2+X&&e.getX()>=line_x2-X&&e.getY()<=line_y2+X&&e.getY()>=line_y2-X)
                {
                    line_x2 = e.getX();
                    line_y2 = e.getY();
                    repaint();
                }
            }

            else if(triangle==true)
            {
                if(e.getX()<=(triangle_x1+triangle_x2+triangle_x3)/3+X&&e.getX()>=(triangle_x1+triangle_x2+triangle_x3)/3-X&&e.getY()<=(triangle_y1+triangle_y2+triangle_y3)/3+X&&e.getY()>=(triangle_y1+triangle_y2+triangle_y3)/3-X)
                {
                    final int x = (triangle_x1+triangle_x2+triangle_x3)/3;
                    final int y = (triangle_y1+triangle_y2+triangle_y3)/3;
                    triangle_x1 += e.getX()-x;
                    triangle_y1 += e.getY()-y;
                    triangle_x2 += e.getX()-x;
                    triangle_y2 += e.getY()-y;
                    triangle_x3 += e.getX()-x;
                    triangle_y3 += e.getY()-y;
                    repaint();
                }

                else if(e.getX()>=triangle_x1-X&&e.getX()<=triangle_x1+X&&e.getY()>=triangle_y1-X&e.getY()<=triangle_y1+X)
                {
                    triangle_x1=e.getX();
                    triangle_y1=e.getY();
                    repaint();
                }

                else if(e.getX()>=triangle_x2-X&&e.getX()<=triangle_x2+X&&e.getY()>=triangle_y2-X&&e.getY()<=triangle_y2+X)
                {
                    triangle_x2=e.getX();
                    triangle_y2=e.getY();
                    repaint();
                }

                else if(e.getX()>=triangle_x3-X&&e.getX()<=triangle_x3+X&&e.getY()>=triangle_y3-X&&e.getY()<=triangle_y3+X)
                {
                    triangle_x3=e.getX();
                    triangle_y3=e.getY();
                    repaint();
                }
            }

            else if(pentagon==true)
            {
                if(e.getX()<=(pentagon_x1+pentagon_x2+pentagon_x3+pentagon_x4+pentagon_x5)/5+X&&e.getX()>=(pentagon_x1+pentagon_x2+pentagon_x3+pentagon_x4+pentagon_x5)/5-X&&e.getY()>=(pentagon_y1+pentagon_y2+pentagon_y3+pentagon_y4+pentagon_y5)/5-X&&e.getY()<=(pentagon_y1+pentagon_y2+pentagon_y3+pentagon_y4+pentagon_y5)/5+X)
                {
                    final int x = (pentagon_x1+pentagon_x2+pentagon_x3+pentagon_x4+pentagon_x5)/5;
                    final int y = (pentagon_y1+pentagon_y2+pentagon_y3+pentagon_y4+pentagon_y5)/5;
                    pentagon_x1 += e.getX()-x;
                    pentagon_y1 += e.getY()-y;
                    pentagon_x2 += e.getX()-x;
                    pentagon_y2 += e.getY()-y;
                    pentagon_x3 += e.getX()-x;
                    pentagon_y3 += e.getY()-y;
                    pentagon_x4 += e.getX()-x;
                    pentagon_y4 += e.getY()-y;
                    pentagon_x5 += e.getX()-x;
                    pentagon_y5 += e.getY()-y;
                    repaint();
                }

                else if(e.getX()<=pentagon_x1+X&e.getX()>=pentagon_x1-X&&e.getY()>=pentagon_y1-X&&e.getY()<=pentagon_y1+X)
                {
                    pentagon_x1=e.getX();
                    pentagon_y1=e.getY();
                    repaint();
                }


                else if(e.getX()<=pentagon_x2+X&e.getX()>=pentagon_x2-X&&e.getY()>=pentagon_y2-X&&e.getY()<=pentagon_y2+X)
                {
                    pentagon_x2=e.getX();
                    pentagon_y2=e.getY();
                    repaint();
                }

                else if(e.getX()<=pentagon_x3+X&e.getX()>=pentagon_x3-X&&e.getY()>=pentagon_y3-X&&e.getY()<=pentagon_y3+X)
                {
                    pentagon_x3=e.getX();
                    pentagon_y3=e.getY();
                    repaint();
                }

                else if(e.getX()<=pentagon_x4+X&e.getX()>=pentagon_x4-X&&e.getY()>=pentagon_y4-X&&e.getY()<=pentagon_y4+X)
                {
                    pentagon_x4=e.getX();
                    pentagon_y4=e.getY();
                    repaint();
                }

                else if(e.getX()<=pentagon_x5+X&e.getX()>=pentagon_x5-X&&e.getY()>=pentagon_y5-X&&e.getY()<=pentagon_y5+X)
                {
                    pentagon_x5=e.getX();
                    pentagon_y5=e.getY();
                    repaint();
                }
            }
        }
    }
    public static void main(String[] args) { //main function to create the frame and run the program
        JFrame frame = new TextEditor();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(75,75);
        frame.setSize(1250,600);
        frame.setTitle("Text Editor");
    }


}

class MyHighlighterPainter extends DefaultHighlighter.DefaultHighlightPainter {

    public MyHighlighterPainter(Color c) {
        super(c);
    }

}



