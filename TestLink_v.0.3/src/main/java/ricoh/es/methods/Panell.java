/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ricoh.es.methods;

/**
 *
 * @author Usuario
 */
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Panell {

    final private static String CAPSALERA = "ATENCIÓ";
    final private static String[] BOTONS = {"Acceptar", "Cancel·lar"};
    final private static String CANCEL = "Cancel·lar";
    final private static String OK = "Acceptar";
    final private static String[] ACCEPTAR = {"Acceptar"};

    public static int botons(String capsalera, Object o, String[] botons, String boto) {
        int n = JOptionPane.showOptionDialog(null, o, capsalera,
                JOptionPane.CANCEL_OPTION, JOptionPane.DEFAULT_OPTION, null, botons,
                boto);
        return n;

    }

    public static int botons(String capsalera, String text, String[] botons, String boto) {
        int n = JOptionPane.showOptionDialog(null, text, capsalera,
                JOptionPane.CANCEL_OPTION, JOptionPane.DEFAULT_OPTION, null, botons,
                boto);
        return n;

    }

    public static void info(String text) {
        JOptionPane.showOptionDialog(null, text, CAPSALERA,
                JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, ACCEPTAR,
                OK);

    }

    public static boolean quest(String text) {
        int n = JOptionPane.showOptionDialog(null, text, CAPSALERA,
                JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, BOTONS,
                CANCEL);
        if (n == 0) {
            return true;
        }
        return false;
    }

    public static boolean warning(String text) {
        int n = JOptionPane.showOptionDialog(null, text, CAPSALERA,
                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
                CANCEL);
        if (n == 0) {
            return true;
        }
        return false;
    }

    public static boolean atencioAcceptar(String text) {
        int n = JOptionPane.showOptionDialog(null, text, CAPSALERA,
                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, ACCEPTAR,
                null);
        if (n == 0) {
            return true;
        }
        return false;
    }

    public static void error(String text) {
        JOptionPane.showOptionDialog(null, text, CAPSALERA,
                JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, ACCEPTAR,
                OK);

    }

    public static String input(Object message, String title) {
        return (String) JOptionPane.showInputDialog(
                null, message, title, JOptionPane.DEFAULT_OPTION);
    }

    public static int inputInt(Object message, String title) {
        try {
            return Integer.parseInt((String) JOptionPane.showInputDialog(
                    null, message, title, JOptionPane.DEFAULT_OPTION));
        } catch (NumberFormatException e) {
            panellAcceptar("El valor introduit no es numèric.\n Es considerarà que s'ha introduit '0'");
        } catch (HeadlessException e) {
            panellAcceptar("El valor introduit no es numèric.\n Es considerarà que s'ha introduit '0'");
        }
        return 0;
    }

    public static void panellAcceptar(String text) {
        String boto = "Acceptar";
        String capsalera = "ATENCIÓ";
        String[] botons = {boto};
        botons(capsalera, text, botons, boto);
    }

    public static File fileChooser(String path, String fileName, String formatsName, String[] formats, String headerButton) {
        File f = new File(path);
        JFileChooser fileopen = new JFileChooser(f);
        if (fileName != null) {
            fileopen.setSelectedFile(new File(path.concat("\\").concat(fileName)));
        }
        if (formats != null) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(formatsName, formats);
            fileopen.addChoosableFileFilter((javax.swing.filechooser.FileFilter) filter);
        }
        new JFileChooser(f);
        new JFileChooser(f);

        int ret = fileopen.showDialog(null, headerButton);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return fileopen.getSelectedFile();
        } else {
            return null;
        }
    }

    public static File[] fileChooser(String header, String headerOneSel, String addButton, String removeButton, String acceptButton,
            String path, String fileName, String formatsName, String[] formats, String headerButton, String updatedFile) {

        ArrayList<File> al = new ArrayList<File>();
        File[] fs = null;
        boolean uploading = false;

        do {
            File f = fileChooser(path, fileName, formatsName, formats, headerButton);

            boolean remove = true;
            if (f != null) {
                if (!al.contains(f)) {
                    al.add(f);
                }
            }

            while (al.size() > 0 && remove) {
                String capsalera = header;
                if (al.size() == 1) {
                    capsalera = headerOneSel;
                }

                String[] botons = new String[al.size() + 2];
                botons[0] = "ADD";
                botons[al.size() + 1] = "OK";
                String text = "";
                for (int i = 0; i < al.size(); i++) {
                    botons[i + 1] = "R" + (i + 1);
                    text += "" + (i + 1) + ". " + al.get(i).getAbsolutePath() + "\n";
                }
                text += "\nADD = " + addButton + ", R# = " + removeButton + ", OK = " + acceptButton;
                int op = botons(capsalera, text, botons, "OK");
                if (op == -1) {
                    remove = false;
                    uploading = false;
                    fs = null;
                } else if (op == 0) {
                    remove = false;
                    uploading = true;
                } else if (op == al.size() + 1) {
                    remove = false;
                    uploading = false;
                    fs = new File[al.size()];
                    for (int i = 0; i < al.size(); i++) {
                        fs[i] = al.get(i);
                    }
                } else {
                    remove = true;
                    al.remove(op - 1);
                }
            }


        } while (uploading);
        return null;

    }

    public static File[] fileChooser(String header, String chooseAgainButton, String acceptButton,
            String path, String fileName,
            String formatsName, String[] formats, String headerButton) {
        File[] fs = null;
        boolean chooseAgain = true;
        do {
            chooseAgain = false;
            File f = fileChooser(path, fileName, formatsName, formats, headerButton);
            if (f != null) {
                ArrayList<javax.swing.JCheckBox> alCB = new ArrayList<javax.swing.JCheckBox>();
                ArrayList<File> alF = new ArrayList<File>();

                javax.swing.JPanel jPanelExternal = new javax.swing.JPanel();
                jPanelExternal.setBackground(new java.awt.Color(255, 255, 255));
                jPanelExternal.setLayout(new java.awt.BorderLayout());

                javax.swing.JLabel jLabel = new javax.swing.JLabel();
                jLabel.setBackground(new java.awt.Color(255, 255, 255));
                jLabel.setText(f.getParent());
                jPanelExternal.add(jLabel, java.awt.BorderLayout.PAGE_START);

                javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane();
                jScrollPane.setPreferredSize(new java.awt.Dimension(800, 350));
                // Code of sub-components and layout - not shown here
                jPanelExternal.add(jScrollPane, java.awt.BorderLayout.PAGE_END);

                javax.swing.JPanel jPanel = new javax.swing.JPanel();
                jPanel.setBackground(new java.awt.Color(255, 255, 255));
                jPanel.setLayout(new java.awt.GridLayout(13, 2));
                // Code of sub-components and layout - not shown here
                jScrollPane.setViewportView(jPanel);

                String[] csv = (new File(f.getParent())).list();
                File[] fcsv = (new File(f.getParent())).listFiles();

                for (int i = 0; i < csv.length; i++) {
                    javax.swing.JCheckBox jCheckBox = new javax.swing.JCheckBox();
                    jCheckBox.setBackground(new java.awt.Color(255, 255, 255));
                    if (fcsv[i].isFile()) {
                        if (0 == f.getName().compareTo(csv[i])) {
                            jCheckBox.setSelected(true);
                        }
                        jCheckBox.setText(csv[i]);
                        jPanel.add(jCheckBox);
                        alCB.add(jCheckBox);
                    }
                }
                String[] botons = {acceptButton, chooseAgainButton};
                int op = Panell.botons(header, jPanelExternal, botons, acceptButton);

                if (op == -1) {
                    fs = null;
                } else if (op == 0) {

                    for (int i = 0; i < alCB.size(); i++) {
                        if (alCB.get(i).isSelected()) {
                            alF.add(new File(f.getParent() + "\\"
                                    + alCB.get(i).getText()));
                        }
                    }
                    fs = new File[alF.size()];
                    for (int i = 0; i < alF.size(); i++) {
                        fs[i] = alF.get(i);
                    }

                } else if (op == 1) {
                    chooseAgain = true;
                }
            }
        } while (chooseAgain);

        return fs;
    }
        @SuppressWarnings({ "rawtypes", "unchecked", "serial" })
		public static String list(String header, String description, final String list[], String accept, String cancel) {
        javax.swing.JPanel jPanel = new javax.swing.JPanel();
        javax.swing.JLabel jLabel = new javax.swing.JLabel();
        javax.swing.JList<?> jList = new javax.swing.JList();
        javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane();
        jPanel.setLayout(new java.awt.BorderLayout());
        jLabel.setText(description);
        jPanel.add(jLabel, java.awt.BorderLayout.PAGE_START);
        jList.setModel(new javax.swing.AbstractListModel() {

            public int getSize() {
                return list.length;
            }

            public Object getElementAt(int i) {
                return list[i];
            }
        });
        jScrollPane.setViewportView(jList);
        jPanel.add(jScrollPane, java.awt.BorderLayout.CENTER);
        String[] butons = {accept, cancel};
        return botons(header, jPanel, butons, butons[0]) == 0 ? (String) jList.getSelectedValue() : null;
    }
    public static String textArea(String header, String description, final String text, int col20, int row5,
            String accept, String cancel) {
        javax.swing.JPanel jPanel = new javax.swing.JPanel();
        javax.swing.JLabel jLabel = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane();
        javax.swing.JTextArea jTextArea = new javax.swing.JTextArea();
        jPanel.setLayout(new java.awt.BorderLayout());
        jLabel.setText(description);
        jPanel.add(jLabel, java.awt.BorderLayout.PAGE_START);
        jTextArea.setColumns(col20);
        jTextArea.setRows(row5);
        jTextArea.setText(text);
        jScrollPane.setViewportView(jTextArea);
        jPanel.add(jScrollPane, java.awt.BorderLayout.CENTER);
        String[] butons = {accept, cancel};
        return botons(header, jPanel, butons, butons[0]) == 0 ? (String) jTextArea.getText() : null;
    }
}
