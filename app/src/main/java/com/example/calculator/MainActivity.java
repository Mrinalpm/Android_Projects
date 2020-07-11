package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import java.lang.*;
import java.text.DecimalFormat;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int SIGNIFICANT_DIGITS = 12; //number of digits after the decimal point
    private static final int MAX_STRING_SIZE = 15; //max number of allowable characters in any textfield

    //String used to format numbers to have correct number of decimal places
    private static String DECIMAL_FORMAT = "#.";

    //When pressed, these buttons will be added to the input field.
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
    private Button buttonOpenBracket;
    private Button buttonCloseBracket;
    private Button buttonPlus;
    private Button buttonSubtract;
    private Button buttonMultiply;
    private Button buttonDivide;
    private Button point;

    //Textfields for input and result
    private TextView userInput;
    private TextView result;

    //the equals button
    private Button equals;

    //buttons to clear everything and clear a single character from input
    private Button clearAll;
    private Button clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    /*
    Initialization method.
    Initializes buttons and their corresponding actions
     */
    private void initialize() {
        //initialize buttons
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
        buttonOpenBracket = findViewById(R.id.buttonOpenBracket);
        buttonCloseBracket = findViewById(R.id.buttonCloseBracket);

        buttonPlus = findViewById(R.id.buttonPlus);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonMultiply = findViewById(R.id.buttonMultiply);
        buttonDivide = findViewById(R.id.buttonDivide);

        userInput = findViewById(R.id.userInput);
        equals = findViewById(R.id.buttonEquals);
        point = findViewById(R.id.buttonPoint);
        clearAll = findViewById(R.id.buttonAllClear);
        clear = findViewById(R.id.buttonClear);
        result = findViewById(R.id.result);

        initializeButton(buttonZero);
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
        initializeButton(buttonOpenBracket);
        initializeButton(buttonCloseBracket);

        equals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try { //set input equal to result only if result is valid
                    double d = Double.parseDouble(result.getText().toString());
                    setInput(formatString(""+d));
                    setResult("");
                } catch (Exception e) {
                    //otherwie, do nothing
                }
            }
        });

        //clear both input and result fields
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult("");
                setInput("");
            }
        });

        //backspace the input field
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = userInput.getText().toString();
                if(!input.isEmpty()) {
                    setInput(formatString(input.substring(0, input.length() - 1)));
                    updateResult(""+evaluate(userInput.getText().toString()));
                }
            }
        });

        //format numbers based on specified number of digits after decimal place
        for(int i = 0 ; i < SIGNIFICANT_DIGITS ; i++) {
            DECIMAL_FORMAT += "#";
        }
    }

    /*
    When a character is to be added to input based on a button press, this method describes the actionlistener of that button
    @param button: The button to which the actionlistener will be associated.
    */
    private void initializeButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inputText = userInput.getText().toString(); //whatever is in the input field
                String toAppend = ((Button)v).getText().toString(); //whatever is to be added
                if(inputText.length() < MAX_STRING_SIZE) {
                    if(!inputText.isEmpty() && toAppend.equals("(") && Character.isDigit(inputText.toCharArray()[inputText.length()-1])) { //if open bracket, automatically add '*'
                        toAppend = "*" + toAppend;
                    }
                    try {
                        double d = Double.parseDouble(inputText);
                        if(d == 0) {
                            setInput(toAppend);
                        } else {
                            appendToInput(toAppend); //add to the input
                        }
                    } catch (Exception e) {
                        appendToInput(toAppend); //add to the input
                    }
                    updateResult(""+evaluate(userInput.getText().toString())); //update the result field accordingly
                }

            }
        });
    }

    /*
    Method that will update the result field based on what is passed as a parameter
    @ param result: what is to be entered in the result field (can be non-numeric)
    */
    private void updateResult(String result) {
        try {
            double d = Double.parseDouble(result); //valid input that can be stored as a numeric double
            if(Double.isInfinite(d)) { //as a result of division by zero
                setResult(formatString("Can't divide by zero"));
            } else {
                if(!Double.isNaN(d)) { //incomplete input results in NaN (Ex: "25+" will result in number being NaN)
                    setResult(formatString(""+d));
                } else {
                    setResult("");
                }
            }
        } catch (Exception e) { //non-numeric input
            setResult("");
        }
    }

    /*
    Add a string to the input
    */
    private void appendToInput(String s) {
        userInput.setText(userInput.getText().toString()+s);
    }

    /*
    Set the input to a string
    */
    private void setInput(String str) {
        userInput.setText(str);
    }

    /*
    Set the result to a string
    */
    private void setResult(String str) {
        result.setText(str);
    }

    /*
    Format string to correctly display required number of decimal places. If non-numeric str, will just return back the str
    @ param str: The input that is to be formatted
    */
    private String formatString(String str) {
        try {
            return new DecimalFormat(DECIMAL_FORMAT).format(Double.parseDouble(str));
        } catch (Exception e) {
            return str;
        }
    }

    /*
    Method to evaluate a mathematical expression
    @param str: The mathematical expression in the form of a string
    */
    private static double evaluate(final String str) {
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
                        else if (eat('/')) x /= parseFactor(); //division
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
