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
    
    //unmatch  backwards the index to check another possibility
    public void unmatch(Scanner.TokenType t){
        if(scanOut.get(index).getR() == t){
            index -=1;
        }
    }
    
    
    public Node parse(){
        index = 0;
        Node n = new Node(null,"program");
        n.addChild(program(n));
        return n;
    } 
    
    // A method for each grammer rule after EBNF
    // RULES:
    // program --->         stmt_sequence
    public Node program(Node p){
        Node n = stmt_sequence(p);
        return n;
    }
    
    // stmt_sequence --->   statement {;statement}
    public Node stmt_sequence(Node p){
        Node n = new Node(p, "stmt_sequence");
        n.addChild(statement(n));
        
       while((index < scanOut.size()) &&(scanOut.get(index).getR() == Scanner.TokenType.SEMICOLON))
       {
           match(Scanner.TokenType.SEMICOLON);
           if(index >= scanOut.size()) break;
           n.addChild(statement(n));
       }
       return n;
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
        else if(scanOut.get(index).getR() == Scanner.TokenType.WRITE){
            return write_stmt(p);
        }
        else{
            //error
        }
        return new Node(p, "mistake");
    }
    
    // if_stmt --->         if exp then stmt_sequence [else stmt_sequence] end
    public Node if_stmt(Node p){
        Node n = new Node(p, "If");
        match(Scanner.TokenType.IF);
        n.addChild(exp(n));
        match(Scanner.TokenType.THEN);
        n.addChild(stmt_sequence(n));
        if (scanOut.get(index).getR() == Scanner.TokenType.ELSE)
        {
            match(Scanner.TokenType.ELSE);
            n.addChild(stmt_sequence(n));
        }
        match(Scanner.TokenType.END);
        return n;
    }
    
    // repeat_stmt --->     repeat stmt_sequence until exp
    public Node repeat_stmt(Node p){
        Node n = new Node(p, "Repeat");
        match(Scanner.TokenType.REPEAT);
        n.addChild(stmt_sequence(n));
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
    public Node exp(Node p){
        Node n = new Node(p,"exp");
        n.addChild(simple_exp(n));
        if ((index < scanOut.size()) &&(scanOut.get(index).getR() == Scanner.TokenType.LESS_THAN ||
            scanOut.get(index).getR() == Scanner.TokenType.EQUAL_TO))
        {
            n.addChild(comparison_op(n));
            n.addChild(simple_exp(n));
        }        
        return n;
    }
    
    // comparison_op --->   < | =
    public Node comparison_op(Node p){
        if(scanOut.get(index).getR() == Scanner.TokenType.LESS_THAN) {
            match(Scanner.TokenType.LESS_THAN);
            return new Node(p,"<");        
        }
        if(scanOut.get(index).getR() == Scanner.TokenType.EQUAL_TO) {
            match(Scanner.TokenType.EQUAL_TO);
            return new Node(p,"=");        
        }
        else
        {
            //error
            return p;
        }
    }
    // simple_exp --->      term {addop term}
    public Node simple_exp(Node p){
        Node n = new Node(p,"simple_exp");
        n.addChild(term(n));
        
        while((index < scanOut.size()) &&(scanOut.get(index).getR() == Scanner.TokenType.PLUS ||
             scanOut.get(index).getR() == Scanner.TokenType.MINUS))
       {
           n.addChild(addop(n));
           n.addChild(term(n));
       }
        
        return n;
    }
    // addop --->           +|-
    public Node addop(Node p){
        if(scanOut.get(index).getR() == Scanner.TokenType.PLUS) {
            match(Scanner.TokenType.PLUS);
            return new Node(p,"+");        
        }
        else if(scanOut.get(index).getR() == Scanner.TokenType.MINUS){
            match(Scanner.TokenType.MINUS);
            return new Node(p,"-");        
        }
        else
        {
            //error
            return p;
        }
    }
    
    // term --->            factor {mulop factor}
    public Node term(Node p){
        Node n = new Node(p,"term");
        n.addChild(factor(n));
       
       while((index < scanOut.size()) &&(scanOut.get(index).getR() == Scanner.TokenType.MULTIPLY ||
             scanOut.get(index).getR() == Scanner.TokenType.DIVIDE))
       {
           n.addChild(mulop(n));
           n.addChild(factor(n));
       }
        return n;
    }
    
    // mulop --->           * | /
    public Node mulop(Node p){
        if(scanOut.get(index).getR() == Scanner.TokenType.MULTIPLY){
            match(Scanner.TokenType.MULTIPLY);
            return new Node(p,"*");        
        }
        else if(scanOut.get(index).getR() == Scanner.TokenType.DIVIDE){
            match(Scanner.TokenType.DIVIDE);
            return new Node(p,"/");        
        }
        else
        {
            //error
            return p;
        }
    }
    
    // factor --->          (exp) | number | identifier
    public Node factor(Node p){
        Node n = new Node(p,"factor");
        if(scanOut.get(index).getR()==Scanner.TokenType.LEFT_PAREN) {
                match(Scanner.TokenType.LEFT_PAREN);
                n.addChild(exp(n));
                match(Scanner.TokenType.RIGHT_PAREN);
        }
        else if(scanOut.get(index).getR()==Scanner.TokenType.NUMBER){
                match(Scanner.TokenType.NUMBER);
                n.addChild(new Node(n,"Number"));
        }
        else if(scanOut.get(index).getR()==Scanner.TokenType.IDENTIFIER){
                match(Scanner.TokenType.IDENTIFIER);
                n.addChild(new Node(n,"IDENTIFIER"));
        }
        //    default:
        //        break;
        
        return n;
    }
}
