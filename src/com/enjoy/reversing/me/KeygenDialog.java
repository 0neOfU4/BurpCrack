import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class KeygenDialog {
   private JPanel rootPanel;
   private JTextField nameTextField;
   private JTextArea licenseArea;
   private JTextArea requestArea;
   private JTextArea responseArea;
   private JTextField loaderTextField;
   private JButton runButton;
   private static byte[] encryption_key = "burpr0x!".getBytes();

   private KeygenDialog() {
      this.$$$setupUI$$$();
      this.nameTextField.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent e) {
            if (KeygenDialog.this.nameTextField.getText().length() > 0) {
               KeygenDialog.this.licenseArea.setText(KeygenDialog.this.generateLicense(KeygenDialog.this.nameTextField.getText()));
            }

         }

         public void removeUpdate(DocumentEvent e) {
            if (KeygenDialog.this.nameTextField.getText().length() > 0) {
               KeygenDialog.this.licenseArea.setText(KeygenDialog.this.generateLicense(KeygenDialog.this.nameTextField.getText()));
            }

         }

         public void changedUpdate(DocumentEvent e) {
            if (KeygenDialog.this.nameTextField.getText().length() > 0) {
               KeygenDialog.this.licenseArea.setText(KeygenDialog.this.generateLicense(KeygenDialog.this.nameTextField.getText()));
            }

         }
      });
      this.requestArea.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent e) {
            if (KeygenDialog.this.requestArea.getText().length() > 0) {
               KeygenDialog.this.responseArea.setText(KeygenDialog.this.generateActivation(KeygenDialog.this.requestArea.getText()));
            }

         }

         public void removeUpdate(DocumentEvent e) {
            if (KeygenDialog.this.requestArea.getText().length() > 0) {
               KeygenDialog.this.responseArea.setText(KeygenDialog.this.generateActivation(KeygenDialog.this.requestArea.getText()));
            }

         }

         public void changedUpdate(DocumentEvent e) {
            if (KeygenDialog.this.requestArea.getText().length() > 0) {
               KeygenDialog.this.responseArea.setText(KeygenDialog.this.generateActivation(KeygenDialog.this.requestArea.getText()));
            }

         }
      });
      this.loaderTextField.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent evt) {
            String filename = (new File(KeygenDialog.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getName();
            File f = null;
            String current_dir = null;

            try {
               f = new File(KeygenDialog.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
               current_dir = f.isDirectory() ? f.getPath() : f.getParentFile().toString();
               System.out.print(current_dir);
            } catch (URISyntaxException var21) {
               var21.printStackTrace();
            }

            long newest_time = 0L;
            String newest_file = "burpsuite_jar_not_found.jar";

            try {
               DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(current_dir), "burpsuite_*.jar");
               Throwable var9 = null;

               try {
                  Iterator var10 = dirStream.iterator();

                  while(var10.hasNext()) {
                     Path path = (Path)var10.next();
                     System.out.print(path);
                     if (!Files.isDirectory(path, new LinkOption[0])) {
                        System.out.print(path);
                        if (newest_time < path.toFile().lastModified()) {
                           newest_time = path.toFile().lastModified();
                           newest_file = path.getFileName().toString();
                        }
                     }
                  }
               } catch (Throwable var22) {
                  var9 = var22;
                  throw var22;
               } finally {
                  if (dirStream != null) {
                     if (var9 != null) {
                        try {
                           dirStream.close();
                        } catch (Throwable var20) {
                           var9.addSuppressed(var20);
                        }
                     } else {
                        dirStream.close();
                     }
                  }

               }
            } catch (IOException var24) {
               var24.printStackTrace();
            }

            if (newest_time != 0L) {
               KeygenDialog.this.runButton.setEnabled(true);
            }

            KeygenDialog.this.loaderTextField.setText("java -javaagent:BurpSuiteLoader.jar -noverify" + " -jar " + newest_file);
         }
      });
      this.runButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               Runtime.getRuntime().exec(KeygenDialog.this.loaderTextField.getText());
            } catch (IOException var3) {
               var3.printStackTrace();
            }

         }
      });
      this.nameTextField.addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent evt) {
            KeygenDialog.this.licenseArea.setText(KeygenDialog.this.generateLicense(KeygenDialog.this.nameTextField.getText()));
         }
      });
   }

   private ArrayList<String> decodeActivationRequest(String activationRequest) {
      try {
         byte[] rawBytes = this.decrypt(Base64.getDecoder().decode(activationRequest));
         ArrayList<String> ar = new ArrayList();
         int from = 0;

         for(int i = 0; i < rawBytes.length; ++i) {
            if (rawBytes[i] == 0) {
               ar.add(new String(rawBytes, from, i - from));
               from = i + 1;
            }
         }

         ar.add(new String(rawBytes, from, rawBytes.length - from));
         if (ar.size() != 5) {
            System.out.print("Activation Request Decoded to wrong size! The following was Decoded: \n");
            System.out.print(ar);
            return null;
         } else {
            return ar;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }

   private String generateActivation(String activationRequest) {
      ArrayList<String> request = this.decodeActivationRequest(activationRequest);
      if (request == null) {
         return "Error decoding activation request :-(";
      } else {
         String[] responseArray = new String[]{"0.4315672535134567", (String)request.get(0), "activation", (String)request.get(1), "True", "", (String)request.get(2), (String)request.get(3), "xMoYxfewJJ3jw/Zrqghq1nMHJIsZEtZLu9kp4PZw+kGt+wiTtoUjUfHyTt/luR3BjzVUj2Rt2tTxV2rjWcuV7MlwsbFrLOqTVGqstIYA1psSP/uspFkkhFwhMi0CJNRHdxe+xPYnXObzi/x6G4e0wH3iZ5bnYPRfn7IHiV1TVzslQur/KR5J8BG8CN3B9XaS8+HJ90Hn4sy81fW0NYRlNW8m5k4rMDNwCLvDzp11EN//wxYEdruNKqtxEvv6VesiFOg711Y6g/9Nf91C5dFedNEhPv2k2fYb4rJ+z1mCOBSmWIzjGlS1r2xOzITrrrMkr+ilBE3VFPPbES4KsRh/fw==", "tdq99QBI3DtnQQ7rRJLR0uAdOXT69SUfAB/8O2zi0lsk4/bXkM58TP6cuhOzeYyrVUJrM11IsJhWrv8SiomzJ/rqledlx+P1G5B3MxFVfjML9xQz0ocZi3N+7dHMjf9/jPuFO7KmGfwjWdU4ItXSHFneqGBccCDHEy4bhXKuQrA="};
         return this.prepareArray(responseArray);
      }
   }

   private String generateLicense(String licenseName) {
      String[] licenseArray = new String[]{this.getRandomString(), "license", licenseName, String.valueOf((new Date()).getTime() + 2208986647000L), "1", "full", "tdq99QBI3DtnQQ7rRJLR0uAdOXT69SUfAB/8O2zi0lsk4/bXkM58TP6cuhOzeYyrVUJrM11IsJhWrv8SiomzJ/rqledlx+P1G5B3MxFVfjML9xQz0ocZi3N+7dHMjf9/jPuFO7KmGfwjWdU4ItXSHFneqGBccCDHEy4bhXKuQrA="};
      return this.prepareArray(licenseArray);
   }

   private String prepareArray(String[] array) {
      try {
         ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

         for(int i = 0; i < array.length - 1; ++i) {
            byteArray.write(array[i].getBytes());
            byteArray.write(0);
         }

         byteArray.write(array[array.length - 1].getBytes());
         return new String(Base64.getEncoder().encode(this.encrypt(byteArray.toByteArray())));
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new RuntimeException(var4);
      }
   }

   private byte[] encrypt(byte[] arrayOfByte) {
      try {
         SecretKeySpec localSecretKeySpec = new SecretKeySpec(encryption_key, "DES");
         Cipher localCipher = Cipher.getInstance("DES");
         localCipher.init(1, localSecretKeySpec);
         return localCipher.doFinal(arrayOfByte);
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new RuntimeException(var4);
      }
   }

   private byte[] decrypt(byte[] arrayOfByte) {
      try {
         SecretKeySpec localSecretKeySpec = new SecretKeySpec(encryption_key, "DES");
         Cipher localCipher = Cipher.getInstance("DES");
         localCipher.init(2, localSecretKeySpec);
         return localCipher.doFinal(arrayOfByte);
      } catch (Exception var4) {
         var4.printStackTrace();
         throw new RuntimeException(var4);
      }
   }

   private String getRandomString() {
      String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
      StringBuilder str = new StringBuilder();
      Random rnd = new Random();

      while(str.length() < 32) {
         int index = (int)(rnd.nextFloat() * (float)CHARS.length());
         str.append(CHARS.charAt(index));
      }

      return str.toString();
   }

   public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      JFrame frame = new JFrame("Burp Suite Pro Crack - By 祝融安全");
      frame.setContentPane((new KeygenDialog()).rootPanel);
      frame.setDefaultCloseOperation(3);
      frame.pack();
      frame.setVisible(true);
   }

   private void $$$setupUI$$$() {
      this.rootPanel = new JPanel();
      this.rootPanel.setLayout(new GridLayoutManager(9, 2, new Insets(3, 3, 3, 3), -1, -1));
      this.rootPanel.setPreferredSize(new Dimension(800, 420));
      this.rootPanel.setBorder(BorderFactory.createTitledBorder((Border)null, "Burp Suite Pro Crack - By 祝融安全", 2, 2));
      JLabel label1 = new JLabel();
      label1.setText("许可证文本: ");
      this.rootPanel.add(label1, new GridConstraints(4, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.nameTextField = new JTextField();
      this.nameTextField.setHorizontalAlignment(0);
      this.nameTextField.setText("licensed to zhurongSec");
      this.rootPanel.add(this.nameTextField, new GridConstraints(4, 1, 1, 1, 0, 1, 0, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null, 0, false));
      JLabel label2 = new JLabel();
      label2.setText("执照: ");
      this.rootPanel.add(label2, new GridConstraints(5, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JLabel label3 = new JLabel();
      label3.setText("激活请求: ");
      this.rootPanel.add(label3, new GridConstraints(6, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JLabel label4 = new JLabel();
      label4.setText("激活响应:");
      this.rootPanel.add(label4, new GridConstraints(7, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      Spacer spacer1 = new Spacer();
      this.rootPanel.add(spacer1, new GridConstraints(8, 0, 1, 1, 0, 2, 1, 4, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      Spacer spacer2 = new Spacer();
      this.rootPanel.add(spacer2, new GridConstraints(8, 1, 1, 1, 0, 2, 1, 4, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JScrollPane scrollPane1 = new JScrollPane();
      this.rootPanel.add(scrollPane1, new GridConstraints(5, 1, 1, 1, 0, 3, 5, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.licenseArea = new JTextArea();
      this.licenseArea.setEditable(false);
      Font licenseAreaFont = UIManager.getFont("TextField.font");
      if (licenseAreaFont != null) {
         this.licenseArea.setFont(licenseAreaFont);
      }

      this.licenseArea.setLineWrap(true);
      this.licenseArea.setRows(5);
      scrollPane1.setViewportView(this.licenseArea);
      JScrollPane scrollPane2 = new JScrollPane();
      this.rootPanel.add(scrollPane2, new GridConstraints(6, 1, 1, 1, 0, 3, 5, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.requestArea = new JTextArea();
      Font requestAreaFont = UIManager.getFont("TextField.font");
      if (requestAreaFont != null) {
         this.requestArea.setFont(requestAreaFont);
      }

      this.requestArea.setLineWrap(true);
      this.requestArea.setRows(5);
      scrollPane2.setViewportView(this.requestArea);
      JScrollPane scrollPane3 = new JScrollPane();
      this.rootPanel.add(scrollPane3, new GridConstraints(7, 1, 1, 1, 0, 3, 5, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.responseArea = new JTextArea();
      Font responseAreaFont = UIManager.getFont("TextField.font");
      if (responseAreaFont != null) {
         this.responseArea.setFont(responseAreaFont);
      }

      this.responseArea.setLineWrap(true);
      this.responseArea.setRows(8);
      scrollPane3.setViewportView(this.responseArea);
      JLabel label5 = new JLabel();
      label5.setRequestFocusEnabled(false);
      label5.setText("加载器命令:");
      this.rootPanel.add(label5, new GridConstraints(3, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JLabel label6 = new JLabel();
      label6.setText("1. 使用bootclasspath 中指定的加载器运行 Burp Suite Pro");
      this.rootPanel.add(label6, new GridConstraints(0, 1, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JLabel label7 = new JLabel();
      label7.setText("2. 使用手动激活注册");
      this.rootPanel.add(label7, new GridConstraints(1, 1, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JLabel label8 = new JLabel();
      label8.setText("3. 在后续运行中, 您必须使用加载程序执行 burpsuite 否则它将未注册的");
      this.rootPanel.add(label8, new GridConstraints(2, 1, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JLabel label9 = new JLabel();
      label9.setText("提示:");
      this.rootPanel.add(label9, new GridConstraints(1, 0, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      JPanel panel1 = new JPanel();
      panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
      this.rootPanel.add(panel1, new GridConstraints(3, 1, 1, 1, 0, 3, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      this.loaderTextField = new JTextField();
//      this.loaderTextField.setEditable(false);
      this.loaderTextField.setHorizontalAlignment(0);
      this.loaderTextField.setText("");
      panel1.add(this.loaderTextField, new GridConstraints(0, 0, 1, 1, 0, 1, 4, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null, 0, false));
      this.runButton = new JButton();
      this.runButton.setEnabled(false);
      this.runButton.setText("Run");
      this.runButton.setVerticalAlignment(1);
      panel1.add(this.runButton, new GridConstraints(0, 1, 1, 1, 4, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
      label1.setLabelFor(this.nameTextField);
      label2.setLabelFor(this.licenseArea);
      label3.setLabelFor(this.requestArea);
      label4.setLabelFor(this.responseArea);
   }

   public JComponent $$$getRootComponent$$$() {
      return this.rootPanel;
   }
}
