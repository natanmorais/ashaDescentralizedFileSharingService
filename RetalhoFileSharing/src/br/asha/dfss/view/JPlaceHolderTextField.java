package br.asha.dfss.view;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

@SuppressWarnings("serial")
public class JPlaceHolderTextField extends JTextField {

    private String placeholder;

    public JPlaceHolderTextField() {
    }

    public JPlaceHolderTextField(Document pDoc, String text, int columns) {
        super(pDoc, text, columns);
    }

    public JPlaceHolderTextField(int columns) {
        super(columns);
    }

    public JPlaceHolderTextField(String text) {
        super(text);
    }

    public JPlaceHolderTextField(String text, String placeholder) {
        super(text, 0);
        setPlaceholder(placeholder);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(final String s) {
        placeholder = s;
    }

    @Override
    protected void paintComponent(final Graphics pG) {
        super.paintComponent(pG);

        if (placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        final Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
                .getMaxAscent() + getInsets().top);
    }

}
