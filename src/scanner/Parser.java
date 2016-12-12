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
    private int index;
    
    //the constructor takes an arraylist *SCANNER OUTPUT*
    //Index data member starting from 0

    public Parser(ArrayList<Pair<String, Scanner.TokenType>> s)
    {
        scanOut = s;
        index = 0;
    }
    //match  forwards the index *after the check*
    public void match(Scanner.TokenType t){
        if(scanOut.get(index).getR() == t){
            index +=1;
        }
    }
    public void unmatch(Scanner.TokenType t){
        if(scanOut.get(index).getR() == t){
            index -=1;
        }
    }
    //unmatch  backwards the index to check another possibility
    
    public Node parse(){
    
        Node n = new Node(null,"program");
        n.addChild(program(n));
        return n;
    } 
    // A method for each grammer rule after EBNF
    // RULES:
    // program --->         stmt_sequence
    public Node program(Node p){
        Node n = new Node(p,"statemnt sequence");
        stmt_sequence(n);
        return n;
    }
    // stmt_sequence --->   statement {;statement}
    public void stmt_sequence(Node p){
       p.addChild(statement(p));
       while(scanOut.get(index).getR() == Scanner.TokenType.SEMICOLON)
       {
           match(Scanner.TokenType.SEMICOLON);
           p.addChild(statement(p));
       
       }
       
    }
    // statement ---> if_statement | repeat_stmt | assign_stmt | read_stmt | write_stmt
    public Node statement(Node p){
        if(scanOut.get(index).getR() == Scanner.TokenType.IF){
            return if_stmt(p);        
        }
        else if(scanOut.get(index).getR() == Scanner.TokenType.REPEAT){
            return repeat_stmt(p);
        }
        else if(scanOut.get(index).getR() == Scanner.TokenType.IDENTIFIER){
            return assign_stmt(p);
        } 
        else if(scanOut.get(index).getR() == Scanner.TokenType.READ){
            return read_stmt(p);
        }
        else{
            //error
        }
    
    }
    // if_stmt --->         if exp then stmt_sequence [else stmt_sequence] end
    public Node if_stmt(Node p){
        Node n = new Node(p, "If");
        match(Scanner.TokenType.IF);
        n.addChild(exp(n));
        match(Scanner.TokenType.THEN);
        stmt_sequence(n);
        if (scanOut.get(index).getR() == Scanner.TokenType.ELSE)
        {
            match(Scanner.TokenType.ELSE);
            stmt_sequence(n);
        }
        return n;
    }
    // repeat_stmt --->     repeat stmt_sequence until exp
    public Node repeat_stmt(Node p){
        Node n = new Node(p, "Repeat");
        match(Scanner.TokenType.REPEAT);
        stmt_sequence(n);
        match(Scanner.TokenType.UNTIL);
        n.addChild(exp(n));
        return n;
    }
    // assign_stmt --->     identifier := exp
    public Node assign_stmt(Node p){
        Node n = new Node(p, "Assign");
        match(Scanner.TokenType.IDENTIFIER);
        match(Scanner.TokenType.ASSIGN);
        n.addChild(exp(n));
        return n;
    }
    // read_stmt --->       read identifier
    public Node read_stmt(Node p){
        Node n = new Node(p,"read");
        match(Scanner.TokenType.READ);
        match(Scanner.TokenType.IDENTIFIER);
        return n;
    }
    // write_stmt --->      write exp
    public Node write_stmt(Node p){
        Node n = new Node(p,"write");
        match(Scanner.TokenType.WRITE);
        n.addChild(exp(n));
        return n;
    }
    // exp --->             simple_exp [comparison_op simple_exp]
    // comparison_op --->   < | =
    // simple_exp --->      term {addop term}
    // addop --->           +|-
    // term --->            factor {mulop factor}
    // mulop --->           * | /
    // factor --->          (exp) | number | identifier
}
