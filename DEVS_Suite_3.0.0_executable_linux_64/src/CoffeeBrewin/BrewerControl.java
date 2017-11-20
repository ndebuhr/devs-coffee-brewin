/*     
 *    
 *  Author     : ACIMS(Arizona Center for Integrative Modeling & Simulation)
 *  Version    : DEVS-Suite 3.0.0  
 *  Date       : 10-01-2017
 */
package CoffeeBrewin;

import GenCol.*;
import model.modeling.*;
import model.simulation.*;
import view.modeling.ViewableAtomic;
import view.simView.*;
import view.modeling.ViewableComponent;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class BrewerControl extends ViewableAtomic {
    // ViewableAtomic is used instead
    // of atomic due to its
    // graphics capability
    protected double register_time;
    protected double safety_timeout;
    protected double display_time;
    protected double indicator_timeout;
    protected double max_safe_temp;

    public BrewerControl() {
	this("BrewerControl", 1, 30, 2, 12, 200);
    }

    public BrewerControl(String name, double Register_time, double Safety_timeout,
			 double Display_time, double Indicator_timeout, double Max_safe_temp) {
	super(name);
	addInport("inThermo");
	addInport("inUser");
	addOutport("outThermo");
	addOutport("outUser");
	
	register_time = Register_time;
	safety_timeout = Safety_timeout;
	display_time = Display_time;
	indicator_timeout = Indicator_timeout;
	max_safe_temp = Max_safe_temp;
	}

    public void initialize() {
	phase = "Passive";
	// Possible phases include Passive, BrewRequest, BrewProgress,
	// BrewReady, BrewTerminate, and Standby
	sigma = INFINITY;
	super.initialize();
    }

    public void deltext(double e, message x) {
	Continue(e);
	
	System.out.println("The elapsed time of the CoffeeControl is" + e);
	System.out.println("*****************************************");
	System.out.println("External-Phase before: " + phase);
	
	if (phaseIs("Passive")) {
	    for (int i = 0; i < x.getLength(); i++)
		if (messageOnPort(x, "inUser", i)) {
		    if (x.getValOnPort("inUser", i).toString() == "On")
			holdIn("BrewRequest", register_time);
		    else if (x.getValOnPort("inUser", i).toString() == "Off")
			sigma = sigma - e;
		}
	} else if (phaseIs("BrewProgress")) {
	    for (int i = 0; i < x.getLength(); i++)
		{
		    if (messageOnPort(x, "inUser", i)) {
			if (x.getValOnPort("inUser", i).toString() == "On")
			    holdIn("BrewProgress", sigma - e);
			else if (x.getValOnPort("inUser", i).toString() == "Off")
			    holdIn("BrewTerminate", register_time);
		    }
		    if (messageOnPort(x, "inThermo", i)) {
			if (Double.parseDouble(x.getValOnPort("inThermo", i).toString()) > max_safe_temp)
			    holdIn("BrewTerminate", register_time);
			else if (Double.parseDouble(x.getValOnPort("inThermo", i).toString()) == 0)
			    holdIn("BrewReady", display_time);
			else
			    holdIn("BrewProgress", sigma - e);
		    }
		}
	}
	System.out.println("External-Phase after: " + phase);
    }

    public void deltint() {
	    
	System.out.println("Internal-Phase before: " + phase);
	if (phaseIs("BrewRequest")) {
	    holdIn("BrewProgress", safety_timeout);
	} else if (phaseIs("BrewProgress")) {
	    holdIn("BrewTerminate", register_time);
	} else if (phaseIs("BrewTerminate")) {
	    phase = "Passive";
	    sigma = INFINITY;
	} else if (phaseIs("BrewReady")) {
	    holdIn("BrewReady", indicator_timeout);
	} else if (phaseIs("Standby")) {
	    phase = "Passive";
	    sigma = INFINITY;
	} else if (phaseIs("Passive")) {
	    phase = "Passive";
	    sigma = INFINITY;
	}
	System.out.println("Internal-Phase after: " + phase);
    }

    public void deltcon(double e, message x) {
	System.out.println("confluent");
	deltext(0, x);
	deltint();
    }

    public message out() {
	message m = new message();
	String out_user = "Blank";
	if (phaseIs("BrewRequest")) {
	    m.add(makeContent("outThermo", new entity("True")));
	    m.add(makeContent("outUser", new entity("Brewing")));
	    out_user = "Brewing";
	} else if (phaseIs("BrewTerminate")) {
	    m.add(makeContent("outThermo", new entity("False")));
	    m.add(makeContent("outUser", new entity("Blank")));
	    out_user = "Blank";
	} else if (phaseIs("BrewReady")) {
	    m.add(makeContent("outThermo", new entity("False")));
	    m.add(makeContent("outUser", new entity("Ready")));
	    out_user = "Ready";
	} else if (phaseIs("Standby")) {
	    m.add(makeContent("outUser", new entity("Blank")));
	    out_user = "Blank";
	}
	try {
	    PrintWriter writer = new PrintWriter("control-vals.txt", "UTF-8");
	    writer.println("OutUser:" + out_user);
	    writer.close();
	} catch (FileNotFoundException|UnsupportedEncodingException e) {
	}
	return m;
    }

    public void showState() {
	super.showState();
    }
    
    public String getTooltipText() {
	return super.getTooltipText() + "\n" + " Temp Safety Cutoff: " + max_safe_temp
	    + "\n" + " Safety Timeout: " + safety_timeout;
    }
}
