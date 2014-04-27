/*
 * Copyright 2014 Yann D'Isanto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// adaptation of http://tips4java.wordpress.com/2008/11/08/message-console/
package com.mytdev.cliqui.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Create a simple console to display text messages.
 *
 * Messages can be directed here from different sources. Each source can have
 * its messages displayed in a different color.
 *
 * Messages can either be appended to the console or inserted as the first line
 * of the console
 *
 * You can limit the number of lines to hold in the Document.
 */
public final class MessageConsole {

    private JTextComponent textComponent;
    private Document document;
    private boolean isAppend;
    private DocumentListener limitLinesListener;

    public MessageConsole(JTextComponent textComponent) {
        this(textComponent, true);
    }

    /**
     * Use the text component specified as a simply console to display text
     * messages.
     *
     * The messages can either be appended to the end of the console or inserted
     * as the first line of the console.
     *
     * @param textComponent
     * @param isAppend
     */
    public MessageConsole(JTextComponent textComponent, boolean isAppend) {
        this.textComponent = textComponent;
        this.document = textComponent.getDocument();
        this.isAppend = isAppend;
        textComponent.setEditable(false);
    }

    public MessageConsole redirect(InputStream input) {
        return redirect(input, null);
    }

    public MessageConsole redirect(InputStream input, Color textColor) {
        final ConsoleOutputStream cos = new ConsoleOutputStream(textColor, null);
        new StreamPipe(input, cos).start();
        return this;
    }

    /**
     * To prevent memory from being used up you can control the number of lines
     * to display in the console
     *
     * This number can be dynamically changed, but the console will only be
     * updated the next time the Document is updated.
     *
     * @param lines
     * @return this message console
     */
    public MessageConsole messageLines(int lines) {
        if (limitLinesListener != null) {
            document.removeDocumentListener(limitLinesListener);
        }

        limitLinesListener = new LimitLinesDocumentListener(lines, isAppend);
        document.addDocumentListener(limitLinesListener);
        return this;
    }

    /**
     * Class to intercept output from a PrintStream and add it to a Document.
     * The output can optionally be redirected to a different PrintStream. The
     * text displayed in the Document can be color coded to indicate the output
     * source.
     */
    private final class ConsoleOutputStream extends ByteArrayOutputStream {

        private final String EOL = System.getProperty("line.separator");
        private final SimpleAttributeSet attributes;
        private final PrintStream printStream;
        private final StringBuffer buffer = new StringBuffer(80);
        private boolean isFirstLine;

        /**
         * Specify the option text color and PrintStream
         */
        public ConsoleOutputStream(Color textColor, PrintStream printStream) {
            if (textColor != null) {
                attributes = new SimpleAttributeSet();
                StyleConstants.setForeground(attributes, textColor);
            } else {
                attributes = null;
            }
            this.printStream = printStream;
            if (isAppend) {
                isFirstLine = true;
            }
        }

        /**
         * Override this method to intercept the output text. Each line of text
         * output will actually involve invoking this method twice:
         *
         * a) for the actual text message b) for the newLine string
         *
         * The message will be treated differently depending on whether the line
         * will be appended or inserted into the Document
         */
        @Override
        public void flush() {
            final String message = toString();
            if (message.length() == 0) {
                return;
            }

            if (isAppend) {
                handleAppend(message);
            } else {
                handleInsert(message);
            }

            reset();
        }

        /**
         * We don't want to have blank lines in the Document. The first line
         * added will simply be the message. For additional lines it will be:
         *
         * newLine + message
         */
        private void handleAppend(String message) {
            //  This check is needed in case the text in the Document has been
            //	cleared. The buffer may contain the EOL string from the previous
            //  message.

            if (document.getLength() == 0) {
                buffer.setLength(0);
            }

            if (EOL.equals(message)) {
                buffer.append(message);
            } else {
                buffer.append(message);
                clearBuffer();
            }

        }

        /**
         * We don't want to merge the new message with the existing message so
         * the line will be inserted as:
         *
         * message + newLine
         */
        private void handleInsert(String message) {
            buffer.append(message);

            if (EOL.equals(message)) {
                clearBuffer();
            }
        }

        /**
         * The message and the newLine have been added to the buffer in the
         * appropriate order so we can now update the Document and send the text
         * to the optional PrintStream.
         */
        private void clearBuffer() {
            //  In case both the standard out and standard err are being redirected
            //  we need to insert a newline character for the first line only

            if (isFirstLine && document.getLength() != 0) {
                buffer.insert(0, "\n");
            }

            isFirstLine = false;
            final String line = buffer.toString();
            try {
                if (isAppend) {
                    int offset = document.getLength();
                    document.insertString(offset, line, attributes);
                    textComponent.setCaretPosition(document.getLength());
                } else {
                    document.insertString(0, line, attributes);
                    textComponent.setCaretPosition(0);
                }
            } catch (BadLocationException ble) {
            }

            if (printStream != null) {
                printStream.print(line);
            }

            buffer.setLength(0);
        }
    }
}
