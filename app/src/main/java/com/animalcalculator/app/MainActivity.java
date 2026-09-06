package com.animalcalculator.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends Activity {
    private TextView display;
    private BigDecimal first = null;
    private String operator = null;
    private boolean startNew = true;
    private boolean error = false;

    private final int CREAM = Color.rgb(255,247,232);
    private final int FOREST = Color.rgb(36,74,59);
    private final int LEAF = Color.rgb(111,155,114);
    private final int SAND = Color.rgb(233,201,138);
    private final int PEACH = Color.rgb(233,167,125);
    private final int SKY = Color.rgb(156,199,216);
    private final int BROWN = Color.rgb(109,85,72);

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(FOREST);
        buildUi();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(14), dp(16), dp(18));
        root.setBackgroundColor(CREAM);

        TextView title = new TextView(this);
        title.setText("🐾  Animal Calculator  🐾");
        title.setTextColor(FOREST);
        title.setTextSize(26);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(54)));

        TextView subtitle = new TextView(this);
        subtitle.setText("Count with the whole zoo!");
        subtitle.setTextColor(BROWN);
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle, new LinearLayout.LayoutParams(-1, dp(34)));

        display = new TextView(this);
        display.setText("0");
        display.setTextColor(FOREST);
        display.setTextSize(38);
        display.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        display.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        display.setBackgroundResource(R.drawable.bg_display);
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(-1, dp(100));
        dlp.setMargins(0, dp(8), 0, dp(16));
        root.addView(display, dlp);

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setRowCount(5);
        grid.setUseDefaultMargins(false);
        root.addView(grid, new LinearLayout.LayoutParams(-1, 0, 1f));

        add(grid, "🦁\nC", PEACH, "C");
        add(grid, "🐼\n±", SAND, "±");
        add(grid, "🐒\n%", SKY, "%");
        add(grid, "🦊\n÷", LEAF, "÷");
        add(grid, "🐶\n7", Color.WHITE, "7");
        add(grid, "🐱\n8", Color.WHITE, "8");
        add(grid, "🐰\n9", Color.WHITE, "9");
        add(grid, "🐯\n×", LEAF, "×");
        add(grid, "🐸\n4", Color.WHITE, "4");
        add(grid, "🐨\n5", Color.WHITE, "5");
        add(grid, "🐻\n6", Color.WHITE, "6");
        add(grid, "🦓\n−", LEAF, "−");
        add(grid, "🐮\n1", Color.WHITE, "1");
        add(grid, "🐷\n2", Color.WHITE, "2");
        add(grid, "🐭\n3", Color.WHITE, "3");
        add(grid, "🦒\n+", LEAF, "+");
        add(grid, "🐘\n0", Color.WHITE, "0");
        add(grid, "🐧\n.", Color.WHITE, ".");
        add(grid, "🦉\n⌫", SAND, "⌫");
        add(grid, "🐾\n=", FOREST, "=");
        setContentView(root);
    }

    private void add(GridLayout grid, String label, int color, String action) {
        Button btn = new Button(this);
        btn.setText(label);
        btn.setTextSize(19);
        btn.setAllCaps(false);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(0, 0, 0, 0);
        btn.setTextColor(color == FOREST ? Color.WHITE : FOREST);
        btn.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(dp(18));
        bg.setStroke(dp(1), color == Color.WHITE ? Color.rgb(220,205,180) : color);
        btn.setBackground(bg);
        GridLayout.LayoutParams p = new GridLayout.LayoutParams();
        p.width = 0; p.height = 0;
        p.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        p.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        p.setMargins(dp(5), dp(5), dp(5), dp(5));
        grid.addView(btn, p);
        btn.setOnClickListener(v -> press(action));
    }

    private void press(String a) {
        if (error && !a.equals("C")) clearAll();
        if (a.matches("[0-9]")) { digit(a); return; }
        if (a.equals(".")) { decimal(); return; }
        if (a.equals("C")) { clearAll(); return; }
        if (a.equals("⌫")) { backspace(); return; }
        if (a.equals("±")) { sign(); return; }
        if (a.equals("%")) { percent(); return; }
        if (a.equals("=")) { equalsNow(); return; }
        operation(a);
    }

    private void digit(String n) {
        String cur = display.getText().toString();
        if (startNew || cur.equals("0")) { display.setText(n); startNew = false; }
        else if (cur.length() < 16) display.setText(cur + n);
    }

    private void decimal() {
        if (startNew) { display.setText("0."); startNew = false; }
        else if (!display.getText().toString().contains(".")) display.append(".");
    }

    private void operation(String op) {
        if (first != null && operator != null && !startNew) equalsNow();
        try { first = new BigDecimal(display.getText().toString()); }
        catch(Exception e) { showError(); return; }
        operator = op;
        startNew = true;
    }

    private void equalsNow() {
        if (first == null || operator == null) return;
        try {
            BigDecimal second = new BigDecimal(display.getText().toString());
            BigDecimal out;
            switch(operator) {
                case "+": out = first.add(second); break;
                case "−": out = first.subtract(second); break;
                case "×": out = first.multiply(second); break;
                case "÷":
                    if (second.compareTo(BigDecimal.ZERO) == 0) { showError(); return; }
                    out = first.divide(second, 12, RoundingMode.HALF_UP); break;
                default: return;
            }
            display.setText(format(out));
            first = null; operator = null; startNew = true;
        } catch(Exception e) { showError(); }
    }

    private void sign() {
        try { display.setText(format(new BigDecimal(display.getText().toString()).negate())); }
        catch(Exception ignored) {}
    }

    private void percent() {
        try { display.setText(format(new BigDecimal(display.getText().toString()).divide(new BigDecimal("100"), 12, RoundingMode.HALF_UP))); startNew = true; }
        catch(Exception ignored) {}
    }

    private void backspace() {
        if (startNew) return;
        String s = display.getText().toString();
        if (s.length() <= 1 || (s.length() == 2 && s.startsWith("-"))) { display.setText("0"); startNew = true; }
        else display.setText(s.substring(0, s.length()-1));
    }

    private void clearAll() {
        first = null; operator = null; startNew = true; error = false; display.setText("0");
    }

    private void showError() {
        display.setText("Oops! 🐵"); error = true; first = null; operator = null; startNew = true;
    }

    private String format(BigDecimal n) {
        n = n.stripTrailingZeros();
        String s = n.toPlainString();
        return s.equals("-0") ? "0" : s;
    }

    private int dp(int v) { return Math.round(v * getResources().getDisplayMetrics().density); }
}
