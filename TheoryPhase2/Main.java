import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int n;
		System.out.println("Enter number of variables(non-terminals): ");
		n = in.nextInt();
		
		ArrayList<Variable> variables = new ArrayList<>();
		
		int x;
		for (int i = 0; i<n; i++)
		{
			System.out.print("Enter variable symbol: ");
			Variable var = new Variable(in.next());
			
			System.out.print("Enter number of productions: ");
			x = in.nextInt();
			
			for (int z = 0; z<x; z++)
			{
				System.out.print("Enter production: ");
				var.addTransition(in.next());
			}
			variables.add(var);
		}
		in.close();
		
		CFG grammar = new CFG(variables);
		
		System.out.println("Original Grammar");
		grammar.print();
		
		Variable newStart = new Variable("S0");
		newStart.addTransition("S");
		grammar.variables.add(0, newStart);
		
		System.out.println("\n1)Adding new starting symbol: ");
		grammar.print();
		
		grammar.removeEpsilonProductions();
		grammar.removeEpsilonProductions();
		System.out.println("\n2)Eliminating epsilon productions: ");
		grammar.print();
		
		grammar.removeAllUnitProductions();
		System.out.println("\n3)Removing unit productions: ");
		grammar.print();
		
		grammar.removeUnreachable();
		System.out.println("\n4)Removing useless symbols: ");
		grammar.print();
		
		grammar.CNFStep1();
		grammar.CNFStep2();
		System.out.println("\n5)Final grammar in CNF");
		grammar.print();
	}

}
