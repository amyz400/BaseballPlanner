package com.baseballPlanner;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.FieldPositionsConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by aziring on 5/9/17.
 */
@Component
public class ApplicationListenerComponent implements ApplicationListener<ApplicationReadyEvent>  {

    public ApplicationListenerComponent() {
        System.out.println("**** in ApplicationListenerComponent constructor");
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationStartedEvent) {

        System.out.println("**** in onApplicationEvent");

        // default population for outfield positions
        FieldPositionsConfiguration.addOutfieldPosition(FieldPositionEnum.LF);
        FieldPositionsConfiguration.addOutfieldPosition(FieldPositionEnum.RF);
        FieldPositionsConfiguration.addOutfieldPosition(FieldPositionEnum.CF);

        // default population for infield positions
        FieldPositionsConfiguration.addInfieldPosition(FieldPositionEnum.SECONDB);
        FieldPositionsConfiguration.addInfieldPosition(FieldPositionEnum.THIRDB);
        FieldPositionsConfiguration.addInfieldPosition(FieldPositionEnum.SS);
        FieldPositionsConfiguration.addInfieldPosition(FieldPositionEnum.CATCHER);

        // default population of premium positions
        FieldPositionsConfiguration.addPremiumPosition(FieldPositionEnum.FIRSTB);
        FieldPositionsConfiguration.addPremiumPosition(FieldPositionEnum.PITCHER);
    }
}
