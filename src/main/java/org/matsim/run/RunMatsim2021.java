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

import org.matsim.utils.gis.matsim2esri.network.FeatureGeneratorBuilderImpl;
import org.matsim.utils.gis.matsim2esri.network.LanesBasedWidthCalculator;
import org.matsim.utils.gis.matsim2esri.network.LineStringBasedFeatureGenerator;
import org.matsim.utils.gis.matsim2esri.network.Links2ESRIShape;
import org.matsim.utils.gis.matsim2esri.plans.SelectedPlans2ESRIShape;
import org.matsim.core.utils.geometry.geotools.MGC;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RunMatsim2021 {

    private static boolean createNewPopulation = false;

    public static void main(String[] args) {

        //Config config = ConfigUtils.loadConfig( "scenarios/Cupchino/config_0_2019.xml", new ConfigGroup[]{new EmissionsConfigGroup()}) ;
        Config config = ConfigUtils.loadConfig( "scenarios/Cupchino/config_0_2019.xml") ;
            //config.controler().setLastIteration(100);
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
        String shp = "shp/baseNetwork.shp";
        createShp(scenario, shp);


    }
    private static void createShp(Scenario scenario, String shp){
        FeatureGeneratorBuilderImpl builder = new FeatureGeneratorBuilderImpl(scenario.getNetwork(), "EPSG:32635");

        LanesBasedWidthCalculator widthCalculator = new LanesBasedWidthCalculator(scenario.getNetwork(), 0.5);
        LineStringBasedFeatureGenerator lineGenerator = new LineStringBasedFeatureGenerator(widthCalculator, MGC.getCRS("EPSG:32635"));
        builder.setFeatureGeneratorPrototype(lineGenerator.getClass());

        Links2ESRIShape links2ESRIShape = new Links2ESRIShape(scenario.getNetwork(), shp, builder);
        links2ESRIShape.write();
        //SelectedPlans2ESRIShape plans2ESRIShape = new SelectedPlans2ESRIShape(scenario.getPopulation(), scenario.getNetwork(),MGC.getCRS("EPSG:32635"),"shp");
        //plans2ESRIShape.write();

    }
}
