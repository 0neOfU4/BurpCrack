package com.intellij.uiDesigner.core;

import java.lang.reflect.Method;
import javax.swing.JComponent;

public final class SupportCode {
   public static SupportCode.TextWithMnemonic parseText(String textWithMnemonic) {
      if (textWithMnemonic == null) {
         throw new IllegalArgumentException("textWithMnemonic cannot be null");
      } else {
         int index = -1;
         StringBuffer plainText = new StringBuffer();

         for(int i = 0; i < textWithMnemonic.length(); ++i) {
            char ch = textWithMnemonic.charAt(i);
            if (ch == '&') {
               ++i;
               if (i >= textWithMnemonic.length()) {
                  break;
               }

               ch = textWithMnemonic.charAt(i);
               if (ch != '&') {
                  index = plainText.length();
               }
            }

            plainText.append(ch);
         }

         return new SupportCode.TextWithMnemonic(plainText.toString(), index);
      }
   }

   public static void setDisplayedMnemonicIndex(JComponent component, int index) {
      try {
         Method method = component.getClass().getMethod("setDisplayedMnemonicIndex", Integer.TYPE);
         method.setAccessible(true);
         method.invoke(component, new Integer(index));
      } catch (Exception var3) {
      }

   }

   public static final class TextWithMnemonic {
      public final String myText;
      public final int myMnemonicIndex;

      private TextWithMnemonic(String text, int index) {
         if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
         } else if (index == -1 || index >= 0 && index < text.length()) {
            this.myText = text;
            this.myMnemonicIndex = index;
         } else {
            throw new IllegalArgumentException("wrong index: " + index + "; text = '" + text + "'");
         }
      }

      public char getMnemonicChar() {
         if (this.myMnemonicIndex == -1) {
            throw new IllegalStateException("text doesn't contain mnemonic");
         } else {
            return Character.toUpperCase(this.myText.charAt(this.myMnemonicIndex));
         }
      }

      // $FF: synthetic method
      TextWithMnemonic(String x0, int x1, Object x2) {
         this(x0, x1);
      }
   }
}
