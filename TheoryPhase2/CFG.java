import java.util.*;

public class CFG {
	//Data members
	public ArrayList<Variable> variables;
	public ArrayList<String> symbols;
	public String alphabet = "DEFGHIJKLMNOPQRTUVWXYZ";
	
	//Constructors
	CFG(){};
	CFG(ArrayList<Variable> variables)
	{
		this.variables = variables;
		this.symbols = new ArrayList<>();
		for (Variable var : this.variables)
		{
			this.symbols.add(var.getSymbol());
		}
	}
	
	//Methods
	
	//Method to print CFG
	public void print()
	{
		for (Variable var : variables)
		{
			var.print();
		}
	}
	
	
	//Method to remove all epsilon productions from CFG
	public void removeEpsilonProductions()
	{
		//Loop on all variables in CFG
		Variable var = new Variable();
		for (int i = 0; i<variables.size(); i++)
		{
			var = variables.get(i);	
			if (isNullable(var))
				{
					var.getTransitions().remove("e");
					//Loop on all transitions of all variables
					for (int z = 0; z<variables.size(); z++)
					{
						Variable var2 = variables.get(z);
			
						for (int v = 0; v<var2.getTransitions().size(); v++)
						{
							String t2 = var2.getTransitions().get(v);
							//Finding variable which has an epsilon transition in all other transitions
							if (t2.contains(var.getSymbol()))
							{
								//In the case of a production which contains a nullable symbol and is > 2 characters
								//Add a new production with for every possible removal of the nullable symbol
								if (t2.length()>1)
								{
									for (int j = 0; j<t2.length(); j++)
									{
										if (String.valueOf(t2.charAt(j)).equals(var.getSymbol()))
										{
											String x = t2.substring(0, j) + t2.substring(j+1);
											if (!(x.equals("")))
											{
												var2.addTransition(x);
											}
											else
											{
												var2.addTransition("e");
											}
										}
									}
									
									String x;
									x = t2.replace(var.getSymbol(), "");
									if (!x.equals(""))
									{
										var2.addTransition(x);
									}
									else
									{
										var2.addTransition("e");
									}
									
								}
								
								else 
								{
									String x;
									x = t2.replace(var.getSymbol(), "");
									if (!x.equals(""))
									{
										var2.addTransition(x);
									}
									else
									{
										var2.addTransition("e");
									}
								}
								
							}
						}
					}
				}
			}
		this.clean();
	}
	
	
	//Method to remove unit productions of one variable
	public void removeUnitProductions(Variable var)
	{
			for (int i = 0; i<var.getTransitions().size(); i++)
			{
				String t = var.getTransitions().get(i);
				if (symbols.contains(t))
				{
					Variable var2 = findVariable(t);
					var.getTransitions().remove(t);
					var.addTransitions(var2.getTransitions());
					removeUnitProductions(var);
				}
			}
		this.clean();
	}
	
	//Method to remove all unit productions in CFG
	public void removeAllUnitProductions()
	{
		for (Variable var : variables)
		{
			for (int i = 0; i<var.getTransitions().size(); i++)
			{
				String t = var.getTransitions().get(i);
				if (var.getSymbol().equals(t))
				{
					var.getTransitions().remove(t);
				}
			}
		}
		
		for (Variable var : variables)
		{
			removeUnitProductions(var);
		}
	}
	
	//Method to remove unreachable variables from CFG
	public void removeUnreachable ()
	{
		Variable var = new Variable();
		
		//New list to store all reachable variables
		ArrayList<Variable> reachable = new ArrayList<>();
		reachable.add(variables.get(0));
		
		//Loop on all variables
		for (int i = 1; i<variables.size(); i++)
		{
			var = variables.get(i);
			
			//Loop on all reachable variables
			//Checking if current variable is reachable from any of the reachable variables
			for (int j = 0; j<reachable.size(); j++)
			{
				if (isReachable(reachable.get(j), var))
				{
					reachable.add(var);
					break;
				}
			}
		}
		
		//Removing any variable which is not in the reachable list
		for (int i = 1; i<variables.size(); i++)
		{
			if (!(reachable.contains(variables.get(i))))
			{
				variables.remove(i);
			}
		}
	}
	
