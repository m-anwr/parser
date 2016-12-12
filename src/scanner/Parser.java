/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import java.util.ArrayList;

/**
 *
 * @author shaza
 */
public class Parser {
        
    private final ArrayList<Pair<String, Scanner.TokenType>> scanOut;
    private final int index;
    
    //the constructor takes an arraylist *SCANNER OUTPUT*
    //Index data member starting from 0

    public Parser(ArrayList<Pair<String, Scanner.TokenType>> s)
    {
        scanOut = s;
        index = 0;
    }
    //match  forwards the index *after the check*
    //unmatch  backwards the index to check another possibility
    // A method for each grammer rule after EBNF
    // RULES:
    // program --->         stmt_sequence
    // stmt_sequence --->   statement {;statement}
    // statement ---> if_statement | repeat_stmt | assign_stmt | read_stmt | write_stmt
    // if_stmt --->         if exp then stmt_sequence [else stmt_sequence] end
    // repeat_stmt --->     repeat stmt_sequence until exp
    // assign_stmt --->     identifier := exp
    // read_stmt --->       read identifier
    // write_stmt --->      write exp
    // exp --->             simple_exp [comparison_op simple_exp]
    // comparison_op --->   < | =
    // simple_exp --->      term {addop term}
    // addop --->           +|-
    // term --->            factor {mulop factor}
    // mulop --->           * | /
    // factor --->          (exp) | number | identifier
}
