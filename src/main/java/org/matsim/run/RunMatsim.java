/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.run;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.roadpricing.ControlerDefaultsWithRoadPricingModule;
import org.matsim.roadpricing.RoadPricingConfigGroup;
import org.matsim.roadpricing.RoadPricingModule;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import static java.lang.Math.random;


public class RunMatsim {

    private static boolean createNewPopulation = false;

    public static void main(String[] args) {

        Config config ;
        if ( args.length==0 || args[0]=="" ) {
            config = ConfigUtils.loadConfig( "scenarios/Cupchino/ConfigBase.xml",new RoadPricingConfigGroup()) ;
            config.controler().setLastIteration(10);
            config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );
        } else {
            config = ConfigUtils.loadConfig(args[0]) ;
        }

        Scenario scenario = ScenarioUtils.loadScenario(config);
        config.controler().setOutputDirectory(config.controler().getOutputDirectory() +
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split(":")[0]);

        if (createNewPopulation){
            createPopulation(scenario);
        }



        Controler controler = new Controler( scenario ) ;
        controler.setModules(new ControlerDefaultsWithRoadPricingModule());


        controler.run();

    }

    private static void createPopulation(Scenario scenario) {
        PopulationFactory populationFactory = scenario.getPopulation().getFactory();

        Coord hCoord1 = new Coord(24100,0);
        Coord hCoord2 = new Coord(12100,20890);
        Coord hCoord3 = new Coord(12100,-20890);


        //Coord wCoord = new Coord(-3000+Math.random()*6000,-3000+Math.random()*3000);
        for (int i = 0; i < 100; i++) {
            Person person_1 = populationFactory.createPerson(Id.createPersonId("agent_from_4_" + i));
            Plan plan_1 = populationFactory.createPlan();
            Activity activity1_1 = populationFactory.createActivityFromCoord("h", hCoord1);
            activity1_1.setEndTime(6 * 3600 + i);
            Activity activity_1 = populationFactory.createActivityFromCoord("w", new Coord(-1000+Math.random()*2000,-1000+Math.random()*2000));
            activity_1.setEndTime(activity1_1.getEndTime() + 3600 + 3600 * 8);
            Leg leg = populationFactory.createLeg("car");
            plan_1.addActivity(activity1_1);
            plan_1.addLeg(leg);
            plan_1.addActivity(activity_1);
            person_1.addPlan(plan_1);
            scenario.getPopulation().addPerson(person_1);

            Person person_2 = populationFactory.createPerson(Id.createPersonId("agent_from_6_" + i));
            Plan plan_2 = populationFactory.createPlan();
            Activity activity2_1 = populationFactory.createActivityFromCoord("h", hCoord2);
            activity2_1.setEndTime(6 * 3600 + i);
            Activity activity2_2 = populationFactory.createActivityFromCoord("w", new Coord(-1000+Math.random()*2000,-1000+Math.random()*2000));
            activity2_2.setEndTime(activity2_1.getEndTime() + 3600 + 3600 * 8);
            plan_2.addActivity(activity2_1);
            plan_2.addLeg(leg);
            plan_2.addActivity(activity2_2);
            person_2.addPlan(plan_2);
            scenario.getPopulation().addPerson(person_2);

            Person person_3 = populationFactory.createPerson(Id.createPersonId("agent_from_2_" + i));
            Plan plan_3 = populationFactory.createPlan();
            Activity activity3_1 = populationFactory.createActivityFromCoord("h", hCoord3);
            activity3_1.setEndTime(6 * 3600 + i);
            Activity activity3_2 = populationFactory.createActivityFromCoord("w", new Coord(-1000+Math.random()*2000,-1000+Math.random()*2000));
            activity3_2.setEndTime(activity3_1.getEndTime() + 3600 + 3600 * 8);
            plan_3.addActivity(activity3_1);
            plan_3.addLeg(leg);
            plan_3.addActivity(activity3_2);
            person_3.addPlan(plan_3);
            scenario.getPopulation().addPerson(person_3);
        }
    }


}
