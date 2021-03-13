// ----------------------------------------------------------------------------
// Copyright (C) 2014 Louise A. Dennis and Michael Fisher 
// 
// This file is part of Gwendolen
//
// Gwendolen is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
// 
// Gwendolen is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with Gwendolen if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
// 
// To contact the authors:
// http://www.csc.liv.ac.uk/~lad
//----------------------------------------------------------------------------

package avror.roadjunction;


import ail.mas.DefaultEnvironmentwRandomness;
// import ail.mas.DefaultEnvironment;
import ail.syntax.Action;
import ail.syntax.Unifier;
import ail.syntax.NumberTerm;
import ail.syntax.NumberTermImpl;
import ail.syntax.Predicate;
import ail.util.AILexception;
import ajpf.MCAPLJobber;
import ajpf.util.AJPFLogger;

/**
 * This is a simple class representing a search and rescue environment on a grid.  For use 
 * with the Gwendolen Tutorials.
 * @author lad
 * 
 * Adapted for AV-RoR Road Junction
 *
 * 19.11.2020: retaking previous versions to implement environment for rules 170-71-72
 * new actions: approach, check-sign, check-safe-gap, ...
 * trying to add dynamic and randomness
 * added "DefaultEnvironmentwRandomness"
 * added "implements MCAPLJobber" and the corresponding methods (see the last lines of this code)
 * so far, we are only using *randomness*
 * ...
 */
public class RoadJunctionEnv extends DefaultEnvironmentwRandomness implements MCAPLJobber {
	String logname = "avror.roadjunction.RoadJunctionEnv";
	
	// grid settings:
	// whole grid = {(0,0); (0,1); (0,2); (1,0); (1,1); (1,2); (2,0); (2,1); (2,2)}
	// SAFE Spots = {(2,0); (2,2)}	
	// TARGET Spots = {(1,0); (1,1); (1,2); (2,1)}
	// TARGET Spots means "busy road junction" 
	// SAFE Spots means "free road junction"
	// AND if all TARGET Spots are "free", also means "free road junction"
	// possible AV positions = {0,0 (away); 0,1 (at-RJ); 1,1 (entered-RJ)}
	// signs (stop/give-way) = {0,2}
	
	// three (possible) road users at three different positions
	// ENV settings for tests, there are road users at: 
	// (2,0); (2,2)  i.e., RJ is free!
	// (2,0); (2,1); (2,2)  i.e., RJ is free!
	// (1,0); (2,1); (2,2)  i.e., RJ is busy!
	// (1,0); (1,1); (1,2)  i.e., RJ is busy!	
	double road_user1_x = 1; 
	double road_user1_y = 0; 
	
	double road_user2_x = 2;
	double road_user2_y = 0;
	
	double road_user3_x = 2;
	double road_user3_y = 2;
		
/*	not using but, ... siren; allclear; could be used for traffic lights.
	double siren_x = road_user4_x;
	double siren_y = road_user4_y;
	
	double allclear_x = 0;
	double allclear_y = 0;
---------------------------*/
	
	// AV-initial position, AV is away from RJ at (0,0). 
	double av_x = 0;
	double av_y = 0;
	
	// av_road_user(i) represents AV target position in the grid; 
	// if there is a road user at AV target, flag is set as busy (TRUE), otherwise is free (FALSE). 
	boolean av_road_user1 = false;	
	boolean av_road_user2 = false;
	boolean av_road_user3 = false;
	
	boolean warning_message = false;
	boolean nowarning_message = false;
	
	
	/* Adding dynamic feature in the environment */

	public RoadJunctionEnv() {
		super();
		getScheduler().addJobber(this);
	}
	
	@Override
	public int compareTo(MCAPLJobber o) {
		return o.getName().compareTo(getName());
	}

	@Override
	public void do_job() {
		Predicate road_user = new Predicate("road_user");
		addPercept(road_user);
		// Predicate no_road_user = new Predicate("no_road_user");
		// addPercept(no_road_user);
		getScheduler().perceptChanged();		
	}