	//Method that checks if a variable is reachable from another variable
	public boolean isReachable(Variable start, Variable var)
	{
		//Loop on productions of start variable to check if symbol of other variable is in any production
		boolean check = false;
		for (String t : start.getTransitions())
		{
			if (t.contains(var.getSymbol()))
			{
				check = true;
				break;
			}
		}
		return check;
	}
	
	
	//Method to start converting to CNF by finding non-terminal productions which contain > 2 non-terminals
	//and replacing the first 2 non-terminals with a new variable which only produces these 2 non-terminals
	public void CNFStep1 ()
	{
		//Loop on all variables in CFG
		Variable var = new Variable();
		String t;
		for (int i = 0; i<variables.size(); i++)
		{
			var = variables.get(i);
			
			//Loop on all productions of a variable
			for (int j = 0; j<var.getTransitions().size(); j++)
			{
				t = var.getTransitions().get(j);
				if (t.length()>2 && isAllSymbols(t))
				{
					String x = t.substring(0, 2);
					String y = selectSymbol(this.alphabet);
					this.symbols.add(y);
					replaceAll(x, y);
					Variable newVar = new Variable(y);
					newVar.addTransition(x);
					variables.add(newVar);
					removeAllUnitProductions();
				}
			}
		}
	}
	
	//Method to continue conversion to CNF by finding productions with both terminals and non-terminals
	//and creating new variable which only produces a terminal and adding it instead of any occurrence of
	//this terminal
	public void CNFStep2()
	{
		Variable var = new Variable();
		String t;
		for (int i = 0; i<variables.size(); i++)
		{
			var = variables.get(i);
			
			for (int j = 0; j<var.getTransitions().size(); j++)
			{
				t = var.getTransitions().get(j);
				
				if (!(isAllSymbols(t)) && t.length()>1)
				{
					String x = findTerminal(t);
					String y = selectSymbol(this.alphabet);
					this.symbols.add(y);
					replaceAll(x, y);
					Variable newVar = new Variable(y);
					newVar.addTransition(x);
					variables.add(newVar);
					removeAllUnitProductions();
				}
				
				if (t.length()>2)
				{
					CNFStep1();
				}
			}
		}
	}
	
	
	//Method to remove duplicate transitions from all variables in CFG
	public void clean()
	{
		for (Variable var : variables)
		{
			ArrayList<String> cleaned = new ArrayList<>();
			
			for (String t : var.getTransitions())
			{
				if (!(cleaned.contains(t)))
				{
					cleaned.add(t);
				}
			}
			var.setTransitions(cleaned);
		}
	}
	
	//Method to find the variable which has the matching symbol
	public Variable findVariable(String symbol)
	{
		Variable found = new Variable();
		for (Variable var : variables)
		{
			if (var.getSymbol().equals(symbol))
			{
				found = var;
			}
		}
		return found;
	}
	
	
	//Method to replace all instances of a production with another production
	public void replaceAll(String x, String y)
	{
		for (int i = 0; i<variables.size(); i++)
		{
			Variable var = variables.get(i);
			
			for (int j = 0; j<var.getTransitions().size(); j++)
			{
				String t = var.getTransitions().get(j);
				if (t.contains(x))
				{
					String m = t.replace(x, y);
					var.modifyTransition(t, m);
				}
			}
		}
	}
	
	
	//Method to randomly choose a new symbol for creating a new variable
	public String selectSymbol (String alpha)
	{
		Random random = new Random();
	    int index = random.nextInt(alpha.length());
	    String x = String.valueOf(alpha.charAt(index));
	    this.alphabet = alpha.replace(x, "");
	    return x;
	}
	
	
	//Method to check if a production contains only non-terminals
	public boolean isAllSymbols (String t)
	{
		char chars [] = t.toCharArray();
		for (char c : chars)
		{
			if(!(symbols.contains(String.valueOf(c))))
			{
				return false;
			}
		}
		return true;
	}
	
	//Method to find the terminal with the given symbol
	public String findTerminal (String t)
	{
		char chars[] = t.toCharArray();
		for (char c : chars)
		{
			if (!(symbols.contains(String.valueOf(c))))
			{
				return String.valueOf(c);
			}
		}
		
		return "None";
	}
	
	//Method to check if a variable has an epsilon production
	public boolean hasEpsilonProduction (Variable var)
	{
		for (String t : var.getTransitions())
		{
			if (t.equals("e"))
			{
				return true;
			}
		}
		return false;
	}
	
	//Method to check if a variable is nullable
	public boolean isNullable (Variable var)
	{
		
		if (hasEpsilonProduction(var))
		{
			return true;
		}
		
		
		for (String t : var.getTransitions())
		{
			if (symbols.contains(t) && hasEpsilonProduction(findVariable(t)))
			{
				return true;
			}
			
			if (t.length()>1 && isAllSymbols(t))
			{
				int count = 0;
				for (char c : t.toCharArray())
				{
					count += 1;
					Variable v = findVariable(String.valueOf(c));
					if (String.valueOf(c).equals(var.getSymbol()))
					{
						return false;
					}
					if (!(isNullable(v)))
					{
						return false;
					}
					if (count == t.length())
					{
						return true;
					}
				}
			}
			
		}
		return false;
	}
}

