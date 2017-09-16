import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FSM{
	public static List<State> states_list = new ArrayList<State>();
	public static List<Transition> transition_list = new ArrayList<Transition>();

	public static void printTransList(List<Transition> tl){
		System.out.println("start of printTransList function");
		for(Transition i: tl){
			System.out.println(i.src + "  " + i.dest);
		}
		System.out.println("end of printTransList function");
	}
	public static void printStatesList(List<State> sl){
		System.out.println("start of printStatesList function");
		for(State i: sl){
			System.out.println(i.name);
		}
		System.out.println("end of printStatesList function");
	}

}

class State{
	public String name;
	public State(String nm){
		this.name = nm;
	}
}

class Transition{
	public String src,dest;
	public Transition(String src,String dest){
		this.src = src;
		this.dest = dest;
	}
} 

