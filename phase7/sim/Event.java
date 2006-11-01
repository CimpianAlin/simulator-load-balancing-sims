package sim;
import java.util.TreeSet; // Gotta love the collections framework...

public class Event implements Comparable
{
	// Static variables and methods for the event queue
	
	private static TreeSet<Event> queue = new TreeSet<Event>();
	private static double clockTime = 0.0;
	private static double lastLogTime = Double.POSITIVE_INFINITY;
	private static int nextId = 0;
	public static double duration = Double.POSITIVE_INFINITY;
	
	public static void reset()
	{
		queue.clear();
		clockTime = 0.0;
		lastLogTime = Double.POSITIVE_INFINITY;
		nextId = 0;
		duration = Double.POSITIVE_INFINITY;
	}
	
	public static void schedule (EventTarget target, double time,
					int type, Object data)
	{
		queue.add (new Event (target, time + clockTime, type, data));
	}
	
	public static boolean nextEvent()
	{
		try {
			Event e = queue.first();
			queue.remove (e);
			// Update the clock
			clockTime = e.time;
			// Quit if the simulation's alloted time has run out
			if (clockTime > duration) return false;
			// Pass the packet to the target's callback method
			e.target.handleEvent (e.type, e.data);
			return true;
		}
		catch (java.util.NoSuchElementException x) {
			// No more events to dispatch
			return false;
		}
	}
	
	public static double time()
	{
		return clockTime;
	}
	
	public static void log (String message)
	{
		// Print a blank line between events
		if (clockTime > lastLogTime) System.out.println();
		lastLogTime = clockTime;
		System.out.print (clockTime + " " + message + "\n");
	}
	
	// Run until the duration expires or there are no more events to process
	public static void run()
	{
		while (nextEvent()) {}
	}
	
	// Non-static variables and methods for individual events
	
	private EventTarget target;
	private double time;
	private int id;
	private int type;
	private Object data;
	
	public Event (EventTarget target, double time, int type, Object data)
	{
		this.target = target;
		this.time = time;
		this.type = type;
		this.data = data;
		id = nextId++;
	}
	
	// Must be consistent with compareTo()
	public boolean equals (Object o)
	{
		Event e = (Event) o;
		if (e.time == time && e.id == id) return true;
		return false;
	}
	
	// Must be consistent with equals()
	public int compareTo (Object o)
	{
		Event e = (Event) o;
		// Sort events by time (order of occurrence)
		if (e.time > time) return -1;
		if (e.time < time) return 1;
		// Break ties by ID (order of scheduling)
		if (e.id > id) return -1;
		if (e.id < id) return 1;
		return 0;
	}
}
