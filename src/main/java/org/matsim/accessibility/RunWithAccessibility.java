package org.matsim.accessibility;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.accessibility.AccessibilityConfigGroup;
import org.matsim.contrib.accessibility.AccessibilityModule;
import org.matsim.contrib.accessibility.Modes4Accessibility;
import org.matsim.contrib.accessibility.utils.AccessibilityUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.roadpricing.ControlerDefaultsWithRoadPricingModule;
import org.matsim.roadpricing.RoadPricingConfigGroup;
import sun.rmi.runtime.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RunWithAccessibility {
    public static void main(String[] args) {

        Config config ;
        if ( args.length==0 || args[0]=="" ) {
            config = ConfigUtils.loadConfig( "scenarios/Cupchino/ConfigBaseAccessibility.xml") ;
            config.controler().setLastIteration(0);
            config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );
        } else {
            config = ConfigUtils.loadConfig(args[0]) ;
        }

        AccessibilityConfigGroup accConfig = ConfigUtils.addOrGetModule(config, AccessibilityConfigGroup.class ) ;
        accConfig.setComputingAccessibilityForMode(Modes4Accessibility.freespeed, true);
        accConfig.setComputingAccessibilityForMode(Modes4Accessibility.car, true);
        accConfig.setComputingAccessibilityForMode(Modes4Accessibility.walk, true);




        Scenario scenario = ScenarioUtils.loadScenario(config);
        config.controler().setOutputDirectory(config.controler().getOutputDirectory() +
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split(":")[0]);



        Controler controler = new Controler( scenario ) ;
        List<String> activityTypes = AccessibilityUtils.collectAllFacilityOptionTypes(scenario);
        for (final String actType : activityTypes) { // Add an overriding module for each activity type.
            final AccessibilityModule module = new AccessibilityModule();
            module.setConsideredActivityType(actType);
            controler.addOverridingModule(module);
        }




        controler.run();

    }


}
