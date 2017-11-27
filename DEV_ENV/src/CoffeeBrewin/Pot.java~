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

public class Boiler extends ViewableAtomic {
    // ViewableAtomic is used instead
    // of atomic due to its
    // graphics capability
    protected double time_step;
    protected double heat_flux;
    protected double volume;
    protected double density;
    protected double heat_capacity;
    protected double flow;
    protected double boiler_temp;
    protected double brewed_part;

    public Boiler() {
	this("Boiler", 0.5, 100, 10, 1, 1, 0.25);
    }

    public Boiler(String name, double Time_step, double Heat_flux,
		  double Volume, double Density, double Heat_capacity,
		  double Flow) {
	super(name);
	addInport("ControlSignal");
	addOutport("Status");

	time_step = Time_step;
	heat_flux = Heat_flux;
	volume = Volume;
	density = Density;
	heat_capacity = Heat_capacity;
	flow = Flow;
	}

    public void initialize() {
	phase = "Passive";
	// Possible phases include Passive and Brewing
	sigma = INFINITY;
	boiler_temp = 25; // Room temperature in celsius
	brewed_part = 0; // Nothing yet brewed
	super.initialize();
    }

    public void deltext(double e, message x) {
	Continue(e);
	
	System.out.println("The elapsed time of the Boiler is" + e);
	System.out.println("*****************************************");
	System.out.println("External-Phase before: " + phase);
	
	if (phaseIs("Passive")) {
	    for (int i = 0; i < x.getLength(); i++)
		if (messageOnPort(x, "ControlSignal", i)) {
		    if (x.getValOnPort("ControlSignal", i).toString() == "True")
			holdIn("Brewing", time_step);
		    else if (x.getValOnPort("ControlSignal", i).toString() == "False")
			holdIn("Passive", sigma-e);
		}
	} else if (phaseIs("Brewing")) {
	    for (int i = 0; i < x.getLength(); i++)
		if (messageOnPort(x, "ControlSignal", i)) {
		    if (x.getValOnPort("ControlSignal", i).toString() == "True")
			holdIn("Brewing", sigma - e);
		    else if (x.getValOnPort("ControlSignal", i).toString() == "False")
			holdIn("Passive", INFINITY);
		}
	}
	System.out.println("External-Phase after: " + phase);
    }

    public void deltint() {
	    
	System.out.println("Internal-Phase before: " + phase);
	if (phaseIs("Brewing")) {
	    if (brewed_part >= 100) {
		phase = "Passive";
		sigma = INFINITY;
	    } else {
		boiler_temp = boiler_temp + time_step*(heat_flux/(volume*density*heat_capacity));
		brewed_part = brewed_part + (time_step/flow);
		holdIn("Brewing", time_step);
	    }
	}
	try {
	    PrintWriter writer = new PrintWriter("thermo-vals.txt", "UTF-8");
	    writer.println("BoilerTemp:" + Double.toString(boiler_temp));
	    writer.println("PortionBrewed:" + Double.toString(brewed_part));
	    writer.close();
	} catch (FileNotFoundException|UnsupportedEncodingException e) {
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
	if (phaseIs("Brewing")) {
	    if (brewed_part < 100) {
		String current_temp = Double.toString(boiler_temp);
		m.add(makeContent("Status", new entity(current_temp)));
	    } else {
		m.add(makeContent("Status", new entity("0")));
	    }
	}
	return m;
    }

    public void showState() {
	super.showState();
    }
    
    public String getTooltipText() {
	return super.getTooltipText() + "\n" + " Boiler Temp: " + boiler_temp
	    + "\n" + " Portion Brewed: " + brewed_part;
    }
}
