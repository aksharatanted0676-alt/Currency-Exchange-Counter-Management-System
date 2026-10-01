import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

/**
 * FX Trading Terminal — Executive Light Fintech UI (Refined & Aligned)
 *
 * PALETTE SPECIFICATION (from Swatch Image 2):
 *  - Canvas Background:  #FFECC8 (Warm Light Cream)
 *  - Header & Sidebar:   #0A1F44 (Deep Navy Blue)
 *  - Accent & Active:    #E3B44C (Warm Gold)
 *  - Secondary Accent:   #2A4B7C (Steel Blue)
 *  - Neutral / Subtitles: #556270 (Slate Gray)
 *  - Cards & Panels:     #FFFFFF (Pure White)
 *
 * REFINEMENTS:
 *  - Sidebar width expanded to 250px to eliminate truncation of "Currencies & Rates" and logo text.
 *  - Crisp, modern, bold sans-serif typography (Segoe UI / Helvetica Neue / SF Pro / Arial).
 *  - High-contrast selected sidebar state.
 *  - Quick Actions toolbar card integration.
 *  - Backend logic 100% UNTOUCHED (public API only).
 */
public class CurrencyExchangeGUI extends JFrame {

    // =========================================================
    // BACKEND REFERENCE
    // =========================================================
    private CurrencyExchangeSystem system;
    private int customerId = 1001;

    // =========================================================
    // NAVIGATION & CARDS
    // =========================================================
    private CardLayout cardLayout;
    private JPanel     contentArea;
    private JPanel[]   navItems;
    private int        currentNav = 0;

    private static final String PANEL_DASHBOARD    = "dashboard";
    private static final String PANEL_EXCHANGE     = "exchange";
    private static final String PANEL_TRANSACTIONS = "transactions";
    private static final String PANEL_CURRENCIES   = "currencies";
    private static final String PANEL_SEARCH       = "search";
    private static final String PANEL_REPORTS      = "reports";

    // =========================================================
    // FORM COMPONENTS
    // =========================================================
    private JTextField customerNameField;
    private JTextField phoneField;
    private JTextField amountField;
    private JTextField rateField;
    private JTextField convertedAmountField;

    private JComboBox<String> currencyComboBox;
    private JLabel            buyBtnToggle;
    private JLabel            sellBtnToggle;
    private String            selectedType = "BUY";

    // =========================================================
    // TABLES & MODELS
    // =========================================================
    private JTable            dashboardTxnTable;
    private DefaultTableModel dashboardTxnModel;
    private JPanel            recentTxnContainer;

    private JTable            transactionTable;
    private DefaultTableModel tableModel;

    private JTextField        searchField;
    private JTable            searchTable;
    private DefaultTableModel searchTableModel;

    private JTable            currencyTable;
    private DefaultTableModel currencyTableModel;
    private JTable            rateTable;
    private DefaultTableModel rateTableModel;

    private JTable            marketRatesTable;
    private DefaultTableModel marketRatesModel;

    // =========================================================
    // STATS & HEADER LABELS
    // =========================================================
    private JLabel statTotalTxnsVal;
    private JLabel statTxnsTodayVal;
    private JLabel statTotalExchangedVal;
    private JLabel statActiveCurrenciesVal;
    private JPanel analyticsContainer;

    private JLabel clockLabel;
    private JLabel dateLabel;

    // =========================================================
    // ██████████   EXECUTIVE LIGHT THEME SYSTEM   ██████████
    // =========================================================
    static class LightTheme {

        // --- Swatch Palette Colors ---
        static final Color DEEP_NAVY      = new Color(0x0A, 0x1F, 0x44); // #0A1F44
        static final Color STEEL_BLUE     = new Color(0x2A, 0x4B, 0x7C); // #2A4B7C
        static final Color NAV_ACTIVE_BG  = new Color(0x15, 0x34, 0x66); // Rich Steel Navy Highlight
        static final Color WARM_GOLD      = new Color(0xE3, 0xB4, 0x4C); // #E3B44C
        static final Color CREAM_CANVAS   = new Color(0xFF, 0xEC, 0xC8); // #FFECC8 Base
        static final Color SLATE_GRAY     = new Color(0x55, 0x62, 0x70); // #556270

        // --- Container Surfaces ---
        static final Color CARD_BG        = new Color(0xFF, 0xFF, 0xFF); // Pure Crisp White
        static final Color CARD_ALT_BG    = new Color(0xFF, 0xF9, 0xEE); // Warm Ivory Accent
        static final Color INPUT_BG       = new Color(0xFF, 0xF6, 0xE5); // Warm Input Background
        static final Color ROW_ALT_BG     = new Color(0xF9, 0xF5, 0xEB);

        // --- Status Colors ---
        static final Color BUY_GREEN      = new Color(0x1B, 0x8A, 0x4C); // Deep Forest Green
        static final Color SELL_RED       = new Color(0xC0, 0x26, 0x2D); // Deep Crimson Red

        // --- Borders ---
        static final Color BORDER_SUBTLE  = new Color(0xD8, 0xC5, 0xA3);
        static final Color BORDER_GOLD    = new Color(0xE3, 0xB4, 0x4C);
        static final Color BORDER_NAVY    = new Color(0x2A, 0x4B, 0x7C);

        // --- Modern Sans-Serif Typography ---
        private static Font font(String name, int style, int size) {
            Font f = new Font(name, style, size);
            return f.getFamily().equalsIgnoreCase(name) ? f : null;
        }

        static Font sansFont(int style, int size) {
            String[] preferred = {"Segoe UI", "Helvetica Neue", "SF Pro Display", "Inter", "Arial", "SansSerif"};
            for (String name : preferred) {
                Font f = font(name, style, size);
                if (f != null) return f;
            }
            return new Font("SansSerif", style, size);
        }

        static final Font FONT_TITLE      = sansFont(Font.BOLD, 20);
        static final Font FONT_HEADING    = sansFont(Font.BOLD, 15);
        static final Font FONT_SUBHEADING = sansFont(Font.BOLD, 13);
        static final Font FONT_BODY       = sansFont(Font.BOLD, 12);
        static final Font FONT_SMALL      = sansFont(Font.BOLD, 11);
        static final Font FONT_STAT_BIG   = sansFont(Font.BOLD, 26);
        static final Font FONT_MONO       = new Font("Monospaced", Font.BOLD, 13);

        // --- Component Helpers ---
        static JLabel label(String text, Font f, Color fg) {
            JLabel l = new JLabel(text);
            l.setFont(f);
            l.setForeground(fg);
            l.setOpaque(false);
            return l;
        }