	@Override
	public String getName() {
		return "avror.roadjunction.RoadJunctionEnv";
	}
	
	
	public Unifier executeAction(String agName, Action act) throws AILexception {
		Unifier u = new Unifier();
		
		// ACTION: approach_roadjunction (AV beliefs is away from RJ and should approach it)
		if (act.getFunctor().equals("approach_roadjunction")) {
			double x = ((NumberTerm) act.getTerm(0)).solve();
			double y = ((NumberTerm) act.getTerm(1)).solve();
			
			Predicate at_roadjunction = new Predicate("at_roadjunction");
			at_roadjunction.addTerm(new NumberTermImpl(x));
			at_roadjunction.addTerm(new NumberTermImpl(y));
			
			Predicate old_pos = new Predicate("at_roadjunction");
			old_pos.addTerm(new NumberTermImpl(av_x));
			old_pos.addTerm(new NumberTermImpl(av_y));
			
			removePercept(agName, old_pos);
			addPercept(agName, at_roadjunction);			
		}
		
		
		//  ACTION: check_sign used to verify if the sign is STOP (rule 171) or GIVE-WAY (rule 172)
		// uses *random* generation of booleans		
		if (act.getFunctor().equals("check_sign")) {
			double x = ((NumberTerm) act.getTerm(0)).solve();
			double y = ((NumberTerm) act.getTerm(1)).solve();
			
			Predicate stop_sign = new Predicate("stop_sign");
			// AV believes there is a stop sign at position (0,2)
			stop_sign.addTerm(new NumberTermImpl(x));
			stop_sign.addTerm(new NumberTermImpl(y));
	
			Predicate give_way_sign = new Predicate("give_way_sign");
			// AV believes there is a give-way sign at position (0,2)
			give_way_sign.addTerm(new NumberTermImpl(x));
			give_way_sign.addTerm(new NumberTermImpl(y));
										
			// random generation for the traffic sign
			// "true" is STOP-sign; "false" is GIVE-WAY-sign
			boolean flag_which_sign = random_booleans.nextBoolean();
			// used for tests
			// System.out.println("Which traffic sign?" + flag_which_sign);
			
			if (flag_which_sign) {
				addPercept(agName, stop_sign);
				System.out.println("Stop sign (rule 171).");
			} else {
				addPercept(agName, give_way_sign);
				System.out.println("Give away sign (rule 172).");
			}					
		}
		
		
		//  ACTION: watch (for road user at road junction)
		if (act.getFunctor().equals("watch")) {
			double x = ((NumberTerm) act.getTerm(0)).solve();
			double y = ((NumberTerm) act.getTerm(1)).solve();
						
			Predicate for_road_users = new Predicate("for_road_users");
			for_road_users.addTerm(new NumberTermImpl(x));
			for_road_users.addTerm(new NumberTermImpl(y));
			
			Predicate old_pos = new Predicate("for_road_users");
			old_pos.addTerm(new NumberTermImpl(av_x));
			old_pos.addTerm(new NumberTermImpl(av_y));
			
			removePercept(agName, old_pos);
			addPercept(agName, for_road_users);
							
			av_x = x;
			av_y = y;
						
			if (av_y == road_user1_y && av_x == road_user1_x && !av_road_user1) {
				Predicate road_user = new Predicate("road_user");
				road_user.addTerm(new NumberTermImpl(road_user1_x));
				road_user.addTerm(new NumberTermImpl(road_user1_y));
				addPercept(agName, road_user);
				System.out.println("Busy at road user 1 position.");
			} else {
				Predicate no_road_user = new Predicate("no_road_user");
				no_road_user.addTerm(new NumberTermImpl(av_x));
				no_road_user.addTerm(new NumberTermImpl(av_y));
				addPercept(agName, no_road_user);
				System.out.println("Free at road user 1 position.");
			}
									
			if (av_y == road_user2_y && av_x == road_user2_x && !av_road_user2) {
				Predicate road_user = new Predicate("road_user");
				road_user.addTerm(new NumberTermImpl(road_user2_x));
				road_user.addTerm(new NumberTermImpl(road_user2_y));
				addPercept(agName, road_user);
				System.out.println("Busy at road user 2 position.");
			} else {
				Predicate no_road_user = new Predicate("no_road_user");
				no_road_user.addTerm(new NumberTermImpl(av_x));
				no_road_user.addTerm(new NumberTermImpl(av_y));
				addPercept(agName, no_road_user);
				System.out.println("Free at road user 2 position.");
			}

			if (av_y == road_user3_y && av_x == road_user3_x && !av_road_user3) {
				Predicate road_user = new Predicate("road_user");
				road_user.addTerm(new NumberTermImpl(road_user3_x));
				road_user.addTerm(new NumberTermImpl(road_user3_y));
				addPercept(agName, road_user);
				System.out.println("Busy at road user 3 position.");
			} else {
				Predicate no_road_user = new Predicate("no_road_user");
				no_road_user.addTerm(new NumberTermImpl(av_x));
				no_road_user.addTerm(new NumberTermImpl(av_y));
				addPercept(agName, no_road_user);
				System.out.println("Free at road user 3 position.");
			}

			
		  //  ACTION: wait (when RJ is busy, it will wait at RJ)
		} if (act.getFunctor().equals("wait")) {
			if (av_x == road_user1_x) {
				if (av_y == road_user1_y && !av_road_user1) {
					Predicate waiting = new Predicate("waiting");
					waiting.addTerm(new Predicate("road_user"));
					addPercept(agName, waiting);
					av_road_user1 = true;
				}
			} else if (av_x == road_user2_x) {
				if (av_y == road_user2_y && !av_road_user2) {
					Predicate waiting = new Predicate("waiting");
					waiting.addTerm(new Predicate("road_user"));
					addPercept(agName, waiting);
					av_road_user2 = true;
				}
			} else if (av_x == road_user3_x) {
				if (av_y == road_user3_y && !av_road_user3) {
					Predicate waiting = new Predicate("waiting");
					waiting.addTerm(new Predicate("road_user"));
					addPercept(agName, waiting);
					av_road_user3 = true;
				}
			}			
		}
		
		
		//  ACTION: watching (keep watching for road users until there is NO road user at all)
		// enables the trigger "try_again"
		// uses *random* generation of booleans until there is no road user at AV target position
		if (act.getFunctor().equals("watching")) {
			double x = ((NumberTerm) act.getTerm(0)).solve();
			double y = ((NumberTerm) act.getTerm(1)).solve();
						
			Predicate try_again = new Predicate("try_again");
			try_again.addTerm(new NumberTermImpl(x));
			try_again.addTerm(new NumberTermImpl(y));
			
			Predicate old_pos = new Predicate("try_again");
			old_pos.addTerm(new NumberTermImpl(av_x));
			old_pos.addTerm(new NumberTermImpl(av_y));
			
			removePercept(agName, old_pos);
			addPercept(agName, try_again);
							
			av_x = x;
			av_y = y;
                                  
			// random generation
            av_road_user1 = random_booleans.nextBoolean();
            av_road_user2 = random_booleans.nextBoolean();
            av_road_user3 = random_booleans.nextBoolean();	

            // used for testing the road users positions (before randomness) 
            /* System.out.println("Road user 1: " + av_road_user1);
            System.out.println("Road user 2: " + av_road_user2);
            System.out.println("Road user 3: " + av_road_user3);
            */
            
            while (!(av_road_user1 == false && av_road_user2 == false && av_road_user3 == false)) {
            	    			
                av_road_user1 = random_booleans.nextBoolean();
                av_road_user2 = random_booleans.nextBoolean();
                av_road_user3 = random_booleans.nextBoolean();
            	
                // IF the three booleans are "false" -> there is no road user at any of the three (possible) positions
                /* if (av_road_user1 == false && av_road_user2 == false && av_road_user3 == false) {
                    Predicate no_road_user = new Predicate("no_road_user");
                    no_road_user.addTerm(new NumberTermImpl(av_x));
        			no_road_user.addTerm(new NumberTermImpl(av_y));
        			System.out.println("No Road User!");
        			addPercept(agName, no_road_user);
        			
                } else {
                    Predicate road_user = new Predicate("road_user");
        			road_user.addTerm(new NumberTermImpl(av_x));
        			road_user.addTerm(new NumberTermImpl(av_y));
        			System.out.println("Road User!");
        			addPercept(agName, road_user);
                } */
                
            }                    
            
            // used for testing the road users positions (after randomness)
            /* System.out.println("Road user 1: " + av_road_user1);
            System.out.println("Road user 2: " + av_road_user2);
            System.out.println("Road user 3: " + av_road_user3);
            */
            
            Predicate no_road_user = new Predicate("no_road_user");
            no_road_user.addTerm(new NumberTermImpl(av_x));
			no_road_user.addTerm(new NumberTermImpl(av_y));
			System.out.println("Road junction is free!");
			addPercept(agName, no_road_user);
			            
        }		
		
								
		// ACTION: check_safe_gap(X,Y)
		// generates a single random boolean and adds the respective perception
		// uses *random* generation of booleans
		if (act.getFunctor().equals("check_safe_gap")) {
			double x = ((NumberTerm) act.getTerm(0)).solve();
			double y = ((NumberTerm) act.getTerm(1)).solve();
			
			Predicate safe_gap = new Predicate("safe_gap");		
			safe_gap.addTerm(new NumberTermImpl(x));
			safe_gap.addTerm(new NumberTermImpl(y));
	
			Predicate no_safe_gap = new Predicate("no_safe_gap");
			no_safe_gap.addTerm(new NumberTermImpl(x));
			no_safe_gap.addTerm(new NumberTermImpl(y));
						
			// random generation for the SAFE_GAP
			// "true" is SAFE_GAP; "false" is NO_SAFE_GAP
			boolean flag_safe_gap = random_booleans.nextBoolean();
			System.out.println("Is there a safe gap? " + flag_safe_gap);
												
			if (flag_safe_gap) {
				addPercept(agName, safe_gap);				
			} else {
				addPercept(agName, no_safe_gap);
			}					
		}		
		
		
		// ACTION: checking(X,Y) keep checking until there is a safe_gap
		// enables the trigger "for_safe_gap"; adds a perception "new_safe_gap"
		// uses *random* generation of booleans		
		if (act.getFunctor().equals("checking")) {
			double x = ((NumberTerm) act.getTerm(0)).solve();
			double y = ((NumberTerm) act.getTerm(1)).solve();
			
			Predicate for_safe_gap = new Predicate("for_safe_gap");
			for_safe_gap.addTerm(new NumberTermImpl(x));
			for_safe_gap.addTerm(new NumberTermImpl(y));
			
			addPercept(agName, for_safe_gap);
			
			// random generation for the SAFE_GAP
			// "true" means a NEW_SAFE_GAP; "false" means there is no safe gap yet.
			boolean flag_safe_gap_2 = random_booleans.nextBoolean();
			while (!flag_safe_gap_2) { 
				// while there is NO safe_gap keep the loop
				flag_safe_gap_2 = random_booleans.nextBoolean();
			}
			// test the output of random generation
			System.out.println("Now, is there a (new) safe gap? " + flag_safe_gap_2);
			
			Predicate new_safe_gap = new Predicate("new_safe_gap");		
			new_safe_gap.addTerm(new NumberTermImpl(x));
			new_safe_gap.addTerm(new NumberTermImpl(y));
			addPercept(agName, new_safe_gap);														
		}
		
		
		// ACTION: enter (AV has entered the road junction (double checks if there is a road user)) 
		if (act.getFunctor().equals("enter")) {
			if (av_x != road_user1_x) {
				if (av_y != road_user1_y && !av_road_user1) {				
					Predicate enter_roadjunction = new Predicate("enter_roadjunction");
					addPercept(agName, enter_roadjunction);					
				}
			} else if (av_x != road_user2_x) {
				if (av_y != road_user2_y && !av_road_user2) {										
					Predicate enter_roadjunction = new Predicate("enter_roadjunction");
					addPercept(agName, enter_roadjunction);										
				}
			} else if (av_x != road_user3_x) {
				if (av_y != road_user3_y && !av_road_user3) {										
					Predicate enter_roadjunction = new Predicate("enter_roadjunction");
					addPercept(agName, enter_roadjunction);															
				}
			}
		} 		
		
		
		super.executeAction(agName, act);
		
		if (warning_message) {
			AJPFLogger.info(logname, "Warning is Sounding");
			warning_message = false;
		}
		
		if (nowarning_message) {
			AJPFLogger.info(logname, "Warning Ceases");
			nowarning_message = false;
		}
		
		return u;		
	}

}