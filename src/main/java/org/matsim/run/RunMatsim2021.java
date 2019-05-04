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
import org.matsim.api.core.v01.population.*;
import org.matsim.contrib.emissions.EmissionModule;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.roadpricing.ControlerDefaultsWithRoadPricingModule;
import org.matsim.roadpricing.RoadPricingConfigGroup;
import org.matsim.contrib.emissions.example.RunEmissionToolOnlineExampleV2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RunMatsim2021 {

    private static boolean createNewPopulation = false;

    public static void main(String[] args) {

        //Config config = ConfigUtils.loadConfig( "scenarios/Cupchino/ConfigBase2021_1.xml", new ConfigGroup[]{new EmissionsConfigGroup()}) ;
        Config config = ConfigUtils.loadConfig( "scenarios/Cupchino/ConfigBase2021_1.xml") ;
            //config.controler().setLastIteration(10);
            config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );

        Scenario scenario = ScenarioUtils.loadScenario(config);
        config.controler().setOutputDirectory(config.controler().getOutputDirectory() +
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split(":")[0]);

        Controler controler = new Controler( scenario ) ;
//        controler.addOverridingModule(new AbstractModule() {
//            public void install() {
//                this.bind(EmissionModule.class).asEagerSingleton();
//            }
//        });
        controler.setModules(new ControlerDefaultsWithRoadPricingModule());
        controler.run();


    }
}