        static JTextField inputField() {
            JTextField tf = new JTextField() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(INPUT_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            tf.setFont(FONT_BODY);
            tf.setForeground(DEEP_NAVY);
            tf.setBackground(INPUT_BG);
            tf.setCaretColor(DEEP_NAVY);
            tf.setPreferredSize(new Dimension(0, 36));
            tf.setBorder(BorderFactory.createCompoundBorder(
                new LightBorder(BORDER_SUBTLE, 6, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            tf.setOpaque(false);

            tf.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                        new LightBorder(WARM_GOLD, 6, 2),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)
                    ));
                }
                @Override public void focusLost(FocusEvent e) {
                    tf.setBorder(BorderFactory.createCompoundBorder(
                        new LightBorder(BORDER_SUBTLE, 6, 1),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)
                    ));
                }
            });
            return tf;
        }

        static JComboBox<String> comboBox(String[] items) {
            JComboBox<String> cb = new JComboBox<>(items);
            cb.setFont(FONT_BODY);
            cb.setForeground(DEEP_NAVY);
            cb.setBackground(INPUT_BG);
            cb.setPreferredSize(new Dimension(0, 36));
            cb.setBorder(BorderFactory.createCompoundBorder(
                new LightBorder(BORDER_SUBTLE, 6, 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            cb.setRenderer(new DefaultListCellRenderer() {
                @Override public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setBackground(isSelected ? WARM_GOLD : INPUT_BG);
                    setForeground(isSelected ? DEEP_NAVY : DEEP_NAVY);
                    setFont(FONT_BODY);
                    setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                    return this;
                }
            });
            return cb;
        }

        static JButton primaryButton(String text) {
            JButton btn = new JButton(text) {
                private boolean hovered = false;
                {
                    addMouseListener(new MouseAdapter() {
                        @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                        @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                    });
                }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color c1 = hovered ? new Color(0xF2, 0xC6, 0x65) : WARM_GOLD;
                    Color c2 = hovered ? WARM_GOLD : new Color(0xC7, 0x99, 0x32);
                    GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(sansFont(Font.BOLD, 12));
            btn.setForeground(DEEP_NAVY);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            return btn;
        }

        static JButton secondaryButton(String text) {
            JButton btn = new JButton(text) {
                private boolean hovered = false;
                {
                    addMouseListener(new MouseAdapter() {
                        @Override public void mouseEntered(MouseEvent e) { hovered = true;  setForeground(WARM_GOLD); repaint(); }
                        @Override public void mouseExited(MouseEvent e)  { hovered = false; setForeground(Color.WHITE); repaint(); }
                    });
                }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(hovered ? DEEP_NAVY : STEEL_BLUE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(sansFont(Font.BOLD, 12));
            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            return btn;
        }

        static JButton ghostButton(String text) {
            JButton btn = new JButton(text) {
                private boolean hovered = false;
                {
                    addMouseListener(new MouseAdapter() {
                        @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                        @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                    });
                }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (hovered) {
                        g2.setColor(new Color(0xE3, 0xB4, 0x4C, 40));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    }
                    g2.setColor(hovered ? WARM_GOLD : BORDER_SUBTLE);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(sansFont(Font.BOLD, 11));
            btn.setForeground(DEEP_NAVY);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
            return btn;
        }

        static JScrollPane lightScroll(Component view) {
            JScrollPane sp = new JScrollPane(view);
            sp.setBackground(CREAM_CANVAS);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.getViewport().setBackground(CREAM_CANVAS);
            sp.getVerticalScrollBar().setUI(new LightScrollBarUI());
            sp.getHorizontalScrollBar().setUI(new LightScrollBarUI());
            return sp;
        }
    }

    // =========================================================
    // LIGHT BORDER
    // =========================================================
    static class LightBorder extends AbstractBorder {
        private final Color color;
        private final int   radius;
        private final int   thickness;

        LightBorder(Color color, int radius, int thickness) {
            this.color     = color;
            this.radius    = radius;
            this.thickness = thickness;
        }

        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }

        @Override public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 1, thickness + 1, thickness + 1, thickness + 1);
        }
    }

    // =========================================================
    // SCROLL BAR UI
    // =========================================================
    static class LightScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = LightTheme.WARM_GOLD;
            trackColor = LightTheme.INPUT_BG;
        }
        @Override protected JButton createDecreaseButton(int orientation) { return zeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return zeroButton(); }
        private JButton zeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 4, 4);
            g2.dispose();
        }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(r.x, r.y, r.width, r.height);
            g2.dispose();
        }
    }

    // =========================================================
    // LIGHT PANEL CARD
    // =========================================================
    static class LightCard extends JPanel {
        LightCard() {
            setOpaque(false);
            setLayout(new BorderLayout());
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(LightTheme.CARD_BG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.setColor(LightTheme.BORDER_SUBTLE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            g2.dispose();
        }
    }

    // =========================================================
    // TABLE CELL RENDERER
    // =========================================================
    static class LightTableRenderer extends DefaultTableCellRenderer {
        private final int typeCol;
        private final int statusCol;

        LightTableRenderer(int typeCol, int statusCol) {
            this.typeCol   = typeCol;
            this.statusCol = statusCol;
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(LightTheme.FONT_BODY);

            if (isSelected) {
                setBackground(LightTheme.WARM_GOLD);
                setForeground(LightTheme.DEEP_NAVY);
            } else {
                setBackground(row % 2 == 0 ? LightTheme.CARD_BG : LightTheme.ROW_ALT_BG);
                setForeground(LightTheme.DEEP_NAVY);

                if (typeCol >= 0 && column == typeCol && value != null) {
                    String str = value.toString();
                    if ("BUY".equalsIgnoreCase(str)) {
                        setForeground(LightTheme.BUY_GREEN);
                        setFont(LightTheme.FONT_BODY);
                    } else if ("SELL".equalsIgnoreCase(str)) {
                        setForeground(LightTheme.SELL_RED);
                        setFont(LightTheme.FONT_BODY);
                    }
                }

                if (statusCol >= 0 && column == statusCol && value != null) {
                    String str = value.toString();
                    if ("ACTIVE".equalsIgnoreCase(str) || "COMPLETED".equalsIgnoreCase(str)) {
                        setForeground(LightTheme.BUY_GREEN);
                        setFont(LightTheme.FONT_BODY);
                    }
                }
            }

            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setOpaque(true);
            return this;
        }
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public CurrencyExchangeGUI() {
        system = new CurrencyExchangeSystem();
        loadSampleData();
        applyLAF();
        createGUI();
    }

    private void applyLAF() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        UIManager.put("ComboBox.background",         LightTheme.INPUT_BG);
        UIManager.put("ComboBox.foreground",          LightTheme.DEEP_NAVY);
        UIManager.put("ComboBox.selectionBackground", LightTheme.WARM_GOLD);
        UIManager.put("ComboBox.selectionForeground", LightTheme.DEEP_NAVY);
        UIManager.put("Panel.background",            LightTheme.CREAM_CANVAS);
        UIManager.put("OptionPane.background",       LightTheme.CARD_BG);
        UIManager.put("OptionPane.messageForeground", LightTheme.DEEP_NAVY);
    }

    private void loadSampleData() {
        try {
            system.addCurrency(new Currency("USD", "US Dollar"));
            system.addCurrency(new Currency("EUR", "Euro"));
            system.addCurrency(new Currency("GBP", "British Pound"));
            system.addCurrency(new Currency("JPY", "Japanese Yen"));

            system.addExchangeRate(new ExchangeRate("USD", new BigDecimal("82.00"),  new BigDecimal("83.00")));
            system.addExchangeRate(new ExchangeRate("EUR", new BigDecimal("89.00"),  new BigDecimal("91.00")));
            system.addExchangeRate(new ExchangeRate("GBP", new BigDecimal("103.00"), new BigDecimal("105.00")));
            system.addExchangeRate(new ExchangeRate("JPY", new BigDecimal("0.55"),   new BigDecimal("0.58")));
        } catch (Exception e) {
            System.err.println("Sample data error: " + e.getMessage());
        }
    }

    // =========================================================
    // CREATE GUI
    // =========================================================
    private void createGUI() {
        setTitle("Currency Exchange Management System — Executive Terminal");
        setSize(1300, 800);
        setMinimumSize(new Dimension(1080, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(LightTheme.CREAM_CANVAS);
        setContentPane(root);

        root.add(buildSidebar(),   BorderLayout.WEST);
        root.add(buildRightArea(), BorderLayout.CENTER);

        updateExchangeRate();
        refreshDashboard();
    }

    // =========================================================
    // SIDEBAR (Deep Navy #0A1F44 - Expanded 250px)
    // =========================================================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(LightTheme.DEEP_NAVY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(250, 0)); // Expanded width to eliminate truncation
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);

        JPanel logoArea = new JPanel();
        logoArea.setOpaque(false);
        logoArea.setLayout(new BoxLayout(logoArea, BoxLayout.Y_AXIS));
        logoArea.setBorder(BorderFactory.createEmptyBorder(18, 18, 14, 18));
        logoArea.setMaximumSize(new Dimension(250, 75));

        JLabel icon    = LightTheme.label("◈ FX TERMINAL", LightTheme.FONT_HEADING, LightTheme.WARM_GOLD);
        JLabel logoSub = LightTheme.label("EXECUTIVE CONTROL CENTER", LightTheme.FONT_SMALL, Color.WHITE);

        logoArea.add(icon);
        logoArea.add(Box.createVerticalStrut(3));
        logoArea.add(logoSub);
        sidebar.add(logoArea);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(250, 1));
        sep.setForeground(LightTheme.STEEL_BLUE);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));

        String[][] navData = {
            {"⊞", "Dashboard",          PANEL_DASHBOARD},
            {"⇄", "Exchange",           PANEL_EXCHANGE},
            {"≡", "Transactions",       PANEL_TRANSACTIONS},
            {"◎", "Currencies & Rates",  PANEL_CURRENCIES},
            {"⌕", "Search",             PANEL_SEARCH},
            {"⊙", "Reports",            PANEL_REPORTS}
        };

        navItems = new JPanel[navData.length];
        for (int i = 0; i < navData.length; i++) {
            navItems[i] = buildNavItem(navData[i][0], navData[i][1], navData[i][2], i);
            navItems[i].setMaximumSize(new Dimension(250, 44));
            sidebar.add(navItems[i]);
            sidebar.add(Box.createVerticalStrut(3));
        }

        sidebar.add(Box.createVerticalGlue());

        JPanel tag = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 12));
        tag.setOpaque(false);
        tag.setMaximumSize(new Dimension(250, 42));
        tag.add(LightTheme.label("v1.0 · Executive Suite", LightTheme.FONT_SMALL, LightTheme.WARM_GOLD));
        sidebar.add(tag);

        setNavActive(0);
        return sidebar;
    }

    private JPanel buildNavItem(String icon, String label, String panelName, int index) {
        JPanel item = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (currentNav == index) {
                    g2.setColor(LightTheme.NAV_ACTIVE_BG);
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 6, 6);
                    g2.setColor(LightTheme.WARM_GOLD);
                    g2.fillRect(0, 4, 4, getHeight() - 8);
                }
                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JPanel inner = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 6));

        boolean active = (currentNav == index);
        JLabel iconLbl = LightTheme.label(icon, LightTheme.FONT_HEADING, active ? LightTheme.WARM_GOLD : Color.LIGHT_GRAY);
        JLabel textLbl = LightTheme.label(label, LightTheme.FONT_BODY, active ? Color.WHITE : Color.LIGHT_GRAY);

        inner.add(iconLbl);
        inner.add(textLbl);
        item.add(inner, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                setNavActive(index);
                cardLayout.show(contentArea, panelName);
                if (panelName.equals(PANEL_DASHBOARD))    refreshDashboard();
                if (panelName.equals(PANEL_TRANSACTIONS)) refreshTable(transactionTable, tableModel);
                if (panelName.equals(PANEL_CURRENCIES))   refreshCurrencyTables();
            }
        });
        return item;
    }

    private void setNavActive(int index) {
        currentNav = index;
        if (navItems == null) return;
        for (JPanel item : navItems) item.repaint();
        for (int i = 0; i < navItems.length; i++) {
            JPanel inner = (JPanel)((JPanel) navItems[i].getComponent(0));
            boolean active = (i == index);
            JLabel iconLbl = (JLabel) inner.getComponent(0);
            JLabel textLbl = (JLabel) inner.getComponent(1);
            iconLbl.setForeground(active ? LightTheme.WARM_GOLD : Color.LIGHT_GRAY);
            textLbl.setForeground(active ? Color.WHITE : Color.LIGHT_GRAY);
        }
    }

    // =========================================================
    // RIGHT AREA
    // =========================================================
    private JPanel buildRightArea() {
        JPanel right = new JPanel(new BorderLayout(0, 0));
        right.setOpaque(false);
        right.add(buildHeader(),      BorderLayout.NORTH);
        right.add(buildContentArea(), BorderLayout.CENTER);
        return right;
    }

    // =========================================================
    // TOP HEADER (Deep Navy #0A1F44)
    // =========================================================
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(LightTheme.DEEP_NAVY);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel title = LightTheme.label("Currency Exchange Dashboard", LightTheme.FONT_TITLE, Color.WHITE);
        JLabel sub   = LightTheme.label("Manage exchange rates, process conversions & monitor transactions", LightTheme.FONT_SMALL, LightTheme.WARM_GOLD);

        left.add(title);
        left.add(Box.createVerticalStrut(2));
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 12));
        right.setOpaque(false);

        dateLabel  = LightTheme.label(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toUpperCase(), LightTheme.FONT_BODY, Color.WHITE);
        clockLabel = LightTheme.label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), LightTheme.FONT_BODY, LightTheme.WARM_GOLD);

        JLabel status = new JLabel("● SYSTEM ONLINE") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(LightTheme.WARM_GOLD);
                g2.fillOval(0, getHeight()/2 - 5, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        status.setFont(LightTheme.FONT_BODY);
        status.setForeground(LightTheme.WARM_GOLD);
        status.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        right.add(dateLabel);
        right.add(LightTheme.label("│", LightTheme.FONT_BODY, LightTheme.STEEL_BLUE));
        right.add(clockLabel);
        right.add(LightTheme.label("│", LightTheme.FONT_BODY, LightTheme.STEEL_BLUE));
        right.add(status);

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        Timer clockTimer = new Timer(1000, e -> clockLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        clockTimer.start();

        return header;
    }

    private JPanel buildContentArea() {
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setOpaque(false);

        contentArea.add(buildDashboardPanel(),    PANEL_DASHBOARD);
        contentArea.add(buildExchangePanel(),     PANEL_EXCHANGE);
        contentArea.add(buildTransactionsPanel(), PANEL_TRANSACTIONS);
        contentArea.add(buildCurrenciesPanel(),   PANEL_CURRENCIES);
        contentArea.add(buildSearchPanel(),       PANEL_SEARCH);
        contentArea.add(buildReportsPanel(),      PANEL_REPORTS);

        cardLayout.show(contentArea, PANEL_DASHBOARD);
        return contentArea;
    }

    // =========================================================
    // ██████████  DASHBOARD PANEL  ██████████
    // =========================================================
    private JPanel buildDashboardPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        wrapper.add(buildQuickActionsToolbar());
        wrapper.add(Box.createVerticalStrut(14));

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));

        statTotalTxnsVal        = LightTheme.label("0", LightTheme.FONT_STAT_BIG, LightTheme.DEEP_NAVY);
        statTotalExchangedVal   = LightTheme.label("₹0", LightTheme.FONT_STAT_BIG, LightTheme.DEEP_NAVY);
        statTxnsTodayVal        = LightTheme.label("0", LightTheme.FONT_STAT_BIG, LightTheme.BUY_GREEN);
        statActiveCurrenciesVal = LightTheme.label("4", LightTheme.FONT_STAT_BIG, LightTheme.DEEP_NAVY);

        statsRow.add(buildStatCard("TOTAL TRANSACTIONS", statTotalTxnsVal,        "+0 today",             "≡"));
        statsRow.add(buildStatCard("TOTAL EXCHANGED",    statTotalExchangedVal,   "Across all exchanges", "₹"));
        statsRow.add(buildStatCard("TODAY'S ACTIVITY",   statTxnsTodayVal,        "Transactions today",   "⊞"));
        statsRow.add(buildStatCard("ACTIVE CURRENCIES",  statActiveCurrenciesVal, "USD EUR GBP JPY",      "◎"));

        wrapper.add(statsRow);
        wrapper.add(Box.createVerticalStrut(14));

        wrapper.add(buildMarketRatesPanel());
        wrapper.add(Box.createVerticalStrut(14));

        JPanel lowerSplit = new JPanel(new GridBagLayout());
        lowerSplit.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.BOTH;
        gc.weighty = 1.0;

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.60;
        lowerSplit.add(buildRecentTransactionsPanel(), gc);

        gc.gridx = 1; gc.gridy = 0; gc.weightx = 0.02;
        lowerSplit.add(Box.createHorizontalStrut(12), gc);

        gc.gridx = 2; gc.gridy = 0; gc.weightx = 0.38;
        lowerSplit.add(buildMarketOverviewPanel(), gc);

        wrapper.add(lowerSplit);

        JScrollPane sp = LightTheme.lightScroll(wrapper);
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.add(sp, BorderLayout.CENTER);
        return outer;
    }

    /**
     * Card-Integrated Quick Actions Toolbar
     */
    private JPanel buildQuickActionsToolbar() {
        LightCard bar = new LightCard();
        bar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JButton newExchBtn = LightTheme.primaryButton("+ New Exchange");
        JButton viewTxnBtn = LightTheme.secondaryButton("View Transactions");
        JButton viewRptBtn = LightTheme.ghostButton("View Reports");
        JButton refreshBtn = LightTheme.ghostButton("↺ Refresh Rates");

        newExchBtn.addActionListener(e -> { setNavActive(1); cardLayout.show(contentArea, PANEL_EXCHANGE); });
        viewTxnBtn.addActionListener(e -> {
            setNavActive(2);
            cardLayout.show(contentArea, PANEL_TRANSACTIONS);
            refreshTable(transactionTable, tableModel);
        });
        viewRptBtn.addActionListener(e -> { setNavActive(5); cardLayout.show(contentArea, PANEL_REPORTS); });
        refreshBtn.addActionListener(e -> {
            refreshDashboard();
            showInfo("Refreshed", "Market rates and metrics refreshed.");
        });

        left.add(newExchBtn);
        left.add(viewTxnBtn);
        left.add(viewRptBtn);
        left.add(refreshBtn);

        bar.add(left, BorderLayout.WEST);
        return bar;
    }

    private JPanel buildStatCard(String title, JLabel valueLbl, String sub, String icon) {
        LightCard card = new LightCard();
        card.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        JLabel titleLbl = LightTheme.label(title, LightTheme.FONT_SMALL, LightTheme.SLATE_GRAY);
        JLabel iconLbl  = LightTheme.label(icon,  LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);
        topRow.add(titleLbl, BorderLayout.WEST);
        topRow.add(iconLbl,  BorderLayout.EAST);

        JLabel subLbl = LightTheme.label(sub, LightTheme.FONT_SMALL, LightTheme.SLATE_GRAY);

        inner.add(topRow,   BorderLayout.NORTH);
        inner.add(valueLbl, BorderLayout.CENTER);
        inner.add(subLbl,   BorderLayout.SOUTH);

        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildMarketRatesPanel() {
        LightCard panel = new LightCard();
        panel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        JLabel title = LightTheme.label("Live Market Exchange Rates", LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);
        JLabel sub   = LightTheme.label("Current Buy / Sell rates vs INR", LightTheme.FONT_SMALL, LightTheme.SLATE_GRAY);
        header.add(title, BorderLayout.WEST);
        header.add(sub,   BorderLayout.EAST);

        String[] cols = {"Currency", "Symbol", "Buy Rate (Customer Sells)", "Sell Rate (Customer Buys)", "Spread", "Status"};
        marketRatesModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        marketRatesTable = new JTable(marketRatesModel);
        styleTable(marketRatesTable, 2, 3, 5);

        JScrollPane sp = LightTheme.lightScroll(marketRatesTable);
        sp.setPreferredSize(new Dimension(0, 110));

        panel.add(header, BorderLayout.NORTH);
        panel.add(sp,     BorderLayout.CENTER);
        return panel;
    }

    private void refreshMarketRatesTable() {
        if (marketRatesModel == null) return;
        marketRatesModel.setRowCount(0);

        Map<String, String> symbols = Map.of("USD", "$", "EUR", "€", "GBP", "£", "JPY", "¥");

        for (Currency c : system.getCurrencies()) {
            ExchangeRate r = system.getExchangeRate(c.getCode());
            if (r != null) {
                BigDecimal buy    = r.getBuyRate();
                BigDecimal sell   = r.getSellRate();
                BigDecimal spread = sell.subtract(buy);

                marketRatesModel.addRow(new Object[]{
                    c.getCode() + " — " + c.getName(),
                    symbols.getOrDefault(c.getCode(), ""),
                    "₹ " + buy.toPlainString(),
                    "₹ " + sell.toPlainString(),
                    "₹ " + spread.setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    "ACTIVE"
                });
            }
        }
    }

    private JPanel buildRecentTransactionsPanel() {
        LightCard panel = new LightCard();
        panel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        JLabel title = LightTheme.label("Recent Transactions", LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);
        JButton viewAll = LightTheme.ghostButton("View All ➔");
        viewAll.addActionListener(e -> { setNavActive(2); cardLayout.show(contentArea, PANEL_TRANSACTIONS); });
        header.add(title,   BorderLayout.WEST);
        header.add(viewAll, BorderLayout.EAST);

        String[] cols = {"Txn ID", "Date", "Customer", "Pair", "Amount", "Converted (₹)", "Status"};
        dashboardTxnModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        dashboardTxnTable = new JTable(dashboardTxnModel);
        styleTable(dashboardTxnTable, -1, -1, 6);

        recentTxnContainer = new JPanel(new BorderLayout());
        recentTxnContainer.setOpaque(false);

        panel.add(header,             BorderLayout.NORTH);
        panel.add(recentTxnContainer, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildEmptyStatePanel() {
        JPanel empty = new JPanel();
        empty.setOpaque(false);
        empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
        empty.setBorder(BorderFactory.createEmptyBorder(20, 14, 20, 14));

        JLabel icon = LightTheme.label("⇄", LightTheme.sansFont(Font.BOLD, 30), LightTheme.SLATE_GRAY);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = LightTheme.label("No transactions recorded yet", LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = LightTheme.label("Execute your first currency exchange transaction to populate metrics.", LightTheme.FONT_BODY, LightTheme.SLATE_GRAY);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = LightTheme.primaryButton("+ Execute Exchange");
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> { setNavActive(1); cardLayout.show(contentArea, PANEL_EXCHANGE); });

        empty.add(icon);
        empty.add(Box.createVerticalStrut(6));
        empty.add(title);
        empty.add(Box.createVerticalStrut(4));
        empty.add(sub);
        empty.add(Box.createVerticalStrut(14));
        empty.add(startBtn);

        return empty;
    }

    private JPanel buildMarketOverviewPanel() {
        LightCard panel = new LightCard();
        panel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel title = LightTheme.label("Currency Volume & Analytics", LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);
        panel.add(title, BorderLayout.NORTH);

        analyticsContainer = new JPanel();
        analyticsContainer.setOpaque(false);
        analyticsContainer.setLayout(new BoxLayout(analyticsContainer, BoxLayout.Y_AXIS));

        panel.add(analyticsContainer, BorderLayout.CENTER);
        return panel;
    }

    private void refreshDashboardAnalytics() {
        if (analyticsContainer == null) return;
        analyticsContainer.removeAll();

        LinkedList<Transaction> txns = system.getTransactionHistory();

        Map<String, Integer> countMap = new HashMap<>();
        Map<String, BigDecimal> volumeMap = new HashMap<>();

        for (Transaction t : txns) {
            String code = t.getCurrencyCode();
            countMap.put(code, countMap.getOrDefault(code, 0) + 1);
            volumeMap.put(code, volumeMap.getOrDefault(code, BigDecimal.ZERO).add(t.getConvertedAmount()));
        }

        JPanel statsGrid = new JPanel(new GridLayout(4, 1, 0, 6));
        statsGrid.setOpaque(false);
        statsGrid.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        String[] codes = {"USD", "EUR", "GBP", "JPY"};
        for (String code : codes) {
            int cnt = countMap.getOrDefault(code, 0);
            BigDecimal vol = volumeMap.getOrDefault(code, BigDecimal.ZERO);

            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            row.setBorder(BorderFactory.createCompoundBorder(
                new LightBorder(LightTheme.BORDER_SUBTLE, 4, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));

            JLabel codeLbl = LightTheme.label(code, LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);
            JLabel detail  = LightTheme.label(cnt + " txns  │  ₹ " + formatAmount(vol), LightTheme.FONT_BODY, LightTheme.SLATE_GRAY);

            row.add(codeLbl, BorderLayout.WEST);
            row.add(detail,  BorderLayout.EAST);
            statsGrid.add(row);
        }

        analyticsContainer.add(statsGrid);

        JPanel footerNote = new JPanel(new BorderLayout());
        footerNote.setOpaque(false);
        footerNote.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        footerNote.add(LightTheme.label("Currencies Available: " + system.getCurrencies().size(), LightTheme.FONT_SMALL, LightTheme.SLATE_GRAY), BorderLayout.WEST);
        footerNote.add(LightTheme.label("Status: Operational", LightTheme.FONT_SMALL, LightTheme.BUY_GREEN), BorderLayout.EAST);

        analyticsContainer.add(footerNote);
        analyticsContainer.revalidate();
        analyticsContainer.repaint();
    }

    private void refreshDashboard() {
        if (statTotalTxnsVal == null) return;

        LinkedList<Transaction> history = system.getTransactionHistory();
        int totalTxns = history.size();

        BigDecimal totalExchanged = BigDecimal.ZERO;
        LocalDate today = LocalDate.now();
        int todayCount = 0;

        for (Transaction t : history) {
            totalExchanged = totalExchanged.add(t.getConvertedAmount());
            if (t.getDateTime().toLocalDate().equals(today)) todayCount++;
        }

        statTotalTxnsVal.setText(String.valueOf(totalTxns));
        statTotalExchangedVal.setText("₹ " + formatAmount(totalExchanged));
        statTxnsTodayVal.setText(String.valueOf(todayCount));
        statActiveCurrenciesVal.setText(String.valueOf(system.getCurrencies().size()));

        refreshMarketRatesTable();

        recentTxnContainer.removeAll();
        if (history.isEmpty()) {
            recentTxnContainer.add(buildEmptyStatePanel(), BorderLayout.CENTER);
        } else {
            dashboardTxnModel.setRowCount(0);
            int count = 0;
            for (Transaction t : history) {
                if (count++ >= 5) break;
                dashboardTxnModel.addRow(new Object[]{
                    "TXN" + String.format("%03d", t.getTransactionId()),
                    t.getDateTime().format(DateTimeFormatter.ofPattern("dd MMM HH:mm")),
                    t.getCustomer().getName(),
                    t.getCurrencyCode() + " → INR",
                    t.getForeignAmount().toPlainString() + " " + t.getCurrencyCode(),
                    "₹ " + formatAmount(t.getConvertedAmount()),
                    "COMPLETED"
                });
            }
            JScrollPane sp = LightTheme.lightScroll(dashboardTxnTable);
            sp.setPreferredSize(new Dimension(0, 160));
            recentTxnContainer.add(sp, BorderLayout.CENTER);
        }
        recentTxnContainer.revalidate();
        recentTxnContainer.repaint();

        refreshDashboardAnalytics();
    }

    // =========================================================
    // ██████████  EXCHANGE PANEL  ██████████
    // =========================================================
    private JPanel buildExchangePanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        wrapper.add(sectionHeading("New Currency Exchange", "Execute instant buy and sell FX transactions"));
        wrapper.add(Box.createVerticalStrut(14));

        JPanel splitRow = new JPanel(new GridBagLayout());
        splitRow.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.BOTH;
        gc.weighty = 1.0;

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.60;
        splitRow.add(buildExchangeFormCard(), gc);

        gc.gridx = 1; gc.gridy = 0; gc.weightx = 0.02;
        splitRow.add(Box.createHorizontalStrut(16), gc);

        gc.gridx = 2; gc.gridy = 0; gc.weightx = 0.38;
        splitRow.add(buildExchangeResultCard(), gc);

        wrapper.add(splitRow);

        outer.add(LightTheme.lightScroll(wrapper), BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildExchangeFormCard() {
        LightCard card = new LightCard();
        card.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.insets  = new Insets(8, 0, 8, 0);
        gc.weightx = 1.0;

        customerNameField = LightTheme.inputField();
        phoneField        = LightTheme.inputField();
        amountField       = LightTheme.inputField();

        rateField = LightTheme.inputField();
        rateField.setEditable(false);
        rateField.setForeground(LightTheme.SLATE_GRAY);

        currencyComboBox = LightTheme.comboBox(new String[]{"USD", "EUR", "GBP", "JPY"});

        int r = 0;
        addFormRow(form, gc, r++, "Customer Name", customerNameField);
        addFormRow(form, gc, r++, "Phone Number",  phoneField);

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 1; form.add(formLabel("Currency Pair"), gc);
        gc.gridx = 1; gc.gridy = r; gc.gridwidth = 3; form.add(currencyComboBox, gc); r++;

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 1; form.add(formLabel("Transaction Mode"), gc);
        gc.gridx = 1; gc.gridy = r; gc.gridwidth = 3; form.add(buildBuySellToggle(), gc); r++;

        addFormRow(form, gc, r++, "Foreign Amount", amountField);
        addFormRow(form, gc, r++, "Applied Rate",   rateField);

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 4;
        gc.insets = new Insets(16, 0, 0, 0);
        form.add(buildExchangeButtons(), gc);

        card.add(form, BorderLayout.NORTH);

        currencyComboBox.addActionListener(e -> updateExchangeRate());
        return card;
    }

    private JPanel buildBuySellToggle() {
        JPanel toggle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toggle.setOpaque(false);

        buyBtnToggle  = buildTypeButton("BUY (Customer Sells Foreign)",  true);
        sellBtnToggle = buildTypeButton("SELL (Customer Buys Foreign)", false);

        buyBtnToggle.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                selectedType = "BUY";
                refreshTypeButtons();
                updateExchangeRate();
            }
        });
        sellBtnToggle.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                selectedType = "SELL";
                refreshTypeButtons();
                updateExchangeRate();
            }
        });

        toggle.add(buyBtnToggle);
        toggle.add(Box.createHorizontalStrut(8));
        toggle.add(sellBtnToggle);
        return toggle;
    }

    private JLabel buildTypeButton(String text, boolean active) {
        JLabel lbl = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isBuy = text.startsWith("BUY");
                boolean sel   = (isBuy && "BUY".equals(selectedType)) || (!isBuy && "SELL".equals(selectedType));
                if (sel) {
                    g2.setColor(isBuy ? LightTheme.BUY_GREEN : LightTheme.SELL_RED);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                } else {
                    g2.setColor(LightTheme.CARD_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.setColor(LightTheme.BORDER_SUBTLE);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boolean isBuy = text.startsWith("BUY");
        boolean sel   = (isBuy && "BUY".equals(selectedType)) || (!isBuy && "SELL".equals(selectedType));
        lbl.setFont(LightTheme.FONT_BODY);
        lbl.setForeground(sel ? Color.WHITE : (isBuy ? LightTheme.BUY_GREEN : LightTheme.SELL_RED));
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl.setOpaque(false);
        return lbl;
    }

    private void refreshTypeButtons() {
        if (buyBtnToggle  != null) {
            boolean sel = "BUY".equals(selectedType);
            buyBtnToggle.setForeground(sel ? Color.WHITE : LightTheme.BUY_GREEN);
            buyBtnToggle.repaint();
        }
        if (sellBtnToggle != null) {
            boolean sel = "SELL".equals(selectedType);
            sellBtnToggle.setForeground(sel ? Color.WHITE : LightTheme.SELL_RED);
            sellBtnToggle.repaint();
        }
    }

    private JPanel buildExchangeButtons() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        JButton calcBtn    = LightTheme.ghostButton("⊞ CALCULATE");
        JButton processBtn = LightTheme.primaryButton("▶ PROCESS TRANSACTION");
        JButton clearBtn   = LightTheme.secondaryButton("↺ Clear");

        calcBtn.addActionListener(e -> calculateAmount());
        processBtn.addActionListener(e -> processTransaction());
        clearBtn.addActionListener(e -> clearFields());

        row.add(calcBtn);
        row.add(processBtn);
        row.add(clearBtn);
        return row;
    }

    private JPanel buildExchangeResultCard() {
        LightCard card = new LightCard();
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel heading = LightTheme.label("CONVERTED AMOUNT (INR)", LightTheme.FONT_SUBHEADING, LightTheme.SLATE_GRAY);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        convertedAmountField = new JTextField("₹ 0.00") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(LightTheme.CARD_ALT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(LightTheme.WARM_GOLD);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        convertedAmountField.setFont(LightTheme.FONT_STAT_BIG);
        convertedAmountField.setForeground(LightTheme.DEEP_NAVY);
        convertedAmountField.setBackground(LightTheme.CARD_ALT_BG);
        convertedAmountField.setEditable(false);
        convertedAmountField.setHorizontalAlignment(JTextField.CENTER);
        convertedAmountField.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        convertedAmountField.setOpaque(false);
        convertedAmountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertedAmountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel details = new JPanel(new GridLayout(4, 2, 8, 8));
        details.setOpaque(false);
        details.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        details.add(LightTheme.label("Base Currency", LightTheme.FONT_BODY, LightTheme.SLATE_GRAY));
        details.add(LightTheme.label("INR (Indian Rupee)", LightTheme.FONT_BODY, LightTheme.DEEP_NAVY));

        details.add(LightTheme.label("Foreign Currency", LightTheme.FONT_BODY, LightTheme.SLATE_GRAY));
        details.add(LightTheme.label("USD / EUR / GBP / JPY", LightTheme.FONT_BODY, LightTheme.DEEP_NAVY));

        details.add(LightTheme.label("Rate Applied", LightTheme.FONT_BODY, LightTheme.SLATE_GRAY));
        details.add(LightTheme.label("Live Market Rate", LightTheme.FONT_BODY, LightTheme.DEEP_NAVY));

        details.add(LightTheme.label("Status", LightTheme.FONT_BODY, LightTheme.SLATE_GRAY));
        details.add(LightTheme.label("READY", LightTheme.FONT_BODY, LightTheme.BUY_GREEN));

        inner.add(heading);
        inner.add(Box.createVerticalStrut(12));
        inner.add(convertedAmountField);
        inner.add(details);

        card.add(inner, BorderLayout.NORTH);
        return card;
    }

    // =========================================================
    // ██████████  TRANSACTIONS PANEL  ██████████
    // =========================================================
    private JPanel buildTransactionsPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setOpaque(false);
        outer.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(sectionHeading("Transactions Log", "Full historical record of all processed currency conversions"), BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        JButton sortAmt  = LightTheme.ghostButton("Sort by Amount");
        JButton sortDate = LightTheme.ghostButton("Sort by Date");
        JButton refreshB = LightTheme.secondaryButton("↺ Refresh");
        JButton deleteB  = LightTheme.ghostButton("✕ Delete");

        sortAmt.addActionListener(e  -> populateTable(system.sortByAmount(),  tableModel));
        sortDate.addActionListener(e -> populateTable(system.sortByDate(),    tableModel));
        refreshB.addActionListener(e -> refreshTable(transactionTable, tableModel));
        deleteB.addActionListener(e  -> deleteSelectedTransaction());

        btns.add(sortAmt);
        btns.add(sortDate);
        btns.add(deleteB);
        btns.add(refreshB);
        header.add(btns, BorderLayout.EAST);
        outer.add(header, BorderLayout.NORTH);

        String[] cols = {"ID", "Customer", "Phone", "Pair", "Type", "Foreign Amount", "Rate", "Converted (₹)", "Date & Time"};
        tableModel       = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        transactionTable = new JTable(tableModel);
        styleTable(transactionTable, 4, -1, -1);

        LightCard card = new LightCard();
        card.add(LightTheme.lightScroll(transactionTable), BorderLayout.CENTER);

        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    private void deleteSelectedTransaction() {
        int row = transactionTable.getSelectedRow();
        if (row < 0) {
            showError("No Selection", "Please select a transaction row to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete Transaction #" + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = system.deleteTransaction(id);
            if (deleted) {
                refreshTable(transactionTable, tableModel);
                refreshDashboard();
                showInfo("Deleted", "Transaction #" + id + " removed successfully.");
            } else {
                showError("Not Found", "Transaction #" + id + " could not be found.");
            }
        }
    }

    // =========================================================
    // ██████████  CURRENCIES & RATES PANEL  ██████████
    // =========================================================
    private JPanel buildCurrenciesPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 14));
        outer.setOpaque(false);
        outer.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        outer.add(sectionHeading("Currencies & Exchange Rates", "Manage supported FX currencies and live market rates"), BorderLayout.NORTH);

        JPanel cols = new JPanel(new GridLayout(1, 2, 14, 0));
        cols.setOpaque(false);

        cols.add(buildCurrencyManagementCard());
        cols.add(buildRateManagementCard());

        outer.add(cols, BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildCurrencyManagementCard() {
        LightCard card = new LightCard();
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = LightTheme.label("Currencies List", LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);

        String[] cols = {"Code", "Currency Name", "Status"};
        currencyTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        currencyTable = new JTable(currencyTableModel);
        styleTable(currencyTable, -1, -1, 2);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setOpaque(false);
        JButton addBtn    = LightTheme.secondaryButton("+ Add Currency");
        JButton updateBtn = LightTheme.ghostButton("✎ Edit Name");
        JButton delBtn    = LightTheme.ghostButton("✕ Delete");

        addBtn.addActionListener(e    -> addCurrencyDialog());
        updateBtn.addActionListener(e -> updateCurrencyDialog());
        delBtn.addActionListener(e    -> deleteCurrencyDialog());

        btns.add(addBtn);
        btns.add(updateBtn);
        btns.add(delBtn);

        card.add(title,                                      BorderLayout.NORTH);
        card.add(LightTheme.lightScroll(currencyTable),     BorderLayout.CENTER);
        card.add(btns,                                       BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildRateManagementCard() {
        LightCard card = new LightCard();
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = LightTheme.label("Exchange Rates (INR)", LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);

        String[] cols = {"Code", "Buy Rate (₹)", "Sell Rate (₹)", "Spread (₹)"};
        rateTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        rateTable = new JTable(rateTableModel);
        styleTable(rateTable, 1, 2, -1);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setOpaque(false);
        JButton addBtn    = LightTheme.secondaryButton("+ Add Rate");
        JButton updateBtn = LightTheme.ghostButton("✎ Edit Rate");
        JButton delBtn    = LightTheme.ghostButton("✕ Delete");

        addBtn.addActionListener(e    -> addRateDialog());
        updateBtn.addActionListener(e -> updateRateDialog());
        delBtn.addActionListener(e    -> deleteRateDialog());

        btns.add(addBtn);
        btns.add(updateBtn);
        btns.add(delBtn);

        card.add(title,                                  BorderLayout.NORTH);
        card.add(LightTheme.lightScroll(rateTable),     BorderLayout.CENTER);
        card.add(btns,                                   BorderLayout.SOUTH);
        return card;
    }

    private void refreshCurrencyTables() {
        if (currencyTableModel == null || rateTableModel == null) return;

        currencyTableModel.setRowCount(0);
        for (Currency c : system.getCurrencies()) {
            currencyTableModel.addRow(new Object[]{c.getCode(), c.getName(), "ACTIVE"});
        }

        rateTableModel.setRowCount(0);
        for (Currency c : system.getCurrencies()) {
            ExchangeRate r = system.getExchangeRate(c.getCode());
            if (r != null) {
                BigDecimal buy    = r.getBuyRate();
                BigDecimal sell   = r.getSellRate();
                BigDecimal spread = sell.subtract(buy);
                rateTableModel.addRow(new Object[]{
                    r.getCurrencyCode(),
                    buy.toPlainString(),
                    sell.toPlainString(),
                    spread.setScale(2, RoundingMode.HALF_UP).toPlainString()
                });
            }
        }
    }

    // --- Currency Dialogs ---
    private void addCurrencyDialog() {
        JTextField codeFld = LightTheme.inputField();
        JTextField nameFld = LightTheme.inputField();
        JPanel form = dialogForm(new String[]{"Currency Code (3 chars)", "Currency Name"}, new JTextField[]{codeFld, nameFld});
        if (styledDialog("Add Currency", form) == JOptionPane.OK_OPTION) {
            try {
                system.addCurrency(new Currency(codeFld.getText().trim(), nameFld.getText().trim()));
                refreshCurrencyTables();
                refreshDashboard();
                showInfo("Added", "Currency " + codeFld.getText().trim().toUpperCase() + " added successfully.");
            } catch (Exception ex) {
                showError("Error", ex.getMessage());
            }
        }
    }

    private void updateCurrencyDialog() {
        int row = currencyTable.getSelectedRow();
        if (row < 0) { showError("No Selection", "Please select a currency row."); return; }
        String code = (String) currencyTableModel.getValueAt(row, 0);
        JTextField nameFld = LightTheme.inputField();
        nameFld.setText((String) currencyTableModel.getValueAt(row, 1));
        if (styledDialog("Edit Currency — " + code, dialogForm(new String[]{"New Name"}, new JTextField[]{nameFld})) == JOptionPane.OK_OPTION) {
            system.updateCurrency(code, nameFld.getText().trim());
            refreshCurrencyTables();
            showInfo("Updated", code + " name updated.");
        }
    }

    private void deleteCurrencyDialog() {
        int row = currencyTable.getSelectedRow();
        if (row < 0) { showError("No Selection", "Please select a currency row."); return; }
        String code = (String) currencyTableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete currency " + code + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            system.deleteCurrency(code);
            refreshCurrencyTables();
            refreshDashboard();
            showInfo("Deleted", code + " removed.");
        }
    }

    // --- Rate Dialogs ---
    private void addRateDialog() {
        JTextField codeFld = LightTheme.inputField();
        JTextField buyFld  = LightTheme.inputField();
        JTextField sellFld = LightTheme.inputField();
        JPanel form = dialogForm(new String[]{"Currency Code", "Buy Rate (₹)", "Sell Rate (₹)"}, new JTextField[]{codeFld, buyFld, sellFld});
        if (styledDialog("Add Exchange Rate", form) == JOptionPane.OK_OPTION) {
            try {
                system.addExchangeRate(new ExchangeRate(
                    codeFld.getText().trim(),
                    new BigDecimal(buyFld.getText().trim()),
                    new BigDecimal(sellFld.getText().trim())
                ));
                refreshCurrencyTables();
                showInfo("Added", "Rate added for " + codeFld.getText().trim().toUpperCase());
            } catch (Exception ex) {
                showError("Error", ex.getMessage());
            }
        }
    }

    private void updateRateDialog() {
        int row = rateTable.getSelectedRow();
        if (row < 0) { showError("No Selection", "Please select a rate row."); return; }
        String code    = (String) rateTableModel.getValueAt(row, 0);
        JTextField buyFld  = LightTheme.inputField();
        JTextField sellFld = LightTheme.inputField();
        buyFld.setText((String) rateTableModel.getValueAt(row, 1));
        sellFld.setText((String) rateTableModel.getValueAt(row, 2));
        if (styledDialog("Edit Rates — " + code, dialogForm(new String[]{"New Buy Rate", "New Sell Rate"}, new JTextField[]{buyFld, sellFld})) == JOptionPane.OK_OPTION) {
            try {
                system.updateExchangeRate(code, new BigDecimal(buyFld.getText().trim()), new BigDecimal(sellFld.getText().trim()));
                refreshCurrencyTables();
                showInfo("Updated", "Rates for " + code + " updated.");
            } catch (Exception ex) {
                showError("Error", ex.getMessage());
            }
        }
    }

    private void deleteRateDialog() {
        int row = rateTable.getSelectedRow();
        if (row < 0) { showError("No Selection", "Please select a rate row."); return; }
        String code = (String) rateTableModel.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Delete rates for " + code + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            system.deleteExchangeRate(code);
            refreshCurrencyTables();
            showInfo("Deleted", "Rate for " + code + " removed.");
        }
    }

    // =========================================================
    // ██████████  SEARCH PANEL  ██████████
    // =========================================================
    private JPanel buildSearchPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setOpaque(false);
        outer.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        outer.add(sectionHeading("Transaction Search", "Filter transactions by currency code or customer name"), BorderLayout.NORTH);

        LightCard searchCard = new LightCard();
        searchCard.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel searchRow = new JPanel(new BorderLayout(12, 0));
        searchRow.setOpaque(false);

        searchField = LightTheme.inputField();

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        JButton byCurr  = LightTheme.secondaryButton("Search Currency");
        JButton byCust  = LightTheme.secondaryButton("Search Customer");
        JButton showAll = LightTheme.ghostButton("Show All");

        byCurr.addActionListener(e  -> searchByCurrency());
        byCust.addActionListener(e  -> searchByCustomer());
        showAll.addActionListener(e -> populateTable(system.getTransactionHistory(), searchTableModel));

        btns.add(byCurr);
        btns.add(byCust);
        btns.add(showAll);

        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(btns,        BorderLayout.EAST);
        searchCard.add(searchRow,  BorderLayout.CENTER);

        String[] cols = {"ID", "Customer", "Phone", "Pair", "Type", "Foreign Amount", "Rate", "Converted (₹)", "Date & Time"};
        searchTableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        searchTable = new JTable(searchTableModel);
        styleTable(searchTable, 4, -1, -1);

        LightCard tableCard = new LightCard();
        tableCard.add(LightTheme.lightScroll(searchTable), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);
        center.add(searchCard, BorderLayout.NORTH);
        center.add(tableCard,  BorderLayout.CENTER);

        outer.add(center, BorderLayout.CENTER);
        searchField.addActionListener(e -> searchByCurrency());
        return outer;
    }

    // =========================================================
    // ██████████  REPORTS PANEL  ██████████
    // =========================================================
    private JPanel buildReportsPanel() {
        JPanel outer = new JPanel();
        outer.setOpaque(false);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        outer.add(sectionHeading("Reports & Analytics", "View comprehensive exchange reports and daily summaries"));
        outer.add(Box.createVerticalStrut(14));

        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);

        row.add(buildReportCard("Exchange System Report", true));
        row.add(buildReportCard("Today's Daily Summary", false));

        outer.add(row);
        outer.add(Box.createVerticalGlue());

        JScrollPane sp = LightTheme.lightScroll(outer);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(sp, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel buildReportCard(String titleText, boolean isFullReport) {
        LightCard card = new LightCard();
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel title = LightTheme.label(titleText, LightTheme.FONT_HEADING, LightTheme.DEEP_NAVY);

        JTextArea area = new JTextArea(12, 30);
        area.setFont(LightTheme.FONT_MONO);
        area.setForeground(LightTheme.DEEP_NAVY);
        area.setBackground(LightTheme.INPUT_BG);
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Runnable loadContent = () -> {
            String text = isFullReport ? system.getExchangeReport() : system.getDailySummary();
            area.setText(text.replace("=====", "─────"));
        };
        loadContent.run();

        JButton refreshBtn = LightTheme.secondaryButton("↺ Refresh " + (isFullReport ? "Report" : "Summary"));
        refreshBtn.addActionListener(e -> loadContent.run());

        inner.add(title);
        inner.add(Box.createVerticalStrut(10));
        inner.add(LightTheme.lightScroll(area));
        inner.add(Box.createVerticalStrut(10));
        inner.add(refreshBtn);

        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    // =========================================================
    // BUSINESS LOGIC INTEGRATION (Public API Only)
    // =========================================================
    private void updateExchangeRate() {
        if (currencyComboBox == null || rateField == null) return;
        String currency = (String) currencyComboBox.getSelectedItem();
        ExchangeRate rate = system.getExchangeRate(currency);
        if (rate != null) {
            BigDecimal selectedRate = "BUY".equals(selectedType) ? rate.getSellRate() : rate.getBuyRate();
            rateField.setText(selectedRate.toPlainString());
        } else {
            rateField.setText("");
        }
    }

    private void calculateAmount() {
        try {
            String amtText = amountField.getText().trim();
            if (amtText.isEmpty()) { showError("Missing Input", "Please enter a foreign currency amount."); return; }
            BigDecimal amount = new BigDecimal(amtText);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) { showError("Invalid Amount", "Amount must be greater than zero."); return; }
            String rateText = rateField.getText().trim();
            if (rateText.isEmpty()) { showError("No Rate", "Exchange rate not available."); return; }
            BigDecimal rate = new BigDecimal(rateText);
            BigDecimal converted = amount.multiply(rate);
            convertedAmountField.setText("₹ " + formatAmount(converted));
        } catch (NumberFormatException ex) {
            showError("Invalid Input", "Please enter a valid numeric amount.");
        }
    }

    private void processTransaction() {
        try {
            String name  = customerNameField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty())  { showError("Missing Input", "Please enter customer name."); return; }
            if (phone.isEmpty()) { showError("Missing Input", "Please enter phone number."); return; }

            String amtText = amountField.getText().trim();
            if (amtText.isEmpty()) { showError("Missing Input", "Please enter amount."); return; }

            BigDecimal amount = new BigDecimal(amtText);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) { showError("Invalid Amount", "Amount must be greater than zero."); return; }

            String currency = (String) currencyComboBox.getSelectedItem();
            Customer customer = new Customer(customerId++, name, phone);

            Transaction transaction = system.processTransaction(customer, currency, selectedType, amount);

            convertedAmountField.setText("₹ " + formatAmount(transaction.getConvertedAmount()));
            refreshTable(transactionTable, tableModel);
            refreshDashboard();

            showReceipt(transaction);
        } catch (InvalidTransactionException ex) {
            showError("Transaction Error", ex.getMessage());
        } catch (NumberFormatException ex) {
            showError("Invalid Amount", "Please enter a valid numeric amount.");
        } catch (Exception ex) {
            showError("Error", ex.getMessage());
        }
    }

    private void refreshTable(JTable table, DefaultTableModel model) {
        populateTable(system.getTransactionHistory(), model);
    }

    private void populateTable(Iterable<Transaction> transactions, DefaultTableModel model) {
        model.setRowCount(0);
        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                t.getTransactionId(),
                t.getCustomer().getName(),
                t.getCustomer().getPhone(),
                t.getCurrencyCode() + " / INR",
                t.getTransactionType(),
                t.getForeignAmount().toPlainString() + " " + t.getCurrencyCode(),
                t.getExchangeRate().toPlainString(),
                "₹ " + formatAmount(t.getConvertedAmount()),
                t.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"))
            });
        }
    }

    private void searchByCurrency() {
        String code = searchField.getText().trim();
        if (code.isEmpty()) { populateTable(system.getTransactionHistory(), searchTableModel); return; }
        LinkedList<Transaction> results = system.searchByCurrency(code);
        populateTable(results, searchTableModel);
        if (results.isEmpty()) showInfo("No Results", "No transactions found for currency: " + code.toUpperCase());
    }

    private void searchByCustomer() {
        String name = searchField.getText().trim();
        if (name.isEmpty()) { showError("Missing Input", "Please enter a customer name."); return; }
        LinkedList<Transaction> results = system.searchByCustomer(name);
        populateTable(results, searchTableModel);
        if (results.isEmpty()) showInfo("No Results", "No transactions found for customer: " + name);
    }

    private void clearFields() {
        customerNameField.setText("");
        phoneField.setText("");
        amountField.setText("");
        convertedAmountField.setText("₹ 0.00");
        if (searchField != null) searchField.setText("");
        currencyComboBox.setSelectedIndex(0);
        selectedType = "BUY";
        refreshTypeButtons();
        updateExchangeRate();
    }

    // =========================================================
    // UTILITIES & HELPERS
    // =========================================================
    private void styleTable(JTable table, int buyCol, int sellCol, int statusCol) {
        table.setBackground(LightTheme.CARD_BG);
        table.setForeground(LightTheme.DEEP_NAVY);
        table.setFont(LightTheme.FONT_BODY);
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(LightTheme.WARM_GOLD);
        table.setSelectionForeground(LightTheme.DEEP_NAVY);
        table.setFocusable(false);
        table.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(LightTheme.STEEL_BLUE);
        header.setForeground(Color.WHITE);
        header.setFont(LightTheme.sansFont(Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 34));

        LightTableRenderer renderer = new LightTableRenderer(buyCol >= 0 ? buyCol : sellCol, statusCol);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private JPanel sectionHeading(String titleText, String subText) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel title = LightTheme.label(titleText, LightTheme.FONT_TITLE, LightTheme.DEEP_NAVY);
        p.add(title);
        if (subText != null && !subText.isEmpty()) {
            JLabel sub = LightTheme.label(subText, LightTheme.FONT_BODY, LightTheme.SLATE_GRAY);
            p.add(Box.createVerticalStrut(2));
            p.add(sub);
        }
        return p;
    }

    private JLabel formLabel(String text) {
        return LightTheme.label(text, LightTheme.FONT_BODY, LightTheme.DEEP_NAVY);
    }

    private void addFormRow(JPanel form, GridBagConstraints gc, int r, String label, JComponent field) {
        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 1; gc.weightx = 0.0;
        gc.insets = new Insets(8, 0, 6, 10);
        form.add(formLabel(label), gc);

        gc.gridx = 1; gc.gridy = r; gc.gridwidth = 3; gc.weightx = 1.0;
        gc.insets = new Insets(8, 0, 6, 0);
        form.add(field, gc);
    }

    private void showInfo(String title, String msg) {
        JOptionPane.showMessageDialog(this, styledMsg(msg, LightTheme.DEEP_NAVY), title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String title, String msg) {
        JOptionPane.showMessageDialog(this, styledMsg(msg, LightTheme.SELL_RED), title, JOptionPane.ERROR_MESSAGE);
    }

    private void showReceipt(Transaction t) {
        JTextArea area = new JTextArea(t.generateReceipt(), 12, 34);
        area.setFont(LightTheme.FONT_MONO);
        area.setForeground(LightTheme.DEEP_NAVY);
        area.setBackground(LightTheme.INPUT_BG);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, LightTheme.lightScroll(area), "✓ Transaction Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private JLabel styledMsg(String msg, Color color) {
        JLabel l = new JLabel("<html><body style='width:280px;padding:4px'>" + msg.replace("\n", "<br>") + "</body></html>");
        l.setFont(LightTheme.FONT_BODY);
        l.setForeground(color);
        return l;
    }

    private int styledDialog(String title, JPanel content) {
        return JOptionPane.showConfirmDialog(this, content, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel dialogForm(String[] labels, JTextField[] fields) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 4, 6, 4);
        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
            panel.add(formLabel(labels[i]), gc);
            gc.gridx = 1; gc.weightx = 1.0;
            panel.add(fields[i], gc);
        }
        return panel;
    }

    private String formatAmount(BigDecimal amt) {
        if (amt == null) return "0.00";
        return String.format("%,.2f", amt);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CurrencyExchangeGUI gui = new CurrencyExchangeGUI();
            gui.setVisible(true);
        });
    }
}