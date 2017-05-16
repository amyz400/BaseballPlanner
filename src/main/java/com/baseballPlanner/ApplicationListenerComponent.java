package com.baseballPlanner;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.FieldPositionsConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by aziring on 5/9/17.
 */
@Component
public class ApplicationListenerComponent implements ApplicationListener<ApplicationStartedEvent>  {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

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
