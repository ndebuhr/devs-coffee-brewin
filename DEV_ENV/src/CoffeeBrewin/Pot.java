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

public class Pot extends ViewableAtomic {
    // ViewableAtomic is used instead
    // of atomic due to its
    // graphics capability
    protected double pot_temp;
    protected double received_temp;
    protected double temp_count;
    
    public Pot() {
	this("CoffeePot");
    }

    public Pot(String name) {
	super(name);
	addInport("TempStatus");
	addOutport("PotTemp");
	}

    public void initialize() {
	phase = "Passive";
	// Possible phases include Passive and Brewing
	sigma = INFINITY;
	temp_count = 1;
	pot_temp = 25; //Room temperature
	received_temp = 25; //Changed after input event
	super.initialize();
    }

    public void deltext(double e, message x) {
	Continue(e);
	
	System.out.println("The elapsed time of the Pot is" + e);
	System.out.println("*****************************************");
	System.out.println("External-Phase before: " + phase);
	
	if (phaseIs("Passive")) {
	    for (int i = 0; i < x.getLength(); i++)
		if (messageOnPort(x, "TempStatus", i)) {
		    if (x.getValOnPort("TempStatus", i).toString() != "0")
			received_temp = Double.parseDouble(x.getValOnPort("TempStatus", i).toString());
			holdIn("Reading", 0);
		}
	}
	System.out.println("External-Phase after: " + phase);
    }

    public void deltint() {
	    
	System.out.println("Internal-Phase before: " + phase);
	if (phaseIs("Reading")) {
	    temp_count++;
	    pot_temp = (pot_temp * (temp_count-1) + received_temp)/temp_count;
	    phase = "Passive";
	    sigma = INFINITY;
	}
	try {
	    PrintWriter writer = new PrintWriter("pot-vals.txt", "UTF-8");
	    writer.println("PotTemp:" + Double.toString(pot_temp));
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

	String average_temp = Double.toString(pot_temp);
	m.add(makeContent("PotTemp", new entity(average_temp)));

	return m;
    }

    public void showState() {
	super.showState();
    }
    
    public String getTooltipText() {
	return super.getTooltipText() + "\n" + " Pot Temp: " + Double.toString(pot_temp);
    }
}
