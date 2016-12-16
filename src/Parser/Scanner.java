/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

/**
 *
 * @author anwer
 */
public class Scanner {
    public enum TokenType {
	IF,
	THEN,
	ELSE,
	END,
	REPEAT,
	UNTIL,
	READ,
	WRITE,
	PLUS,
	MINUS,
	MULTIPLY,
	DIVIDE,
	EQUAL_TO,
	LESS_THAN,
	GREATER_THAN,
	LEFT_PAREN,
	RIGHT_PAREN,
	SEMICOLON,
	ASSIGN,
	IDENTIFIER,
	NUMBER;
    }
    
    public Scanner(String c) {
        this.res = new ArrayList<>();
        code = c;
    }
    
    public void scan() {
        String token;
        
        for(String line : code.split("\n") ) {
            // clearing extra spaces
            line = line.replaceAll("\\s+", " ");
            
            // looping on each character on each line
            ArrayList<String> chars = new ArrayList<>(Arrays.asList(line.split("")));
            
            for (ListIterator<String> i = chars.listIterator(); i.hasNext();) {
                token = "";
                String stringChar = i.next();
                
                if(stringChar.matches("[a-zA-Z]")) {
                    token = token.concat(stringChar);
                    
                    while (i.hasNext()) {
                        stringChar = i.next();
                        if (stringChar.matches("[a-zA-Z0-9]")) {
                            token = token.concat(stringChar);
                        } else {
                            i.previous();
                            break;
                        }
                    }
                    
                    boolean added = false;
                    for(TokenType t : Arrays.asList(TokenType.values()).subList(0, 8)) {
                        if(token.matches("(?i)" + t.toString())) {
                            res.add(new Pair<>(token, t));
                            added = true;
                        }
                    }
                    
                    if(!added)
                        res.add(new Pair<>(token, TokenType.IDENTIFIER));
                } else if(stringChar.matches("[0-9]")) {
                    token = token.concat(stringChar);
                    
                    while (i.hasNext()) {
                        stringChar = i.next();
                        if (stringChar.matches("[0-9]")) {
                            token = token.concat(stringChar);
                        } else {
                            i.previous();
                            break;
                        }
                    }
                    
                    res.add(new Pair<>(token, TokenType.NUMBER));
                } else if (stringChar.matches("\\{")) {
                    eliminateComment(i);
                    //while(i.hasNext() && !i.next().matches("\\}")) {}
                } else if (stringChar.matches(":")) {
                    token = token.concat(stringChar);
                    if(i.hasNext()) {  
                        if (i.next().matches("=")) {
                            token = token.concat("=");
                            res.add(new Pair<>(token, TokenType.ASSIGN));
                        } else {
                            i.previous();
                        }
                    }
                } else if (stringChar.matches(";")) {
                    res.add(new Pair<>(stringChar, TokenType.SEMICOLON));
                } else if (stringChar.matches("\\+")) {
                    res.add(new Pair<>(stringChar, TokenType.PLUS));
                } else if (stringChar.matches("-")) {
                    res.add(new Pair<>(stringChar, TokenType.MINUS));
                } else if (stringChar.matches("\\*")) {
                    res.add(new Pair<>(stringChar, TokenType.MULTIPLY));
                } else if (stringChar.matches("/")) {
                    res.add(new Pair<>(stringChar, TokenType.DIVIDE));
                } else if (stringChar.matches("=")) {
                    res.add(new Pair<>(stringChar, TokenType.EQUAL_TO));
                } else if (stringChar.matches(">")) {
                    res.add(new Pair<>(stringChar, TokenType.GREATER_THAN));
                } else if (stringChar.matches("<")) {
                    res.add(new Pair<>(stringChar, TokenType.LESS_THAN));
                } else if (stringChar.matches("\\)")) {
                    res.add(new Pair<>(stringChar, TokenType.RIGHT_PAREN));
                } else if (stringChar.matches("\\(")) {
                    res.add(new Pair<>(stringChar, TokenType.LEFT_PAREN));
                }
            }
        }
    }
    
    private void eliminateComment(ListIterator<String> i) {
        while (i.hasNext()) {
            String c = i.next();
            if(c.matches("\\}")) {
                break;
            } else if (c.matches("\\{")) {
                eliminateComment(i);
            }
        }
    }
    
    public ArrayList<Pair<String, TokenType>> scanResult() {
        return res;
    }
    
    private final ArrayList<Pair<String, TokenType>> res;
    private final String code;
}