package Vista.Componentes;


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class NumeroCellEditor extends DefaultCellEditor {

    public NumeroCellEditor(int maxLength) {
        super(new JTextField());

        JTextField textField = (JTextField) getComponent();

        // Filtro de solo dígitos y longitud máxima
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (esNumero(string) && (fb.getDocument().getLength() + string.length() <= maxLength)) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (esNumero(text) && (fb.getDocument().getLength() - length + text.length() <= maxLength)) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }

            private boolean esNumero(String text) {
                return text.matches("\\d*");
            }
        });
    }
}
