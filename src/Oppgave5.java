import db.ConnectToDB;

import javax.swing.*;
import javax.xml.transform.Source;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Lars on 22.09.2015.
 */
public class Oppgave5 extends JFrame implements ActionListener {
    private JLabel lblAuthor = new JLabel("Forfatter");
    private JLabel lblTitle = new JLabel("Title");
    private JLabel lblISBN = new JLabel("ISBN");
    private JTextField txtAuthor = new JTextField(20);
    private JTextField txtTitle = new JTextField(20);
    private JTextField txtISBN = new JTextField(20);
    private JButton btnFirst = new JButton("Første");
    private JButton btnLast = new JButton("Siste");
    private JButton btnNext = new JButton("Neste");
    private JButton btnPrevious = new JButton("Forrige");
    private JButton btnExit = new JButton("Avslutt");
    private JPanel panelFields = new JPanel(new GridLayout(3, 2, 5, 5));
    private JPanel panelButtons = new JPanel(new GridLayout(1, 5));
    private ResultSet res;
    private ConnectToDB db;

    public Oppgave5(String user, String password) throws SQLException {
        db = new ConnectToDB("localhost", "pg3100", user, password);
        Statement stmt = db.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String sql = "SELECT * FROM bokliste";
        res = stmt.executeQuery(sql);

        setLayout(new BorderLayout());
        setTitle("Navigering i boklisten");

        panelFields.add(lblAuthor);
        panelFields.add(txtAuthor);
        panelFields.add(lblTitle);
        panelFields.add(txtTitle);
        panelFields.add(lblISBN);
        panelFields.add(txtISBN);

        btnFirst.addActionListener(this);
        btnLast.addActionListener(this);
        btnNext.addActionListener(this);
        btnPrevious.addActionListener(this);
        btnExit.addActionListener(this);

        panelButtons.add(btnFirst);
        panelButtons.add(btnLast);
        panelButtons.add(btnNext);
        panelButtons.add(btnPrevious);
        panelButtons.add(btnExit);

        add(panelFields, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);

        setSize(400, 150);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void showRows() {
        try {
            txtAuthor.setText(res.getString("FORFATTER"));
            txtTitle.setText(res.getString("TITTEL"));
            txtISBN.setText(res.getString("ISBN"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button == btnFirst) {
            try {
                res.first();
                showRows();
            } catch (SQLException b1) {
                b1.printStackTrace();
            }
        } else if (button == btnNext) {
            try {
                if (!res.isLast() && !res.isAfterLast()) {
                    res.next();
                } else {
                    res.first();
                }
                showRows();
            } catch (SQLException b2) {
                b2.printStackTrace();
            }
        } else if (button == btnLast) {
            try {
                res.last();
                showRows();
            } catch (SQLException b3) {
                b3.printStackTrace();
            }
        } else if (button == btnPrevious) {
            try {
                if (!res.isFirst() && !res.isBeforeFirst()) {
                    res.previous();
                } else {
                    res.last();
                }
                showRows();
            } catch (SQLException b4) {
                b4.printStackTrace();
            }
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws SQLException {
        new Oppgave5("root", "root");
    }
}
