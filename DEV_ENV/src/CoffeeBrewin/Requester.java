/*     
 *    
 *  Author     : Neal DeBuhr
 *  Version    : DEVS-Suite 3.0.0  
 *  Date       : 12-19-2017
 */
package CoffeeBrewin;

import java.util.Random;
import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class Requester extends ViewableAtomic {

    // variable for sequencing order of internal transition states and thus outputs
    protected int count;
    
    public Requester() {
	this("Requester");
    }
    
    public Requester(String name) {
	super(name);
	addOutport("forUser");
	addOutport("forThermo");
    }
    
    public void initialize() {
	holdIn("active", 0);
	count = 0;
	super.initialize();
    }
    
    public void deltext(double e, message x) {
	Continue(e);
    }
    
    //time scheduler for producing outputs
    public void deltint() {
	Random randomGenerator = new Random();
	int next_event_time = randomGenerator.nextInt(50);
	if (count < 20) {
	    holdIn("active", next_event_time);
	} else {
	    passivate();
	}
	count = count + 1;
    }

    public message out() {

	//creating empty message
	message m = new message();

	//random selection of output message
	Random randomGenerator = new Random();
	int next_event_type = randomGenerator.nextInt(100000);

	String next_port;
	String next_val;
	if (next_event_type % 5 == 0) {
	    next_port = "forUser";
	    next_val = "On";
	} else if (next_event_type % 5 == 1) {
	    next_port = "forUser";
	    next_val = "Off";
	} else {
	    next_port = "forThermo";
	    next_val = Integer.toString(randomGenerator.nextInt(230));
	}
	
	content con = makeContent(next_port, new entity(next_val));
	System.out.println("--------Count ="+count);

	m.add(con);
	return m;
    }
    
    public void showState() {
	super.showState();
    }
    
    public String getTooltipText() {
	return super.getTooltipText() + "\n"  + " count: " + count;
    }
    
}
