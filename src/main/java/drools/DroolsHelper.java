package drools;
import models.ProductOrder;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import java.util.HashSet;
import java.util.Set;

public class DroolsHelper {
    private KieContainer kieContainer;

    public DroolsHelper() {
        KieServices kieServices = KieServices.Factory.get();
        kieContainer = kieServices.getKieClasspathContainer();
    }

    public void applyRules(ProductOrder productOrder) {
        KieSession kieSession = kieContainer.newKieSession("ksession-rules");
        // Define working days
        Set<String> workingDays = new HashSet<>();
//        workingDays.add("Monday");
//        workingDays.add("Tuesday");
//        workingDays.add("Wednesday");
//        workingDays.add("Thursday");
        workingDays.add("Friday");

        // Set the global variable in the KieSession
        kieSession.setGlobal("workingDays", workingDays);

        FactHandle productOrderFactHandle = kieSession.insert(productOrder);
        kieSession.fireAllRules();
        kieSession.update(productOrderFactHandle, productOrder);
        kieSession.dispose();
    }
}
