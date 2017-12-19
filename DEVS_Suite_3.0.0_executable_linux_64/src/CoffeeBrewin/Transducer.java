/*     
 *    
 *  Author     : Neal DeBuhr
 *  Version    : DEVS-Suite 3.0.0  
 *  Date       : 12-19-2017
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

public class Transducer extends ViewableAtomic {
    // ViewableAtomic is used instead
    // of atomic due to its
    // graphics capability
    
    public Transducer() {
	this("Transducer");
    }

    public Transducer(String name) {
	super(name);
	addInport("CoffeeTemp");
	}

    public void initialize() {
	phase = "Passive";
	sigma = INFINITY;
	super.initialize();
    }

    public void deltext(double e, message x) {
	Continue(e);
    }

    public void deltint() {
    }

    public void deltcon(double e, message x) {
	deltext(0, x);
	deltint();
    }

    public message out() {
	message m = new message();
	return m;
    }

    public void showState() {
	super.showState();
    }
    
    public String getTooltipText() {
	return super.getTooltipText();
    }
}
