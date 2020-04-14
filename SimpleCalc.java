import java.util.List;		// used by expression evaluator

/**
 *	SimpleCalc uses to stacks to emulate a simple arithmetic 
 *  calculator.
 *
 *	@author	Anushka Vijay
 *	@since	February 28, 2019
 */
public class SimpleCalc {
	
	private ExprUtils utils;	// expression utilities
	
	private ArrayStack<Double> valueStack;		// value stack
	private ArrayStack<String> operatorStack;	// operator stack
	
	// constructor	
	public SimpleCalc() {
		//initialize objects and the stacks
		utils = new ExprUtils(); 
		valueStack = new ArrayStack<Double>();
		operatorStack = new ArrayStack<String>();
		
	}
	
	public static void main(String[] args) {
		SimpleCalc sc = new SimpleCalc();
		sc.run();
	}
	
	/**
	 * run method that calls runCalc() and prints introductory and
	 * closing statements
	 */
	public void run() {
		System.out.println("\nWelcome to SimpleCalc!!!");
		runCalc(); 
		System.out.println("\nThanks for using SimpleCalc! Goodbye.\n");
	}
	
	/**
	 *	Prompt the user for expressions, run the expression evaluator,
	 *	and display the answer.
	 */
	public void runCalc() {
		String userInput = Prompt.getString(" ");
		if(userInput.equalsIgnoreCase("h")) //if user requests help menu
		{
			printHelp();
			System.out.println();
		}
		else if(userInput.equalsIgnoreCase("q")) //if user wants to quit
		{
			System.out.println("\nThanks for using SimpleCalc! Goodbye.\n");
			System.exit(1);
		}
		else
		{
			//evaluating expression otherwise
			System.out.println();
			List<String> tokens = utils.tokenizeExpression(userInput);
			double answer = evaluateExpression(tokens);
			System.out.println(answer);
		}
		runCalc();
	}
	
	/**	Print help */
	public void printHelp() {
		System.out.println("Help:");
		System.out.println("  h - this message\n  q - quit\n");
		System.out.println("Expressions can contain:");
		System.out.println("  integers or decimal numbers");
		System.out.println("  arithmetic operators +, -, *, /, %, ^");
		System.out.println("  parentheses '(' and ')'");
	}
	
	/**
	 *	Evaluate expression and return the value
	 *	@param tokens	a List of String tokens making up an arithmetic expression
	 *	@return			a double value of the evaluated expression
	 */
	public double evaluateExpression(List<String> tokens) {
		double value = 0.0;
		int pointer = 0;
		if(tokens.size() == 1)
			return Double.parseDouble(tokens.get(0));
		//1)While there are tokens to be read in, 
		while(pointer < tokens.size())
		{
			 //Get the next token, If the token is:
			String currentToken = tokens.get(pointer); 
			//a) A number: push it onto the value stack 
			if(utils.isOperator(currentToken.charAt(0)) == false) {
				double val = Double.parseDouble(currentToken);
				valueStack.push(val);
			}	
			//b)  A left parenthesis: push it onto the operator stack
			else if(currentToken.equals("(")) {
				operatorStack.push(currentToken);
			}
			//c) A right parenthesis 
			else if(currentToken.equals(")")) {
				String leftParen = "(";
				//A. While the thing on top of the operator stack is not a left parenthesis
				while(!operatorStack.peek().equals(leftParen)) 
				{
					//A.1 Pop the operator from the operator stack 
					String operator = operatorStack.pop();
					//A.2 Pop the value stack twice, getting two operands. 
					double operand2 = valueStack.pop();
					double operand1 = valueStack.pop();
					//A.3 Apply the operator to the operands, in the correct order.
					double answer = operation(operand1, operand2, operator);
					//A.4 Push the result onto the value stack. 
					valueStack.push(answer);
				}
				//B. Pop the left parenthesis from the operator stack, and discard. 
				String discard = operatorStack.pop();
			}
			//if token an operator 
			else if(utils.isOperator(currentToken.charAt(0))) {
				//A. While the operator stack is not empty, and the top thing 
					//on the operator stack has the same or greater precedence 
					//as the token's operator 
				while(operatorStack.isEmpty() == false && 
						hasPrecedence(currentToken, operatorStack.peek()))	
				{	//A.1 Pop the operator from the operator stack
					String operator = operatorStack.pop(); 
					//A.2 Pop the value stack twice, getting two operands 
					double operand2 = valueStack.pop();
					double operand1 = valueStack.pop();
					//A.3 Apply the operator to the operands, in the correct order.
					double answer = operation(operand1, operand2, operator); 
					//A.4 Push the result onto the value stack. 
					valueStack.push(answer);
				}
				//B. Push token operator onto the operator stack. 
				operatorStack.push(currentToken);
			}
			pointer++;
		}
		//2)While the operator stack is not empty,
		if(valueStack.size() > 1)
		{
			boolean isWorking = true; 
			while(isWorking)
			{
				//2.1 Pop the operator from the operator stack. 
				String operator = operatorStack.pop();
					//2.2 Pop the value stack twice, getting two operands 
				double operand2 = valueStack.pop();
				double operand1 = valueStack.pop();
				//2.3 Apply the operator to the operands, in the correct order 
				double answer = operation(operand1, operand2, operator);
					//2.4 Push the result on the value stack. 
				valueStack.push(answer);
				if(operatorStack.isEmpty())
					isWorking = false;
				if(isWorking == false)
					value = answer;
			}
		}
		//3.) At this point the operator stack should be empty, and value stack has the answer
		value = valueStack.pop();
		return value;
	}
	
	/**
	 * Evaluates both operands and the operator
	 * @param operand1	operand on left-hand side of expression
	 * @param operand2	operand on right-hand side of expression
	 * @param operator	operator to be solved
	 * @return 	result of the solved operations
	 */
	private double operation(double operand1, double operand2, String operator)
	{
		double answer = 0.0;
		//solves based on specified operations and operands
		if(operator.equals("+"))
			answer = operand1 + operand2;
		else if(operator.equals("-"))
			answer = operand1 - operand2;
		else if(operator.equals("*"))
			answer = operand1 * operand2;
		else if(operator.equals("^"))
			answer = Math.pow(operand1, operand2);
		else if(operator.equals("%"))
			answer = operand1 % operand2;
		else if(operator.equals("/"))
			answer = operand1/operand2;
		return answer;
	}	
	
	/**
	 *	Precedence of operators
	 *	@param op1	operator 1
	 *	@param op2	operator 2
	 *	@return		true if op2 has higher or same precedence as op1; false otherwise
	 *	Algorithm:
	 *		if op1 is exponent, then false
	 *		if op2 is either left or right parenthesis, then false
	 *		if op1 is multiplication or division or modulus and 
	 *				op2 is addition or subtraction, then false
	 *		otherwise true
	 */
	private boolean hasPrecedence(String op1, String op2) {
		if (op1.equals("^")) return false;
		if (op2.equals("(") || op2.equals(")")) return false;
		if ((op1.equals("*") || op1.equals("/") || op1.equals("%")) 
				&& (op2.equals("+") || op2.equals("-")))
			return false;
		return true;
	}
	 
}
