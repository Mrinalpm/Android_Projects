package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import java.lang.*;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonZero;
    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    private Button buttonFour;
    private Button buttonFive;
    private Button buttonSix;
    private Button buttonSeven;
    private Button buttonEight;
    private Button buttonNine;

    private Button buttonPlus;
    private Button buttonSubtract;
    private Button buttonMultiply;
    private Button buttonDivide;

    private TextView userInput;
    private Button equals;
    private Button point;
    private Button clearAll;
    private TextView result;
    //private Button[] signs = new Button[]{findViewById(R.id.buttonPlus), findViewById(R.id.buttonSubtract), findViewById(R.id.buttonMultiply), findViewById(R.id.buttonDivide)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initializeButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                appendToInput(((Button)v).getText().toString());
                double result = evaluate(userInput.getText().toString());
                if(Double.isNaN(result)) {
                    setResult("");
                } else {
                    setResult(""+result);
                }
            }
        });
    }

    private void initialize() {
        buttonZero = findViewById(R.id.buttonZero);
        buttonOne = findViewById(R.id.buttonOne);
        buttonTwo = findViewById(R.id.buttonTwo);
        buttonThree = findViewById(R.id.buttonThree);
        buttonFour = findViewById(R.id.buttonFour);
        buttonFive = findViewById(R.id.buttonFive);
        buttonSix = findViewById(R.id.buttonSix);
        buttonSeven = findViewById(R.id.buttonSeven);
        buttonEight = findViewById(R.id.buttonEight);
        buttonNine = findViewById(R.id.buttonNine);

        buttonPlus = findViewById(R.id.buttonPlus);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonMultiply = findViewById(R.id.buttonMultiply);
        buttonDivide = findViewById(R.id.buttonDivide);

        userInput = findViewById(R.id.userInput);
        equals = findViewById(R.id.buttonEquals);
        point = findViewById(R.id.buttonPoint);
        clearAll = findViewById(R.id.buttonClear);
        result = findViewById(R.id.result);


        initializeButton(buttonOne);
        initializeButton(buttonTwo);
        initializeButton(buttonThree);
        initializeButton(buttonFour);
        initializeButton(buttonFive);
        initializeButton(buttonSix);
        initializeButton(buttonSeven);
        initializeButton(buttonEight);
        initializeButton(buttonNine);
        initializeButton(buttonPlus);
        initializeButton(buttonSubtract);
        initializeButton(buttonMultiply);
        initializeButton(buttonDivide);
        initializeButton(point);
        equals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(""+evaluate(userInput.getText().toString()));
                setInput("");
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult("");
                setInput("");
            }
        });
    }

    private void appendToInput(String s) {
        userInput.setText(userInput.getText().toString()+s);
    }

    private void appendToResult(String s) {
        result.setText(result.getText().toString()+s);
    }

    private void setInput(String str) {
        userInput.setText(str);
    }

    private void setResult(String str) {
        result.setText(str);
    }

    public static double evaluate(final String str) {
        try {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                    return x;
                }

                // Grammar:
                // expression = term | expression `+` term | expression `-` term
                // term = factor | term `*` factor | term `/` factor
                // factor = `+` factor | `-` factor | `(` expression `)`
                //        | number | functionName factor | factor `^` factor

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if      (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if      (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('/')) x /= parseFactor(); // division
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(str.substring(startPos, this.pos));
                    } else if (ch >= 'a' && ch <= 'z') { // functions
                        while (ch >= 'a' && ch <= 'z') nextChar();
                        String func = str.substring(startPos, this.pos);
                        x = parseFactor();
                        if (func.equals("sqrt")) x = Math.sqrt(x);
                        else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                        else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                        else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                        else throw new RuntimeException("Unknown function: " + func);
                    } else {
                        throw new RuntimeException("Unexpected: " + (char)ch);
                    }

                    if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                    return x;
                }
            }.parse();
        } catch(Exception e) {
            return Double.NaN;
        }

    }
}
