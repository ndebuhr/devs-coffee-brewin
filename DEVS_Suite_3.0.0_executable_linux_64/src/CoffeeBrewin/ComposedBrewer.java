package CoffeeBrewin;
import java.awt.*;

import GenCol.*;


import model.modeling.*;
import model.simulation.*;

import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;
import view.simView.*;

public class ComposedBrewer extends ViewableDigraph
{
    public ComposedBrewer(){
	super("ComposedBrewer");
	
	ViewableAtomic controller = new BrewerControl("BrewerControl", 3, 40,
						      2, 85, 200);
	// ViewableAtomic requester = new Requester();
	ViewableAtomic boiler = new Boiler("Boiler", 0.1, 200, 10,
					   1, 1, 0.05);

	ViewableAtomic pot = new Pot("CoffeePot");
	    
	add(controller);
	// add(requester);
	add(boiler);
	add(pot);
	
	addInport("inThermo");
	addInport("inUser");
	addOutport("outThermo");
	addOutport("outUser");
	addOutport("CoffeeTemp");
	
	addCoupling(this, "inThermo", controller, "inThermo");
	addCoupling(this, "inUser", controller, "inUser");
	// addCoupling(requester, "forThermo", controller, "inThermo");
	// addCoupling(requester, "forUser", controller, "inUser");
	addCoupling(controller, "outThermo", this, "outThermo");
	addCoupling(controller, "outUser", this, "outUser");

	addCoupling(controller, "outThermo", boiler, "ControlSignal");
	addCoupling(boiler, "Status", controller, "inThermo");

	addCoupling(boiler, "Status", pot, "TempStatus");
	addCoupling(pot, "PotTemp", this, "CoffeeTemp");
	
	addTestInput("inThermo", new entity("4"), 1);
	addTestInput("inThermo", new entity("8"), 5);
	addTestInput("inThermo", new entity("150"), 8);
	addTestInput("inThermo", new entity("210"), 4);
	addTestInput("inUser", new entity("Off"), 0);
	addTestInput("inUser", new entity("On"), 1);
	addTestInput("inUser", new entity("Off"), 4);
	addTestInput("inUser", new entity("On"), 8);
	addTestInput("inUser", new entity("Off"), 12);
    }
    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(591, 269);
	
    }
}
