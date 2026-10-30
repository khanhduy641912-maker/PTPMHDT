import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

class DataTableForm extends JPanel {
    interface Validator {
        String validate(String[] values);
    }

    static final class Field {
        final String label;
        final boolean required;
        final String[] options;

        private Field(String label, boolean required, String... options) {
            this.label = label;
            this.required = required;
            this.options = options;
        }

        static Field required(String label) {
            return new Field(label, true);
        }

        static Field optional(String label) {
            return new Field(label, false);
        }

        static Field choice(String label, String... options) {
            return new Field(label, true, options);
        }
    }

    private final String singular;
    private final List<Field> fields;
    private final DefaultTableModel model;
    private final Validator validator;

    DataTableForm(String title, String description, String addLabel, String singular,
            String[] columns, List<Field> fields, String[][] sampleRows, Validator validator) {
        this.singular = singular;
        this.fields = fields;
        this.validator = validator;
        if (fields.size() != columns.length) {
            throw new IllegalArgumentException("Form fields must match table columns for " + title);
        }
        for (String[] row : sampleRows) {
            if (row.length != columns.length) {
                throw new IllegalArgumentException("Sample row must match table columns for " + title);
            }
        }

        setLayout(new BorderLayout(0, 15));
        setBackground(FormUi.BACKGROUND);
        add(createHeading(title, description), BorderLayout.NORTH);

        JPanel card = FormUi.cardPanel();
        card.setLayout(new BorderLayout(0, 14));
        JTextField search = new JTextField();
        search.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        search.setPreferredSize(new Dimension(270, 37));
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FormUi.BORDER), BorderFactory.createEmptyBorder(8, 11, 8, 11)));
        search.putClientProperty("JTextField.placeholderText", "Tìm kiếm trong danh sách...");
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);
        toolbar.add(search, BorderLayout.WEST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        javax.swing.JButton add = FormUi.primaryButton(addLabel);
        javax.swing.JButton edit = FormUi.secondaryButton("Chỉnh sửa");
        javax.swing.JButton delete = FormUi.secondaryButton("Xóa");
        edit.setEnabled(false);
        delete.setEnabled(false);
        actions.add(add);
        actions.add(edit);
        actions.add(delete);
        toolbar.add(actions, BorderLayout.EAST);
        card.add(toolbar, BorderLayout.NORTH);

        model = new DefaultTableModel(sampleRows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = createTable(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String query = search.getText().trim();
                sorter.setRowFilter(query.isEmpty() ? null
                        : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(query)));
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent event) {
                update();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent event) {
                update();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent event) {
                update();
            }
        });
        table.getSelectionModel().addListSelectionListener(event -> {
            boolean selected = table.getSelectedRow() >= 0;
            edit.setEnabled(selected);
            delete.setEnabled(selected);
        });
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        JLabel note = new JLabel("  " + sampleRows.length + " bản ghi mẫu • Dữ liệu hiện chỉ lưu trong phiên giao diện");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        note.setForeground(FormUi.MUTED);
        card.add(note, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);

        add.addActionListener(event -> showEditor(null, -1, addLabel));
        edit.addActionListener(event -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                String[] values = new String[model.getColumnCount()];
                for (int i = 0; i < values.length; i++) {
                    values[i] = String.valueOf(model.getValueAt(modelRow, i));
                }
                showEditor(values, modelRow, "Chỉnh sửa " + singular);
            }
        });
        delete.addActionListener(event -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0 && JOptionPane.showConfirmDialog(this,
                    "Xóa dòng dữ liệu đang chọn khỏi danh sách giao diện?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                model.removeRow(table.convertRowIndexToModel(viewRow));
            }
        });
    }

    private JPanel createHeading(String title, String description) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
        heading.setForeground(FormUi.TEXT);
        JLabel detail = new JLabel(description);
        detail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detail.setForeground(FormUi.MUTED);
        panel.add(heading);
        panel.add(javax.swing.Box.createVerticalStrut(5));
        panel.add(detail);
        return panel;
    }

    private JTable createTable(DefaultTableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setForeground(FormUi.TEXT);
        table.setRowHeight(36);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(230, 239, 255));
        table.setSelectionForeground(FormUi.TEXT);
        table.setGridColor(new Color(238, 241, 246));
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 10));
        header.setForeground(FormUi.MUTED);
        header.setBackground(new Color(249, 250, 252));
        header.setPreferredSize(new Dimension(0, 36));
        return table;
    }

    private void showEditor(String[] existing, int rowIndex, String title) {
        JDialog dialog = new JDialog(javax.swing.SwingUtilities.getWindowAncestor(this), title,
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(560, Math.min(720, 175 + fields.size() * 58));
        dialog.setLocationRelativeTo(this);
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(22, 24, 18, 24));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(FormUi.TEXT);
        root.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        List<JComponent> inputs = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = i;
            c.insets = new Insets(6, 4, 6, 4);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.weightx = 0.34;
            JLabel label = new JLabel(field.label + (field.required ? " *" : ""));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(FormUi.TEXT);
            form.add(label, c);
            c.gridx = 1;
            c.weightx = 0.66;
            JComponent input;
            if (field.options.length > 0) {
                JComboBox<String> combo = new JComboBox<>(field.options);
                combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                input = combo;
            } else {
                JTextField text = new JTextField();
                text.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                text.setPreferredSize(new Dimension(220, 32));
                input = text;
            }
            if (existing != null && i < existing.length) {
                setValue(input, existing[i]);
            }
            inputs.add(input);
            form.add(input, c);
        }
        root.add(new JScrollPane(form), BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        JLabel requiredNote = new JLabel(" * Trường bắt buộc");
        requiredNote.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        requiredNote.setForeground(FormUi.MUTED);
        footer.add(requiredNote, BorderLayout.WEST);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setBackground(Color.WHITE);
        javax.swing.JButton cancel = FormUi.secondaryButton("Hủy");
        javax.swing.JButton save = FormUi.primaryButton("Lưu thông tin");
        buttons.add(cancel);
        buttons.add(save);
        footer.add(buttons, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);
        cancel.addActionListener(event -> dialog.dispose());
        save.addActionListener(event -> {
            String[] values = new String[fields.size()];
            for (int i = 0; i < fields.size(); i++) {
                values[i] = getValue(inputs.get(i));
                if (fields.get(i).required && values[i].trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập " + fields.get(i).label + ".",
                            "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            String error = validator == null ? null : validator.validate(values);
            if (error != null) {
                JOptionPane.showMessageDialog(dialog, error, "Dữ liệu chưa hợp lệ",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (rowIndex < 0) {
                model.addRow(values);
            } else {
                for (int i = 0; i < values.length; i++) {
                    model.setValueAt(values[i], rowIndex, i);
                }
            }
            dialog.dispose();
        });
        dialog.setContentPane(root);
        dialog.setVisible(true);
    }

    private void setValue(JComponent input, String value) {
        if (input instanceof JTextField) {
            ((JTextField) input).setText(value);
        } else if (input instanceof JComboBox) {
            ((JComboBox<?>) input).setSelectedItem(value);
        }
    }

    private String getValue(JComponent input) {
        if (input instanceof JTextField) {
            return ((JTextField) input).getText().trim();
        }
        if (input instanceof JComboBox) {
            Object value = ((JComboBox<?>) input).getSelectedItem();
            return value == null ? "" : value.toString();
        }
        return "";
    }
}
