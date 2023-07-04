import java.util.ArrayList;

public class Variable {
	//Data members
	private String symbol;
	private ArrayList<String> transitions;
	
	//Constructors
	Variable(){};
	Variable(String symbol)
	{
		this.symbol = symbol;
		transitions = new ArrayList<String>();
	}
	
	//Setters and getters
	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	public String getSymbol()
	{
		return symbol;
	}
	
	public ArrayList<String> getTransitions()
	{
		return transitions;
	}
	public void setTransitions(ArrayList<String> transitions)
	{
		this.transitions = transitions;
	}
	
	//Methods
	public void modifyTransition (String old, String modified)
	{
		for (int i = 0; i<transitions.size(); i++)
		{
			String t = transitions.get(i);
			if (t.equals(old))
			{
				transitions.set(transitions.indexOf(old), modified);
			}
		}
	}
	public void addTransition(String transition)
	{
		transitions.add(transition);
	}
	
	public void addTransitions(ArrayList<String> transitions)
	{
		this.getTransitions().addAll(transitions);
	}
	
	public void print()
	{
		System.out.print(symbol + " --> ");
		
		for (int i = 0; i<transitions.size(); i++)
		{
			String t = transitions.get(i);
			
			if (i == (transitions.size()-1))
			{
				System.out.print(t);
			}
			else
			{
				System.out.print(t + " | ");
			}
			
		}
		System.out.print("\n");
	}
	
}
