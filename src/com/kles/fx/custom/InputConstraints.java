/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.fx.custom;

import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author JCHAUT
 */
public class InputConstraints {

    public static int UPPER_LOWER = 0;
    public static int UPPER = 1;
    public static int LOWER = 2;

    public static String LETTER_ONLY = "[A-Za-z]";
    public static String NUMBER_ONLY = "[0-9.]";
    public static String NUMBER_LETTER_ONLY = "[A-Za-z0-9.]";

    public static void noLeadingAndTrailingBlanks(final TextInputControl textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createNoLeadingBlanksInputHandler());
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static void noLeadingBlanks(final TextInputControl textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createNoLeadingBlanksInputHandler());
    }

    public static void noBlanks(final TextInputControl textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createNoBlanksInputHandler());
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static void numbersOnly(final TextInputControl textField) {
        numbersOnly(textField, Integer.MAX_VALUE);
    }

    public static void numbersOnly(final TextInputControl textField, final Integer maxLenth) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createNumbersOnlyInputHandler(maxLenth));
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static void decimalOnly(final TextInputControl textField, final Integer maxDecimal) {
        decimalOnly(textField, Integer.MAX_VALUE, maxDecimal);
    }

    public static void decimalOnly(final TextInputControl textField, final Integer minDecimal, final Integer maxDecimal) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createDecimalInputHandler(minDecimal, maxDecimal));
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static void maxLength(final TextInputControl textField, final Integer maxLenth) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createMaxLengthInputHandler(maxLenth));
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static void lettersOnly(final TextInputControl textField) {
        lettersOnly(textField, Integer.MAX_VALUE);
    }

    public static void lettersOnly(final TextInputControl textField, final Integer maxLenth) {
        lettersOnly(textField, maxLenth, UPPER_LOWER);
    }

    public static void lettersOnly(final TextInputControl textField, final Integer maxLenth, int type) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createLettersOnlyInputHandler(maxLenth, type));
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static void lettersNumbersOnly(final TextInputControl textField, final Integer maxLenth, int type) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createNumberLettersOnlyInputHandler(maxLenth, type));
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static void checkPattern(final TextInputControl textField, final String pattern) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, createPatternInputHandler(pattern));
        textField.focusedProperty().addListener((Observable observable) -> {
            textField.setText(textField.getText().trim());
        });
    }

    public static EventHandler<KeyEvent> createNoLeadingBlanksInputHandler() {
        return (KeyEvent event) -> {
            if (event.getSource() instanceof TextField) {
                TextField textField = (TextField) event.getSource();
                if (" ".equals(event.getCharacter()) && textField.getCaretPosition() == 0) {
                    event.consume();
                }
            }
        };
    }

    public static EventHandler<KeyEvent> createNumbersOnlyInputHandler(final Integer maxLength) {
        return createPatternInputHandler(maxLength, NUMBER_ONLY, 0);
    }

    public static EventHandler<KeyEvent> createLettersOnlyInputHandler(final Integer maxLength, int type) {
        return createPatternInputHandler(maxLength, LETTER_ONLY, type);
    }

    public static EventHandler<KeyEvent> createNumberLettersOnlyInputHandler(final Integer maxLength, int type) {
        return createPatternInputHandler(maxLength, NUMBER_LETTER_ONLY, type);
    }

    public static EventHandler<KeyEvent> createNoBlanksInputHandler() {
        return (KeyEvent event) -> {
            if (event.getSource() instanceof TextField) {
                if (" ".equals(event.getCharacter())) {
                    event.consume();
                }
            }
        };
    }

    public static EventHandler<KeyEvent> createPatternInputHandler(String pattern) {
        return (KeyEvent event) -> {
            if (event.getSource() instanceof TextField) {
                TextField textField = (TextField) event.getSource();
                if (!event.getCharacter().matches(pattern)) {
                    event.consume();
                }
            }
        };
    }

    public static EventHandler<KeyEvent> createMaxLengthInputHandler(final Integer maxLength) {
        return (KeyEvent event) -> {
            if (event.getSource() instanceof TextField) {
                TextField textField = (TextField) event.getSource();
                if (textField.getText().length() >= maxLength) {
                    event.consume();
                }
            }
        };
    }

    public static EventHandler<KeyEvent> createPatternInputHandler(final Integer maxLength, String pattern, int type) {
        return (KeyEvent event) -> {
            if (event.getSource() instanceof TextField) {
                TextField textField = (TextField) event.getSource();
                if (textField.getText().length() >= maxLength || !event.getCharacter().matches(pattern)) {
                    event.consume();
                } else {
                    if (pattern.equals(LETTER_ONLY) || pattern.equals(NUMBER_LETTER_ONLY)) {
                        switch (type) {
                            case 1:
                                textField.setText(textField.getText().toUpperCase() + event.getCharacter().toUpperCase());
                                break;
                            case 2:
                                textField.setText(textField.getText().toLowerCase() + event.getCharacter().toLowerCase());
                                break;
                        }
                        textField.positionCaret(textField.getText().length());
                        event.consume();
                    }
                }
            }
        };
    }

    public static EventHandler<KeyEvent> createDecimalInputHandler(final Integer minDecimal, final Integer maxDecimal) {
        return (KeyEvent event) -> {
            if (event.getSource() instanceof TextField) {
                TextField textField = (TextField) event.getSource();
                String[] tabDot = textField.getText().split("\\.");
                if (tabDot.length > 2 || (tabDot.length == 2 && minDecimal == 0) || (tabDot.length == 2 && tabDot[1].length() >= maxDecimal)) {
                    event.consume();
                }
                if (!event.getCharacter().matches("[0-9.]") && !event.getCharacter().matches("\\.")) {
                    event.consume();
                }

            }
        };
    }
}
